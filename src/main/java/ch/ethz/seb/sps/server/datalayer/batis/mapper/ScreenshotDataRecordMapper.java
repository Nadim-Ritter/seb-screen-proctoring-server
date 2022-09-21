package ch.ethz.seb.sps.server.datalayer.batis.mapper;

import static ch.ethz.seb.sps.server.datalayer.batis.mapper.ScreenshotDataRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import ch.ethz.seb.sps.server.datalayer.batis.model.ScreenshotDataRecord;
import java.util.List;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.MyBatis3DeleteModelAdapter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.MyBatis3UpdateModelAdapter;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface ScreenshotDataRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.760+02:00", comments="Source Table: SCREENSHOT_DATA")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ScreenshotDataRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="ID", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="SCREENSHOT_ID", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="SESSION_ID", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="GROUPID", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="TIMESTAMP", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="IMAGE_URL", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="IMAGE_FORMAT", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="META_DATA", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    ScreenshotDataRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="ID", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="SCREENSHOT_ID", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="SESSION_ID", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="GROUPID", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="TIMESTAMP", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="IMAGE_URL", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="IMAGE_FORMAT", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="META_DATA", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<ScreenshotDataRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(screenshotDataRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, screenshotDataRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, screenshotDataRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    default int insert(ScreenshotDataRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(screenshotDataRecord)
                .map(screenshotId).toProperty("screenshotId")
                .map(sessionId).toProperty("sessionId")
                .map(groupid).toProperty("groupid")
                .map(timestamp).toProperty("timestamp")
                .map(imageUrl).toProperty("imageUrl")
                .map(imageFormat).toProperty("imageFormat")
                .map(metaData).toProperty("metaData")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    default int insertSelective(ScreenshotDataRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(screenshotDataRecord)
                .map(screenshotId).toPropertyWhenPresent("screenshotId", record::getScreenshotId)
                .map(sessionId).toPropertyWhenPresent("sessionId", record::getSessionId)
                .map(groupid).toPropertyWhenPresent("groupid", record::getGroupid)
                .map(timestamp).toPropertyWhenPresent("timestamp", record::getTimestamp)
                .map(imageUrl).toPropertyWhenPresent("imageUrl", record::getImageUrl)
                .map(imageFormat).toPropertyWhenPresent("imageFormat", record::getImageFormat)
                .map(metaData).toPropertyWhenPresent("metaData", record::getMetaData)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ScreenshotDataRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, screenshotId, sessionId, groupid, timestamp, imageUrl, imageFormat, metaData)
                .from(screenshotDataRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.761+02:00", comments="Source Table: SCREENSHOT_DATA")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ScreenshotDataRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, screenshotId, sessionId, groupid, timestamp, imageUrl, imageFormat, metaData)
                .from(screenshotDataRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.762+02:00", comments="Source Table: SCREENSHOT_DATA")
    default ScreenshotDataRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, screenshotId, sessionId, groupid, timestamp, imageUrl, imageFormat, metaData)
                .from(screenshotDataRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.762+02:00", comments="Source Table: SCREENSHOT_DATA")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(ScreenshotDataRecord record) {
        return UpdateDSL.updateWithMapper(this::update, screenshotDataRecord)
                .set(screenshotId).equalTo(record::getScreenshotId)
                .set(sessionId).equalTo(record::getSessionId)
                .set(groupid).equalTo(record::getGroupid)
                .set(timestamp).equalTo(record::getTimestamp)
                .set(imageUrl).equalTo(record::getImageUrl)
                .set(imageFormat).equalTo(record::getImageFormat)
                .set(metaData).equalTo(record::getMetaData);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.762+02:00", comments="Source Table: SCREENSHOT_DATA")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(ScreenshotDataRecord record) {
        return UpdateDSL.updateWithMapper(this::update, screenshotDataRecord)
                .set(screenshotId).equalToWhenPresent(record::getScreenshotId)
                .set(sessionId).equalToWhenPresent(record::getSessionId)
                .set(groupid).equalToWhenPresent(record::getGroupid)
                .set(timestamp).equalToWhenPresent(record::getTimestamp)
                .set(imageUrl).equalToWhenPresent(record::getImageUrl)
                .set(imageFormat).equalToWhenPresent(record::getImageFormat)
                .set(metaData).equalToWhenPresent(record::getMetaData);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.762+02:00", comments="Source Table: SCREENSHOT_DATA")
    default int updateByPrimaryKey(ScreenshotDataRecord record) {
        return UpdateDSL.updateWithMapper(this::update, screenshotDataRecord)
                .set(screenshotId).equalTo(record::getScreenshotId)
                .set(sessionId).equalTo(record::getSessionId)
                .set(groupid).equalTo(record::getGroupid)
                .set(timestamp).equalTo(record::getTimestamp)
                .set(imageUrl).equalTo(record::getImageUrl)
                .set(imageFormat).equalTo(record::getImageFormat)
                .set(metaData).equalTo(record::getMetaData)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-21T14:52:03.762+02:00", comments="Source Table: SCREENSHOT_DATA")
    default int updateByPrimaryKeySelective(ScreenshotDataRecord record) {
        return UpdateDSL.updateWithMapper(this::update, screenshotDataRecord)
                .set(screenshotId).equalToWhenPresent(record::getScreenshotId)
                .set(sessionId).equalToWhenPresent(record::getSessionId)
                .set(groupid).equalToWhenPresent(record::getGroupid)
                .set(timestamp).equalToWhenPresent(record::getTimestamp)
                .set(imageUrl).equalToWhenPresent(record::getImageUrl)
                .set(imageFormat).equalToWhenPresent(record::getImageFormat)
                .set(metaData).equalToWhenPresent(record::getMetaData)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator",comments="Source Table: exam")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({@Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true)})
    List<Long> selectIds(SelectStatementProvider select);

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<Long>>> selectIdsByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectIds, id)
                        .from(screenshotDataRecord);
    }
}