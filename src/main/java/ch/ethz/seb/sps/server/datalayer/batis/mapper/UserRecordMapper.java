package ch.ethz.seb.sps.server.datalayer.batis.mapper;

import static ch.ethz.seb.sps.server.datalayer.batis.mapper.UserRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import ch.ethz.seb.sps.server.datalayer.batis.JodaTimeTypeResolver;
import ch.ethz.seb.sps.server.datalayer.batis.model.UserRecord;
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
import org.joda.time.DateTime;
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
public interface UserRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.566+02:00", comments="Source Table: USER")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.567+02:00", comments="Source Table: USER")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.567+02:00", comments="Source Table: USER")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<UserRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.569+02:00", comments="Source Table: USER")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="ID", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="UUID", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="CREATION_DATE", javaType=DateTime.class, typeHandler=JodaTimeTypeResolver.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="NAME", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="SURNAME", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="USERNAME", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="PASSWORD", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="EMAIL", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="LANGUAGE", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="TIMEZONE", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="ACTIVE", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="ROLES", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    UserRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.570+02:00", comments="Source Table: USER")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="ID", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="UUID", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="CREATION_DATE", javaType=DateTime.class, typeHandler=JodaTimeTypeResolver.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="NAME", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="SURNAME", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="USERNAME", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="PASSWORD", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="EMAIL", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="LANGUAGE", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="TIMEZONE", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="ACTIVE", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="ROLES", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<UserRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.570+02:00", comments="Source Table: USER")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.571+02:00", comments="Source Table: USER")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(userRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.572+02:00", comments="Source Table: USER")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, userRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.572+02:00", comments="Source Table: USER")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, userRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.573+02:00", comments="Source Table: USER")
    default int insert(UserRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(userRecord)
                .map(uuid).toProperty("uuid")
                .map(creationDate).toProperty("creationDate")
                .map(name).toProperty("name")
                .map(surname).toProperty("surname")
                .map(username).toProperty("username")
                .map(password).toProperty("password")
                .map(email).toProperty("email")
                .map(language).toProperty("language")
                .map(timezone).toProperty("timezone")
                .map(active).toProperty("active")
                .map(roles).toProperty("roles")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.574+02:00", comments="Source Table: USER")
    default int insertSelective(UserRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(userRecord)
                .map(uuid).toPropertyWhenPresent("uuid", record::getUuid)
                .map(creationDate).toPropertyWhenPresent("creationDate", record::getCreationDate)
                .map(name).toPropertyWhenPresent("name", record::getName)
                .map(surname).toPropertyWhenPresent("surname", record::getSurname)
                .map(username).toPropertyWhenPresent("username", record::getUsername)
                .map(password).toPropertyWhenPresent("password", record::getPassword)
                .map(email).toPropertyWhenPresent("email", record::getEmail)
                .map(language).toPropertyWhenPresent("language", record::getLanguage)
                .map(timezone).toPropertyWhenPresent("timezone", record::getTimezone)
                .map(active).toPropertyWhenPresent("active", record::getActive)
                .map(roles).toPropertyWhenPresent("roles", record::getRoles)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.575+02:00", comments="Source Table: USER")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<UserRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, uuid, creationDate, name, surname, username, password, email, language, timezone, active, roles)
                .from(userRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.576+02:00", comments="Source Table: USER")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<UserRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, uuid, creationDate, name, surname, username, password, email, language, timezone, active, roles)
                .from(userRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.576+02:00", comments="Source Table: USER")
    default UserRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, uuid, creationDate, name, surname, username, password, email, language, timezone, active, roles)
                .from(userRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.577+02:00", comments="Source Table: USER")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(UserRecord record) {
        return UpdateDSL.updateWithMapper(this::update, userRecord)
                .set(uuid).equalTo(record::getUuid)
                .set(creationDate).equalTo(record::getCreationDate)
                .set(name).equalTo(record::getName)
                .set(surname).equalTo(record::getSurname)
                .set(username).equalTo(record::getUsername)
                .set(password).equalTo(record::getPassword)
                .set(email).equalTo(record::getEmail)
                .set(language).equalTo(record::getLanguage)
                .set(timezone).equalTo(record::getTimezone)
                .set(active).equalTo(record::getActive)
                .set(roles).equalTo(record::getRoles);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.578+02:00", comments="Source Table: USER")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(UserRecord record) {
        return UpdateDSL.updateWithMapper(this::update, userRecord)
                .set(uuid).equalToWhenPresent(record::getUuid)
                .set(creationDate).equalToWhenPresent(record::getCreationDate)
                .set(name).equalToWhenPresent(record::getName)
                .set(surname).equalToWhenPresent(record::getSurname)
                .set(username).equalToWhenPresent(record::getUsername)
                .set(password).equalToWhenPresent(record::getPassword)
                .set(email).equalToWhenPresent(record::getEmail)
                .set(language).equalToWhenPresent(record::getLanguage)
                .set(timezone).equalToWhenPresent(record::getTimezone)
                .set(active).equalToWhenPresent(record::getActive)
                .set(roles).equalToWhenPresent(record::getRoles);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.578+02:00", comments="Source Table: USER")
    default int updateByPrimaryKey(UserRecord record) {
        return UpdateDSL.updateWithMapper(this::update, userRecord)
                .set(uuid).equalTo(record::getUuid)
                .set(creationDate).equalTo(record::getCreationDate)
                .set(name).equalTo(record::getName)
                .set(surname).equalTo(record::getSurname)
                .set(username).equalTo(record::getUsername)
                .set(password).equalTo(record::getPassword)
                .set(email).equalTo(record::getEmail)
                .set(language).equalTo(record::getLanguage)
                .set(timezone).equalTo(record::getTimezone)
                .set(active).equalTo(record::getActive)
                .set(roles).equalTo(record::getRoles)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2022-09-23T10:10:02.582+02:00", comments="Source Table: USER")
    default int updateByPrimaryKeySelective(UserRecord record) {
        return UpdateDSL.updateWithMapper(this::update, userRecord)
                .set(uuid).equalToWhenPresent(record::getUuid)
                .set(creationDate).equalToWhenPresent(record::getCreationDate)
                .set(name).equalToWhenPresent(record::getName)
                .set(surname).equalToWhenPresent(record::getSurname)
                .set(username).equalToWhenPresent(record::getUsername)
                .set(password).equalToWhenPresent(record::getPassword)
                .set(email).equalToWhenPresent(record::getEmail)
                .set(language).equalToWhenPresent(record::getLanguage)
                .set(timezone).equalToWhenPresent(record::getTimezone)
                .set(active).equalToWhenPresent(record::getActive)
                .set(roles).equalToWhenPresent(record::getRoles)
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
                        .from(userRecord);
    }
}