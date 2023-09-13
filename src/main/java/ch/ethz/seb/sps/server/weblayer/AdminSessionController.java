/*
 * Copyright (c) 2023 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.server.weblayer;

import ch.ethz.seb.sps.domain.Domain;
import ch.ethz.seb.sps.domain.api.API;
import ch.ethz.seb.sps.domain.api.APIErrorException;
import ch.ethz.seb.sps.domain.api.POSTMapper;
import ch.ethz.seb.sps.domain.model.service.Session;
import ch.ethz.seb.sps.domain.model.service.Session.ImageFormat;
import ch.ethz.seb.sps.server.datalayer.batis.mapper.SessionRecordDynamicSqlSupport;
import ch.ethz.seb.sps.server.datalayer.dao.AuditLogDAO;
import ch.ethz.seb.sps.server.datalayer.dao.GroupDAO;
import ch.ethz.seb.sps.server.datalayer.dao.SessionDAO;
import ch.ethz.seb.sps.server.servicelayer.BeanValidationService;
import ch.ethz.seb.sps.server.servicelayer.PaginationService;
import ch.ethz.seb.sps.server.servicelayer.UserService;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.SqlTable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("${sps.api.admin.endpoint.v1}" + API.ADMIN_SESSION_ENDPOINT)
public class AdminSessionController extends EntityController<Session, Session> {

    private final GroupDAO groupDAO;

    public AdminSessionController(
            final GroupDAO groupDAO,
            final UserService userService,
            final SessionDAO entityDAO,
            final AuditLogDAO auditLogDAO,
            final PaginationService paginationService,
            final BeanValidationService beanValidationService) {

        super(userService, entityDAO, auditLogDAO, paginationService, beanValidationService);
        this.groupDAO = groupDAO;
    }

    @Override
    protected Session createNew(final POSTMapper postParams) {

        String uuid = postParams.getString(Domain.SESSION.ATTR_UUID);
        if (StringUtils.isNotBlank(uuid)) {
            try {
                UUID.fromString(uuid);
            } catch (final Exception e) {
                uuid = UUID.randomUUID().toString();
            }
        } else {
            uuid = UUID.randomUUID().toString();
        }

        final String groupId = postParams.getString(Domain.SESSION.ATTR_GROUP_ID);
        final Long groupPK = this.groupDAO.modelIdToPK(groupId);
        if (groupPK == null) {
            throw APIErrorException.ofMissingAttribute(Domain.SESSION.ATTR_GROUP_ID, groupId);
        }

        return new Session(
                null,
                groupPK,
                uuid,
                postParams.getString(Domain.SESSION.ATTR_CLIENT_NAME),
                postParams.getString(Domain.SESSION.ATTR_CLIENT_IP),
                postParams.getString(Domain.SESSION.ATTR_CLIENT_MACHINE_NAME),
                postParams.getString(Domain.SESSION.ATTR_CLIENT_OS_NAME),
                postParams.getString(Domain.SESSION.ATTR_CLIENT_VERSION),
                postParams.getEnum(Domain.SESSION.ATTR_IMAGE_FORMAT, ImageFormat.class),
                null, null, null);
    }

    @Override
    protected Session merge(final Session modifyData, final Session existingEntity) {
        return new Session(
                existingEntity.id,
                null,
                existingEntity.uuid,
                modifyData.clientName,
                modifyData.clientIP,
                modifyData.clientMachineName,
                modifyData.clientOSName,
                modifyData.clientVersion,
                modifyData.imageFormat,
                existingEntity.creationTime,
                null,
                null);
    }

    @Override
    protected SqlTable getSQLTableOfEntity() {
        return SessionRecordDynamicSqlSupport.sessionRecord;
    }

}
