/*
 * Copyright (c) 2024 ETH Zürich, IT Services
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.server.datalayer.dao;

import ch.ethz.seb.sps.domain.model.user.ClientAccess;
import ch.ethz.seb.sps.utils.Result;

public interface ClientAccessDAO extends ActivatableEntityDAO<ClientAccess, ClientAccess> {

    public Result<CharSequence> getEncodedClientPWD(String clientId, boolean checkActive);

}
