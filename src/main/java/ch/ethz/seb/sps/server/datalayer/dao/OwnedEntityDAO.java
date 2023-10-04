/*
 * Copyright (c) 2023 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.server.datalayer.dao;

import java.util.Set;

import ch.ethz.seb.sps.utils.Result;

public interface OwnedEntityDAO {

    Result<Set<Long>> getAllOwnedEntityPKs(String userUUID);

}
