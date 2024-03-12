/*
 * Copyright (c) 2024 ETH Zürich, IT Services
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JSONMapper extends ObjectMapper {

    private static final long serialVersionUID = 2883304481547670626L;

    public JSONMapper() {
        super();
        super.registerModule(new JodaModule());
        super.configure(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                false);
        super.configure(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_ZONE_ID,
                false);
        super.setSerializationInclusion(Include.NON_NULL);
    }

}
