/*
 * Copyright (c) 2022 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.bot;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.http.OAuth2ErrorHandler;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ethz.seb.sps.domain.api.API;
import ch.ethz.seb.sps.domain.api.JSONMapper;
import ch.ethz.seb.sps.domain.model.screenshot.ScreenshotData;
import ch.ethz.seb.sps.utils.Constants;
import ch.ethz.seb.sps.utils.Utils;

public class WebsocketClientBot {

    private static final Logger log = LoggerFactory.getLogger(WebsocketClientBot.class);

    private final Random random = new Random();
    private final ExecutorService executorService;
    private Profile profile;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public WebsocketClientBot(final Properties properties) throws Exception {

        final String profileName = properties.getProperty("profile");
        if (StringUtils.isNotBlank(profileName)) {
            try {
                final ResourceLoader resourceLoader = new DefaultResourceLoader();
                final Resource resource = resourceLoader.getResource("classpath:/profiles/" + profileName + ".json");
                this.profile = this.jsonMapper.readValue(resource.getInputStream(), Profile.class);
            } catch (final Exception e) {
                log.error("Failed to load profile: {}", profileName, e);
                this.profile = new Profile();
                throw e;
            }
        } else {
            this.profile = new Profile();
        }

        this.executorService = Executors.newFixedThreadPool(this.profile.numberOfConnections);
        for (int i = 0; i < this.profile.numberOfConnections; i++) {
            final String sessionId = StringUtils.isNotBlank(this.profile.sessionId)
                    ? this.profile.sessionId
                    : "seb_" + getRandomName();

            this.executorService.execute(new ConnectionBot(sessionId));

            try {
                Thread.sleep(this.profile.spawnDelay);
            } catch (final Exception e) {

            }
        }

        this.executorService.shutdown();
    }

    private final class ConnectionBot implements Runnable {

        private final String name;
        private final OAuth2RestTemplate restTemplate;
        private final JSONMapper jsonMapper = new JSONMapper();

        private final String handshakeURI = WebsocketClientBot.this.profile.webserviceAddress +
                WebsocketClientBot.this.profile.apiPath +
                API.PARAM_MODEL_PATH_SEGMENT +
                API.SESSION_HANDSHAKE_ENDPOINT;

        private final String imageDataURI = WebsocketClientBot.this.profile.websocketAddress +
        //"/socket";
                WebsocketClientBot.this.profile.apiPath + "/wsock";

//        private final String imageUploadURI = WebsocketClientBot.this.profile.websocketAddress +
//                WebsocketClientBot.this.profile.apiPath + "/wsock/screenshot";

        public ConnectionBot(final String name) {
            this.name = name;
            this.restTemplate = createRestTemplate(null);
        }

        @Override
        public void run() {
            log.info("ConnectionBot {} : SEB-Connection-Bot started: {}", WebsocketClientBot.this.profile);

            String sessionUUID = null;
            OAuth2AccessToken accessToken = null;

            try {

                accessToken = this.restTemplate.getAccessToken();
                log.info("ConnectionBot {} : Got access token: {}", this.name, accessToken);
                sessionUUID = createSession();

                if (sessionUUID == null) {
                    throw new RuntimeException("Handshake failed, no session has been generated!");
                }

                log.info("Successfully initialized session: {}", sessionUUID);

            } catch (final Exception e) {
                log.error("ConnectionBot {} : Failed : ", this.name, e);
            }

            final WebSocketSession screenshotDataSession = createScreenshotDataSession(
                    accessToken.getValue(),
                    sessionUUID);

            if (sessionUUID != null && accessToken != null && screenshotDataSession != null) {

                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + WebsocketClientBot.this.profile.runtime;
                long currentTime = startTime;
                long lastScreenshotTime = startTime;

                while (currentTime < endTime) {
                    if (currentTime - lastScreenshotTime >= WebsocketClientBot.this.profile.screenshotInterval) {
                        screenshot(sessionUUID, screenshotDataSession);
                        lastScreenshotTime = currentTime;
                    }

                    try {
                        Thread.sleep(50);
                    } catch (final Exception e) {
                    }
                    currentTime = System.currentTimeMillis();
                }
            }
        }

        private void screenshot(final String sessionUUID, final WebSocketSession screenshotDataSession) {

            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                takeScreenshot(byteArrayOutputStream);

                // first send the metadata
                final ScreenshotData screenshotData = new ScreenshotData(
                        null,
                        sessionUUID,
                        Utils.getMillisecondsNow(),
                        null,
                        "jpg",
                        "some meta data");

                final String screenshotDataJSON = this.jsonMapper.writeValueAsString(screenshotData);
                screenshotDataSession.sendMessage(new BinaryMessage(screenshotDataJSON.getBytes("UTF8")));

                // then send the screenshot
                screenshotDataSession.sendMessage(new BinaryMessage(byteArrayOutputStream.toByteArray()));
                //ByteArrayInputStream in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        private String createSession() {
            log.info("ConnectionBot {} : init connection", this.name);

            final ResponseEntity<Void> exchange = this.restTemplate.exchange(
                    this.handshakeURI,
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    Void.class,
                    WebsocketClientBot.this.profile.groupId);

            if (exchange.getStatusCode() != HttpStatus.OK) {
                log.error("Handshake failed: {}", exchange.getStatusCode());
                return null;
            }

            return exchange.getHeaders().getFirst(API.SESSION_HEADER_UUID);
        }

        private WebSocketSession createScreenshotDataSession(final String accessToken, final String sessionId) {
            try {
                return createSession(this.imageDataURI, accessToken, sessionId);
            } catch (final Exception e) {
                log.error("Failed to create session: ", e);
                return null;
            }
        }

        private WebSocketSession createSession(
                final String uri,
                final String accessToken,
                final String sessionId) throws Exception {

            log.info("Try connect to: {}", uri);

            final WebSocketClient client = new StandardWebSocketClient();
            final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
            webSocketHttpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            webSocketHttpHeaders.set(API.SESSION_HEADER_UUID, sessionId);
            return client.doHandshake(
                    new WebSocketHandler() {

                        @Override
                        public void handleTransportError(final WebSocketSession session, final Throwable exception)
                                throws Exception {
                            log.error("error: ", exception);
                        }

                        @Override
                        public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message)
                                throws Exception {
                            log.info("Message: {}", message.getPayload());
                        }

                        @Override
                        public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                            log.info("  !!!! Connection established !!!! ");
                        }

                        @Override
                        public void afterConnectionClosed(final WebSocketSession session, final CloseStatus closeStatus)
                                throws Exception {
                            log.info("  !!!! Connection closed !!!! status: {}", closeStatus);
                            Thread.dumpStack();
                        }

                        @Override
                        public boolean supportsPartialMessages() {
                            return true;
                        }
                    },
                    webSocketHttpHeaders,
                    URI.create(uri)).get();
        }
    }

    private final Rectangle screenRect = new Rectangle(0, 0, 800, 600);

    private void takeScreenshot(final OutputStream out) {
        try {

            final BufferedImage capture = new Robot().createScreenCapture(this.screenRect);
            ImageIO.write(capture, "jpg", out);

        } catch (final Exception e) {
            e.printStackTrace();
            IOUtils.closeQuietly(out);
        }
    }

    private OAuth2RestTemplate createRestTemplate(final String scopes) {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        final ClientCredentialsResourceDetails clientCredentialsResourceDetails =
                new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails
                .setAccessTokenUri(this.profile.webserviceAddress + this.profile.accessTokenEndpoint);
        clientCredentialsResourceDetails.setClientId(this.profile.clientId);
        clientCredentialsResourceDetails.setClientSecret(this.profile.clientSecret);
        if (StringUtils.isBlank(scopes)) {
            clientCredentialsResourceDetails.setScope(Arrays.asList("read", "write"));
        } else {
            clientCredentialsResourceDetails.setScope(
                    Arrays.asList(StringUtils.split(scopes, Constants.LIST_SEPARATOR)));
        }

        final OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(clientCredentialsResourceDetails);
        restTemplate.setErrorHandler(new OAuth2ErrorHandler(clientCredentialsResourceDetails) {

            @Override
            public void handleError(final ClientHttpResponse response) throws IOException {
                System.out.println("********************** handleError: " + response.getStatusCode());
                System.out.println("********************** handleError: " + response.getStatusText());
                super.handleError(response);
            }

        });
        restTemplate
                .getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    private String getRandomName() {
        final StringBuilder sb = new StringBuilder(String.valueOf(this.random.nextInt(100)));
        while (sb.length() < 3) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    public static void main(final String[] args) throws Exception {
        new WebsocketClientBot(System.getProperties());
    }

}