package com.github.theprogmatheus.mc.plugin.spawnerx.database.repository;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobEntity;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.*;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.ObjectFactory;
import com.j256.ormlite.table.TableInfo;
import lombok.Getter;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

@Getter
@Singleton
public class MobEntityRepository implements Dao<MobEntity, Long> {

    private final Dao<MobEntity, Long> mobEntities;

    public MobEntityRepository(ConnectionSource connectionSource) throws SQLException {
        this.mobEntities = DaoManager.createDao(connectionSource, MobEntity.class);
    }

    @Override
    public MobEntity queryForId(Long aLong) throws SQLException {
        return mobEntities.queryForId(aLong);
    }

    @Override
    public MobEntity queryForFirst(PreparedQuery<MobEntity> preparedQuery) throws SQLException {
        return mobEntities.queryForFirst(preparedQuery);
    }

    @Override
    public List<MobEntity> queryForAll() throws SQLException {
        return mobEntities.queryForAll();
    }

    @Override
    public MobEntity queryForFirst() throws SQLException {
        return mobEntities.queryForFirst();
    }

    @Override
    public List<MobEntity> queryForEq(String s, Object o) throws SQLException {
        return mobEntities.queryForEq(s, o);
    }

    @Override
    public List<MobEntity> queryForMatching(MobEntity mobEntity) throws SQLException {
        return mobEntities.queryForMatching(mobEntity);
    }

    @Override
    public List<MobEntity> queryForMatchingArgs(MobEntity mobEntity) throws SQLException {
        return mobEntities.queryForMatchingArgs(mobEntity);
    }

    @Override
    public List<MobEntity> queryForFieldValues(Map<String, Object> map) throws SQLException {
        return mobEntities.queryForFieldValues(map);
    }

    @Override
    public List<MobEntity> queryForFieldValuesArgs(Map<String, Object> map) throws SQLException {
        return mobEntities.queryForFieldValuesArgs(map);
    }

    @Override
    public MobEntity queryForSameId(MobEntity mobEntity) throws SQLException {
        return mobEntities.queryForSameId(mobEntity);
    }

    @Override
    public QueryBuilder<MobEntity, Long> queryBuilder() {
        return mobEntities.queryBuilder();
    }

    @Override
    public UpdateBuilder<MobEntity, Long> updateBuilder() {
        return mobEntities.updateBuilder();
    }

    @Override
    public DeleteBuilder<MobEntity, Long> deleteBuilder() {
        return mobEntities.deleteBuilder();
    }

    @Override
    public List<MobEntity> query(PreparedQuery<MobEntity> preparedQuery) throws SQLException {
        return mobEntities.query(preparedQuery);
    }

    @Override
    public int create(MobEntity mobEntity) throws SQLException {
        return mobEntities.create(mobEntity);
    }

    @Override
    public int create(Collection<MobEntity> collection) throws SQLException {
        return mobEntities.create(collection);
    }

    @Override
    public MobEntity createIfNotExists(MobEntity mobEntity) throws SQLException {
        return mobEntities.createIfNotExists(mobEntity);
    }

    @Override
    public CreateOrUpdateStatus createOrUpdate(MobEntity mobEntity) throws SQLException {
        return mobEntities.createOrUpdate(mobEntity);
    }

    @Override
    public int update(MobEntity mobEntity) throws SQLException {
        return mobEntities.update(mobEntity);
    }

    @Override
    public int updateId(MobEntity mobEntity, Long aLong) throws SQLException {
        return mobEntities.updateId(mobEntity, aLong);
    }

    @Override
    public int update(PreparedUpdate<MobEntity> preparedUpdate) throws SQLException {
        return mobEntities.update(preparedUpdate);
    }

    @Override
    public int refresh(MobEntity mobEntity) throws SQLException {
        return mobEntities.refresh(mobEntity);
    }

    @Override
    public int delete(MobEntity mobEntity) throws SQLException {
        return mobEntities.delete(mobEntity);
    }

    @Override
    public int deleteById(Long aLong) throws SQLException {
        return mobEntities.deleteById(aLong);
    }

    @Override
    public int delete(Collection<MobEntity> collection) throws SQLException {
        return mobEntities.delete(collection);
    }

    @Override
    public int deleteIds(Collection<Long> collection) throws SQLException {
        return mobEntities.deleteIds(collection);
    }

    @Override
    public int delete(PreparedDelete<MobEntity> preparedDelete) throws SQLException {
        return mobEntities.delete(preparedDelete);
    }

    @Override
    public CloseableIterator<MobEntity> iterator() {
        return mobEntities.iterator();
    }

    @Override
    public CloseableIterator<MobEntity> iterator(int i) {
        return mobEntities.iterator(i);
    }

    @Override
    public CloseableIterator<MobEntity> iterator(PreparedQuery<MobEntity> preparedQuery) throws SQLException {
        return mobEntities.iterator(preparedQuery);
    }

    @Override
    public CloseableIterator<MobEntity> iterator(PreparedQuery<MobEntity> preparedQuery, int i) throws SQLException {
        return mobEntities.iterator(preparedQuery, i);
    }

    @Override
    public CloseableWrappedIterable<MobEntity> getWrappedIterable() {
        return mobEntities.getWrappedIterable();
    }

    @Override
    public CloseableWrappedIterable<MobEntity> getWrappedIterable(PreparedQuery<MobEntity> preparedQuery) {
        return mobEntities.getWrappedIterable(preparedQuery);
    }

    @Override
    public void closeLastIterator() throws Exception {
        mobEntities.closeLastIterator();
    }

    @Override
    public GenericRawResults<String[]> queryRaw(String s, String... strings) throws SQLException {
        return mobEntities.queryRaw(s, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, RawRowMapper<UO> rawRowMapper, String... strings) throws SQLException {
        return mobEntities.queryRaw(s, rawRowMapper, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, DataType[] dataTypes, RawRowObjectMapper<UO> rawRowObjectMapper, String... strings) throws SQLException {
        return mobEntities.queryRaw(s, dataTypes, rawRowObjectMapper, strings);
    }

    @Override
    public GenericRawResults<Object[]> queryRaw(String s, DataType[] dataTypes, String... strings) throws SQLException {
        return mobEntities.queryRaw(s, dataTypes, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, DatabaseResultsMapper<UO> databaseResultsMapper, String... strings) throws SQLException {
        return mobEntities.queryRaw(s, databaseResultsMapper, strings);
    }

    @Override
    public long queryRawValue(String s, String... strings) throws SQLException {
        return mobEntities.queryRawValue(s, strings);
    }

    @Override
    public int executeRaw(String s, String... strings) throws SQLException {
        return mobEntities.executeRaw(s, strings);
    }

    @Override
    public int executeRawNoArgs(String s) throws SQLException {
        return mobEntities.executeRawNoArgs(s);
    }

    @Override
    public int updateRaw(String s, String... strings) throws SQLException {
        return mobEntities.updateRaw(s, strings);
    }

    @Override
    public <CT> CT callBatchTasks(Callable<CT> callable) throws Exception {
        return mobEntities.callBatchTasks(callable);
    }

    @Override
    public String objectToString(MobEntity mobEntity) {
        return mobEntities.objectToString(mobEntity);
    }

    @Override
    public boolean objectsEqual(MobEntity mobEntity, MobEntity t1) throws SQLException {
        return mobEntities.objectsEqual(mobEntity, t1);
    }

    @Override
    public Long extractId(MobEntity mobEntity) throws SQLException {
        return mobEntities.extractId(mobEntity);
    }

    @Override
    public Class<MobEntity> getDataClass() {
        return mobEntities.getDataClass();
    }

    @Override
    public FieldType findForeignFieldType(Class<?> aClass) {
        return mobEntities.findForeignFieldType(aClass);
    }

    @Override
    public boolean isUpdatable() {
        return mobEntities.isUpdatable();
    }

    @Override
    public boolean isTableExists() throws SQLException {
        return mobEntities.isTableExists();
    }

    @Override
    public long countOf() throws SQLException {
        return mobEntities.countOf();
    }

    @Override
    public long countOf(PreparedQuery<MobEntity> preparedQuery) throws SQLException {
        return mobEntities.countOf(preparedQuery);
    }

    @Override
    public void assignEmptyForeignCollection(MobEntity mobEntity, String s) throws SQLException {
        mobEntities.assignEmptyForeignCollection(mobEntity, s);
    }

    @Override
    public <FT> ForeignCollection<FT> getEmptyForeignCollection(String s) throws SQLException {
        return mobEntities.getEmptyForeignCollection(s);
    }

    @Override
    public void setObjectCache(boolean b) throws SQLException {
        mobEntities.setObjectCache(b);
    }

    @Override
    public void setObjectCache(ObjectCache objectCache) throws SQLException {
        mobEntities.setObjectCache(objectCache);
    }

    @Override
    public ObjectCache getObjectCache() {
        return mobEntities.getObjectCache();
    }

    @Override
    public void clearObjectCache() {
        mobEntities.clearObjectCache();
    }

    @Override
    public MobEntity mapSelectStarRow(DatabaseResults databaseResults) throws SQLException {
        return mobEntities.mapSelectStarRow(databaseResults);
    }

    @Override
    public GenericRowMapper<MobEntity> getSelectStarRowMapper() throws SQLException {
        return mobEntities.getSelectStarRowMapper();
    }

    @Override
    public RawRowMapper<MobEntity> getRawRowMapper() {
        return mobEntities.getRawRowMapper();
    }

    @Override
    public boolean idExists(Long aLong) throws SQLException {
        return mobEntities.idExists(aLong);
    }

    @Override
    public DatabaseConnection startThreadConnection() throws SQLException {
        return mobEntities.startThreadConnection();
    }

    @Override
    public void endThreadConnection(DatabaseConnection databaseConnection) throws SQLException {
        mobEntities.endThreadConnection(databaseConnection);
    }

    @Override
    public void setAutoCommit(DatabaseConnection databaseConnection, boolean b) throws SQLException {
        mobEntities.setAutoCommit(databaseConnection, b);
    }

    @Override
    public boolean isAutoCommit(DatabaseConnection databaseConnection) throws SQLException {
        return mobEntities.isAutoCommit(databaseConnection);
    }

    @Override
    public void commit(DatabaseConnection databaseConnection) throws SQLException {
        mobEntities.commit(databaseConnection);
    }

    @Override
    public void rollBack(DatabaseConnection databaseConnection) throws SQLException {
        mobEntities.rollBack(databaseConnection);
    }

    @Override
    public ConnectionSource getConnectionSource() {
        return mobEntities.getConnectionSource();
    }

    @Override
    public void setObjectFactory(ObjectFactory<MobEntity> objectFactory) {
        mobEntities.setObjectFactory(objectFactory);
    }

    @Override
    public void registerObserver(DaoObserver daoObserver) {
        mobEntities.registerObserver(daoObserver);
    }

    @Override
    public void unregisterObserver(DaoObserver daoObserver) {
        mobEntities.unregisterObserver(daoObserver);
    }

    @Override
    public String getTableName() {
        return mobEntities.getTableName();
    }

    @Override
    public void notifyChanges() {
        mobEntities.notifyChanges();
    }

    @Override
    public MobEntity createObjectInstance() throws SQLException {
        return mobEntities.createObjectInstance();
    }

    @Override
    public TableInfo<MobEntity, Long> getTableInfo() {
        return mobEntities.getTableInfo();
    }

    @Override
    public CloseableIterator<MobEntity> closeableIterator() {
        return mobEntities.closeableIterator();
    }

    @Override
    public void forEach(Consumer<? super MobEntity> action) {
        mobEntities.forEach(action);
    }

    @Override
    public Spliterator<MobEntity> spliterator() {
        return mobEntities.spliterator();
    }
}
