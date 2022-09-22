/*
 * Copyright (c) 2022 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.server.servicelayer.dao;

import java.io.InputStream;

import ch.ethz.seb.sps.utils.Result;

public interface ScreenshotDAO {

    Result<InputStream> getImage(Long pk, final String groupId, final String sessionId);

    default Result<InputStream> getImage(final Long pk) {
        return getImage(pk, null, null);
    }

    Result<Long> storeImage(Long pk, String groupId, String sessionId, InputStream in);

    default Result<Long> storeImage(final Long pk, final InputStream in) {
        return storeImage(pk, null, null, in);
    }

}
