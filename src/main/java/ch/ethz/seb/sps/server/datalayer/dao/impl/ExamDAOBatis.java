package ch.ethz.seb.sps.server.datalayer.dao.impl;

import static ch.ethz.seb.sps.server.datalayer.batis.mapper.ExamRecordDynamicSqlSupport.examRecord;
import static ch.ethz.seb.sps.server.datalayer.batis.mapper.GroupRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.ethz.seb.sps.domain.Domain;
import ch.ethz.seb.sps.domain.Domain.EXAM;
import ch.ethz.seb.sps.domain.api.API;
import ch.ethz.seb.sps.domain.model.EntityKey;
import ch.ethz.seb.sps.domain.model.EntityType;
import ch.ethz.seb.sps.domain.model.FilterMap;
import ch.ethz.seb.sps.domain.model.service.Exam;
import ch.ethz.seb.sps.domain.model.user.EntityPrivilege;
import ch.ethz.seb.sps.server.datalayer.batis.mapper.ExamRecordDynamicSqlSupport;
import ch.ethz.seb.sps.server.datalayer.batis.mapper.ExamRecordMapper;
import ch.ethz.seb.sps.server.datalayer.batis.model.ExamRecord;
import ch.ethz.seb.sps.server.datalayer.dao.DuplicateEntityException;
import ch.ethz.seb.sps.server.datalayer.dao.EntityPrivilegeDAO;
import ch.ethz.seb.sps.server.datalayer.dao.ExamDAO;
import ch.ethz.seb.sps.server.datalayer.dao.GroupDAO;
import ch.ethz.seb.sps.server.datalayer.dao.NoResourceFoundException;
import ch.ethz.seb.sps.server.servicelayer.UserService;
import ch.ethz.seb.sps.server.weblayer.BadRequestException;
import ch.ethz.seb.sps.utils.Result;
import ch.ethz.seb.sps.utils.Utils;

@Service
public class ExamDAOBatis implements ExamDAO {

    private final ExamRecordMapper examRecordMapper;
    private final GroupDAO groupDAO;
    private final EntityPrivilegeDAO entityPrivilegeDAO;
    private final UserService userService;

    public ExamDAOBatis(
            final ExamRecordMapper examRecordMapper,
            final GroupDAO groupDAO,
            final EntityPrivilegeDAO entityPrivilegeDAO,
            final UserService userService) {

        this.examRecordMapper = examRecordMapper;
        this.groupDAO = groupDAO;
        this.entityPrivilegeDAO = entityPrivilegeDAO;
        this.userService = userService;
    }

    @Override
    public EntityType entityType() {
        return EntityType.EXAM;
    }

    @Override
    public Long modelIdToPK(final String modelId) {
        final Long pk = isPK(modelId);
        if (pk != null) {
            return pk;
        } else {
            return pkByUUID(modelId).getOr(null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Exam> byPK(final Long id) {

        return recordByPK(id)
                .map(this::toDomainModel);
    }

    @Override
    public Result<Exam> byModelId(final String id) {
        try {
            final long pk = Long.parseLong(id);
            return this.byPK(pk);
        } catch (final Exception e) {
            return recordByUUID(id)
                    .map(this::toDomainModel);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUUID(final String examUUID) {
        try {

            final Long count = this.examRecordMapper
                    .countByExample()
                    .where(ExamRecordDynamicSqlSupport.uuid, SqlBuilder.isEqualTo(examUUID))
                    .build()
                    .execute();

            return count != null && count.intValue() > 0;
        } catch (final Exception e) {
            log.error("Failed to check exam exists: ", e);
            return false;
        }
    }

    @Override
    public Result<Collection<Exam>> allOf(final Set<Long> pks) {
        return Result.tryCatch(() -> {

            if (pks == null || pks.isEmpty()) {
                return Collections.emptyList();
            }

            return this.examRecordMapper
                    .selectByExample()
                    .where(ExamRecordDynamicSqlSupport.id, isIn(new ArrayList<>(pks)))
                    .build()
                    .execute()
                    .stream()
                    .map(this::toDomainModel)
                    .collect(Collectors.toList());
        });
    }

    //todo: question: should we remove this function
    @Override
    @Transactional(readOnly = true)
    public Result<Collection<Exam>> allMatching(final FilterMap filterMap, final Predicate<Exam> predicate) {
        return Result.tryCatch(() -> {

            final Boolean active = filterMap.getBooleanObject(API.ACTIVE_FILTER);
            final Long fromTime = filterMap.getLong(API.PARAM_FROM_TIME);
            final Long toTime = filterMap.getLong(API.PARAM_TO_TIME);

            final List<Exam> result = this.examRecordMapper
                    .selectByExample()
                    .where(
                            ExamRecordDynamicSqlSupport.terminationTime,
                            (active != null) ? active ? SqlBuilder.isNull() : SqlBuilder.isNotNull()
                                    : SqlBuilder.isEqualToWhenPresent(() -> null))
                    .and(
                            ExamRecordDynamicSqlSupport.name,
                            isLikeWhenPresent(filterMap.getSQLWildcard(EXAM.ATTR_NAME)))
                    .and(
                            ExamRecordDynamicSqlSupport.description,
                            isLikeWhenPresent(filterMap.getSQLWildcard(EXAM.ATTR_DESCRIPTION)))
                    .and(
                            ExamRecordDynamicSqlSupport.creationTime,
                            SqlBuilder.isGreaterThanOrEqualToWhenPresent(fromTime))
                    .and(
                            ExamRecordDynamicSqlSupport.creationTime,
                            SqlBuilder.isLessThanOrEqualToWhenPresent(toTime))
                    .build()
                    .execute()
                    .stream()
                    .map(this::toDomainModel)
                    .collect(Collectors.toList());

            return result;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Collection<Exam>> pksByExamName(final FilterMap filterMap) {
        return Result.tryCatch(() -> {
            return this.examRecordMapper
                    .selectByExample()
                    .where(
                            ExamRecordDynamicSqlSupport.terminationTime, SqlBuilder.isNull())
                    .and(
                            ExamRecordDynamicSqlSupport.name,
                            isLikeWhenPresent(filterMap.getSQLWildcard(API.PARAM_EXAM_NAME)))
                    .build()
                    .execute()
                    .stream()
                    .map(this::toDomainModel)
                    .collect(Collectors.toList());
        });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isActive(final String modelId) {
        if (StringUtils.isBlank(modelId)) {
            return false;
        }

        return this.examRecordMapper
                .countByExample()
                .where(ExamRecordDynamicSqlSupport.id, isEqualTo(Long.valueOf(modelId)))
                .and(ExamRecordDynamicSqlSupport.terminationTime, SqlBuilder.isNull())
                .build()
                .execute() > 0;
    }

    @Override
    @Transactional
    public Result<EntityKey> setActive(final EntityKey entityKey, final boolean active) {
        return pkByUUID(entityKey.modelId)
                .map(pk -> {

                    final long now = Utils.getMillisecondsNow();

                    UpdateDSL.updateWithMapper(this.examRecordMapper::update, examRecord)
                            .set(lastUpdateTime).equalTo(now)
                            .set(terminationTime).equalTo(() -> active ? null : now)
                            .where(id, isEqualTo(pk))
                            .build()
                            .execute();

                    return entityKey;
                });
    }

    @Override
    @Transactional
    public Result<Exam> createNew(final Exam data) {
        return Result.tryCatch(() -> {

            checkUniqueName(data);

            final long millisecondsNow = Utils.getMillisecondsNow();
            final ExamRecord newRecord = new ExamRecord(
                    null,
                    (StringUtils.isNotBlank(data.uuid)) ? data.uuid : UUID.randomUUID().toString(),
                    data.name,
                    data.description,
                    data.url,
                    data.type,
                    this.userService.getCurrentUserUUIDOrNull(),
                    millisecondsNow,
                    millisecondsNow,
                    null,
                    data.startTime != null ? data.startTime : millisecondsNow,
                    data.endTime);

            this.examRecordMapper.insert(newRecord);
            return this.examRecordMapper.selectByPrimaryKey(newRecord.getId());

        })
                .map(this::toDomainModel)
                .onError(TransactionHandler::rollback);
    }

    @Override
    @Transactional
    public Result<Exam> save(final Exam data) {
        return Result.tryCatch(() -> {

            final long millisecondsNow = Utils.getMillisecondsNow();

            Long pk = data.id;
            if (pk == null && data.uuid != null) {
                pk = this.pkByUUID(data.uuid).getOr(null);
            }
            if (pk == null) {
                throw new BadRequestException("exam save", "no exam with uuid: " + data.uuid + "found");
            }

            UpdateDSL.updateWithMapper(this.examRecordMapper::update, examRecord)
                    .set(ExamRecordDynamicSqlSupport.name).equalTo(data.name)
                    .set(ExamRecordDynamicSqlSupport.description).equalTo(data.description)
                    .set(ExamRecordDynamicSqlSupport.url).equalTo(data.url)
                    .set(ExamRecordDynamicSqlSupport.type).equalTo(data.type)
                    .set(ExamRecordDynamicSqlSupport.lastUpdateTime).equalTo(millisecondsNow)
                    .where(ExamRecordDynamicSqlSupport.id, isEqualTo(pk))
                    .build()
                    .execute();

            this.entityPrivilegeDAO.savePut(EntityType.EXAM, pk, data.entityPrivileges);
            return this.examRecordMapper.selectByPrimaryKey(pk);
        })
                .map(this::toDomainModel)
                .onError(TransactionHandler::rollback);
    }

    @Override
    @Transactional
    public Result<Collection<EntityKey>> delete(final Set<EntityKey> all) {
        return Result.tryCatch(() -> {

            final List<Long> ids = extractListOfPKs(all);
            if (ids == null || ids.isEmpty()) {
                return Collections.emptyList();
            }

            // delete all involved groups first
            final Collection<EntityKey> deletedGroups = this.groupDAO
                    .deleteAllForExams(ids)
                    .getOrThrow();

            log.info("Deleted following groups: {} before deleting exams: {}", deletedGroups, all);

            // delete the exams
            this.examRecordMapper
                    .deleteByExample()
                    .where(ExamRecordDynamicSqlSupport.id, isIn(ids))
                    .build()
                    .execute();

            return ids.stream()
                    .map(pk -> new EntityKey(pk, EntityType.EXAM))
                    .collect(Collectors.toList());
        });
    }

    private Result<ExamRecord> recordByUUID(final String uuid) {
        return Result.tryCatch(() -> {

            final List<ExamRecord> execute = this.examRecordMapper.selectByExample()
                    .where(ExamRecordDynamicSqlSupport.uuid, isEqualTo(uuid))
                    .build()
                    .execute();

            if (execute == null || execute.isEmpty()) {
                throw new NoResourceFoundException(EntityType.EXAM, uuid);
            }

            return execute.get(0);
        });
    }

    private Result<Long> pkByUUID(final String examUUID) {

        return Result.tryCatch(() -> {
            final List<Long> execute = this.examRecordMapper
                    .selectIdsByExample()
                    .where(ExamRecordDynamicSqlSupport.uuid, SqlBuilder.isEqualTo(examUUID))
                    .build()
                    .execute();

            if (execute == null || execute.isEmpty()) {
                throw new NoResourceFoundException(EntityType.EXAM, examUUID);
            }

            return execute.get(0);
        });
    }

    private Result<ExamRecord> recordByPK(final Long pk) {
        return Result.tryCatch(() -> {

            final ExamRecord selectByPrimaryKey = this.examRecordMapper.selectByPrimaryKey(pk);

            if (selectByPrimaryKey == null) {
                throw new NoResourceFoundException(EntityType.EXAM, String.valueOf(pk));
            }

            return selectByPrimaryKey;
        });
    }

    private Exam toDomainModel(final ExamRecord record) {
        return new Exam(
                record.getId(),
                record.getUuid(),
                record.getName(),
                record.getDescription(),
                record.getUrl(),
                record.getType(),
                record.getOwner(),
                record.getCreationTime(),
                record.getLastUpdateTime(),
                record.getTerminationTime(),
                record.getStartTime(),
                record.getEndTime(),
                getEntityPrivileges(record.getId()));
    }

    private Collection<EntityPrivilege> getEntityPrivileges(final Long id) {
        try {

            if (id == null) {
                return Collections.emptyList();
            }

            return this.entityPrivilegeDAO
                    .getEntityPrivileges(EntityType.EXAM, id)
                    .getOrThrow();

        } catch (final Exception e) {
            log.error("Failed to get entity privileges for Exam: {}", id, e);
            return Collections.emptyList();
        }
    }

    private void checkUniqueName(final Exam exam) {

        final Long otherWithSameName = this.examRecordMapper
                .countByExample()
                .where(ExamRecordDynamicSqlSupport.name, isEqualTo(exam.name))
                .and(ExamRecordDynamicSqlSupport.id, isNotEqualToWhenPresent(exam.id))
                .build()
                .execute();

        if (otherWithSameName != null && otherWithSameName > 0) {
            throw new DuplicateEntityException(
                    EntityType.EXAM,
                    Domain.CLIENT_ACCESS.ATTR_NAME,
                    "clientaccess:name:name.notunique");
        }
    }

}