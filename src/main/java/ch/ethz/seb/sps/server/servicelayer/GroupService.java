/*
 * Copyright (c) 2024 ETH Zürich, IT Services
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.server.servicelayer;

import java.util.Collection;

public interface GroupService {

    /** Use this to get a predicated list of all group PK's for that the user has read access
     * if there is no overall read access for groups.
     *
     * @return */
    Collection<Long> getReadPrivilegedPredication();

}
