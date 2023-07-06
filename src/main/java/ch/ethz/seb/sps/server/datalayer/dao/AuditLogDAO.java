/*
 * Copyright (c) 2023 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.server.datalayer.dao;

import java.util.Collection;

import ch.ethz.seb.sps.domain.model.Entity;
import ch.ethz.seb.sps.domain.model.EntityKey;
import ch.ethz.seb.sps.domain.model.user.AuditLog;
import ch.ethz.seb.sps.domain.model.user.AuditLog.AuditLogType;
import ch.ethz.seb.sps.domain.model.user.UserInfo;
import ch.ethz.seb.sps.utils.Result;

public interface AuditLogDAO extends EntityDAO<AuditLog, AuditLog> {

    <T extends Entity> Result<T> log(AuditLogType logType, T entity);

    <T extends Entity> Result<T> logCreate(T entity);

    <T extends Entity> Result<T> logModify(T entity);

    <T extends Entity> Result<T> logDelete(T entity);

    Result<Collection<EntityKey>> logDeleted(Collection<EntityKey> entities);

    void logLogin(UserInfo userInfo);

    void logLogout(UserInfo userInfo);

}