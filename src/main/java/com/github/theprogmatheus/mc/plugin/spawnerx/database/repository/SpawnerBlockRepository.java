package com.github.theprogmatheus.mc.plugin.spawnerx.database.repository;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.SpawnerBlockEntity;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.*;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.ObjectFactory;
import com.j256.ormlite.table.TableInfo;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;

import javax.inject.Inject;
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
public class SpawnerBlockRepository implements Dao<SpawnerBlockEntity, Long> {

    private final Dao<SpawnerBlockEntity, Long> spawnerBlockEntities;

    @Inject
    public SpawnerBlockRepository(ConnectionSource connectionSource) throws SQLException {
        this.spawnerBlockEntities = DaoManager.createDao(connectionSource, SpawnerBlockEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, SpawnerBlockEntity.class);
    }

    @Override
    public SpawnerBlockEntity queryForId(Long aLong) throws SQLException {
        return spawnerBlockEntities.queryForId(aLong);
    }

    @Override
    public SpawnerBlockEntity queryForFirst(PreparedQuery<SpawnerBlockEntity> preparedQuery) throws SQLException {
        return spawnerBlockEntities.queryForFirst(preparedQuery);
    }

    @Override
    public List<SpawnerBlockEntity> queryForAll() throws SQLException {
        return spawnerBlockEntities.queryForAll();
    }

    @Override
    public SpawnerBlockEntity queryForFirst() throws SQLException {
        return spawnerBlockEntities.queryForFirst();
    }

    @Override
    public List<SpawnerBlockEntity> queryForEq(String s, Object o) throws SQLException {
        return spawnerBlockEntities.queryForEq(s, o);
    }

    @Override
    public List<SpawnerBlockEntity> queryForMatching(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.queryForMatching(spawnerBlockEntity);
    }

    @Override
    public List<SpawnerBlockEntity> queryForMatchingArgs(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.queryForMatchingArgs(spawnerBlockEntity);
    }

    @Override
    public List<SpawnerBlockEntity> queryForFieldValues(Map<String, Object> map) throws SQLException {
        return spawnerBlockEntities.queryForFieldValues(map);
    }

    @Override
    public List<SpawnerBlockEntity> queryForFieldValuesArgs(Map<String, Object> map) throws SQLException {
        return spawnerBlockEntities.queryForFieldValuesArgs(map);
    }

    @Override
    public SpawnerBlockEntity queryForSameId(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.queryForSameId(spawnerBlockEntity);
    }

    @Override
    public QueryBuilder<SpawnerBlockEntity, Long> queryBuilder() {
        return spawnerBlockEntities.queryBuilder();
    }

    @Override
    public UpdateBuilder<SpawnerBlockEntity, Long> updateBuilder() {
        return spawnerBlockEntities.updateBuilder();
    }

    @Override
    public DeleteBuilder<SpawnerBlockEntity, Long> deleteBuilder() {
        return spawnerBlockEntities.deleteBuilder();
    }

    @Override
    public List<SpawnerBlockEntity> query(PreparedQuery<SpawnerBlockEntity> preparedQuery) throws SQLException {
        return spawnerBlockEntities.query(preparedQuery);
    }

    @Override
    public int create(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.create(spawnerBlockEntity);
    }

    @Override
    public int create(Collection<SpawnerBlockEntity> collection) throws SQLException {
        return spawnerBlockEntities.create(collection);
    }

    @Override
    public SpawnerBlockEntity createIfNotExists(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.createIfNotExists(spawnerBlockEntity);
    }

    @Override
    public CreateOrUpdateStatus createOrUpdate(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.createOrUpdate(spawnerBlockEntity);
    }

    @Override
    public int update(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.update(spawnerBlockEntity);
    }

    @Override
    public int updateId(SpawnerBlockEntity spawnerBlockEntity, Long aLong) throws SQLException {
        return spawnerBlockEntities.updateId(spawnerBlockEntity, aLong);
    }

    @Override
    public int update(PreparedUpdate<SpawnerBlockEntity> preparedUpdate) throws SQLException {
        return spawnerBlockEntities.update(preparedUpdate);
    }

    @Override
    public int refresh(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.refresh(spawnerBlockEntity);
    }

    @Override
    public int delete(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.delete(spawnerBlockEntity);
    }

    @Override
    public int deleteById(Long aLong) throws SQLException {
        return spawnerBlockEntities.deleteById(aLong);
    }

    @Override
    public int delete(Collection<SpawnerBlockEntity> collection) throws SQLException {
        return spawnerBlockEntities.delete(collection);
    }

    @Override
    public int deleteIds(Collection<Long> collection) throws SQLException {
        return spawnerBlockEntities.deleteIds(collection);
    }

    @Override
    public int delete(PreparedDelete<SpawnerBlockEntity> preparedDelete) throws SQLException {
        return spawnerBlockEntities.delete(preparedDelete);
    }

    @Override
    public CloseableIterator<SpawnerBlockEntity> iterator() {
        return spawnerBlockEntities.iterator();
    }

    @Override
    public CloseableIterator<SpawnerBlockEntity> iterator(int i) {
        return spawnerBlockEntities.iterator(i);
    }

    @Override
    public CloseableIterator<SpawnerBlockEntity> iterator(PreparedQuery<SpawnerBlockEntity> preparedQuery) throws SQLException {
        return spawnerBlockEntities.iterator(preparedQuery);
    }

    @Override
    public CloseableIterator<SpawnerBlockEntity> iterator(PreparedQuery<SpawnerBlockEntity> preparedQuery, int i) throws SQLException {
        return spawnerBlockEntities.iterator(preparedQuery, i);
    }

    @Override
    public CloseableWrappedIterable<SpawnerBlockEntity> getWrappedIterable() {
        return spawnerBlockEntities.getWrappedIterable();
    }

    @Override
    public CloseableWrappedIterable<SpawnerBlockEntity> getWrappedIterable(PreparedQuery<SpawnerBlockEntity> preparedQuery) {
        return spawnerBlockEntities.getWrappedIterable(preparedQuery);
    }

    @Override
    public void closeLastIterator() throws Exception {
        spawnerBlockEntities.closeLastIterator();
    }

    @Override
    public GenericRawResults<String[]> queryRaw(String s, String... strings) throws SQLException {
        return spawnerBlockEntities.queryRaw(s, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, RawRowMapper<UO> rawRowMapper, String... strings) throws SQLException {
        return spawnerBlockEntities.queryRaw(s, rawRowMapper, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, DataType[] dataTypes, RawRowObjectMapper<UO> rawRowObjectMapper, String... strings) throws SQLException {
        return spawnerBlockEntities.queryRaw(s, dataTypes, rawRowObjectMapper, strings);
    }

    @Override
    public GenericRawResults<Object[]> queryRaw(String s, DataType[] dataTypes, String... strings) throws SQLException {
        return spawnerBlockEntities.queryRaw(s, dataTypes, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, DatabaseResultsMapper<UO> databaseResultsMapper, String... strings) throws SQLException {
        return spawnerBlockEntities.queryRaw(s, databaseResultsMapper, strings);
    }

    @Override
    public long queryRawValue(String s, String... strings) throws SQLException {
        return spawnerBlockEntities.queryRawValue(s, strings);
    }

    @Override
    public int executeRaw(String s, String... strings) throws SQLException {
        return spawnerBlockEntities.executeRaw(s, strings);
    }

    @Override
    public int executeRawNoArgs(String s) throws SQLException {
        return spawnerBlockEntities.executeRawNoArgs(s);
    }

    @Override
    public int updateRaw(String s, String... strings) throws SQLException {
        return spawnerBlockEntities.updateRaw(s, strings);
    }

    @Override
    public <CT> CT callBatchTasks(Callable<CT> callable) throws Exception {
        return spawnerBlockEntities.callBatchTasks(callable);
    }

    @Override
    public String objectToString(SpawnerBlockEntity spawnerBlockEntity) {
        return spawnerBlockEntities.objectToString(spawnerBlockEntity);
    }

    @Override
    public boolean objectsEqual(SpawnerBlockEntity spawnerBlockEntity, SpawnerBlockEntity t1) throws SQLException {
        return spawnerBlockEntities.objectsEqual(spawnerBlockEntity, t1);
    }

    @Override
    public Long extractId(SpawnerBlockEntity spawnerBlockEntity) throws SQLException {
        return spawnerBlockEntities.extractId(spawnerBlockEntity);
    }

    @Override
    public Class<SpawnerBlockEntity> getDataClass() {
        return spawnerBlockEntities.getDataClass();
    }

    @Override
    public FieldType findForeignFieldType(Class<?> aClass) {
        return spawnerBlockEntities.findForeignFieldType(aClass);
    }

    @Override
    public boolean isUpdatable() {
        return spawnerBlockEntities.isUpdatable();
    }

    @Override
    public boolean isTableExists() throws SQLException {
        return spawnerBlockEntities.isTableExists();
    }

    @Override
    public long countOf() throws SQLException {
        return spawnerBlockEntities.countOf();
    }

    @Override
    public long countOf(PreparedQuery<SpawnerBlockEntity> preparedQuery) throws SQLException {
        return spawnerBlockEntities.countOf(preparedQuery);
    }

    @Override
    public void assignEmptyForeignCollection(SpawnerBlockEntity spawnerBlockEntity, String s) throws SQLException {
        spawnerBlockEntities.assignEmptyForeignCollection(spawnerBlockEntity, s);
    }

    @Override
    public <FT> ForeignCollection<FT> getEmptyForeignCollection(String s) throws SQLException {
        return spawnerBlockEntities.getEmptyForeignCollection(s);
    }

    @Override
    public void setObjectCache(boolean b) throws SQLException {
        spawnerBlockEntities.setObjectCache(b);
    }

    @Override
    public void setObjectCache(ObjectCache objectCache) throws SQLException {
        spawnerBlockEntities.setObjectCache(objectCache);
    }

    @Override
    public ObjectCache getObjectCache() {
        return spawnerBlockEntities.getObjectCache();
    }

    @Override
    public void clearObjectCache() {
        spawnerBlockEntities.clearObjectCache();
    }

    @Override
    public SpawnerBlockEntity mapSelectStarRow(DatabaseResults databaseResults) throws SQLException {
        return spawnerBlockEntities.mapSelectStarRow(databaseResults);
    }

    @Override
    public GenericRowMapper<SpawnerBlockEntity> getSelectStarRowMapper() throws SQLException {
        return spawnerBlockEntities.getSelectStarRowMapper();
    }

    @Override
    public RawRowMapper<SpawnerBlockEntity> getRawRowMapper() {
        return spawnerBlockEntities.getRawRowMapper();
    }

    @Override
    public boolean idExists(Long aLong) throws SQLException {
        return spawnerBlockEntities.idExists(aLong);
    }

    @Override
    public DatabaseConnection startThreadConnection() throws SQLException {
        return spawnerBlockEntities.startThreadConnection();
    }

    @Override
    public void endThreadConnection(DatabaseConnection databaseConnection) throws SQLException {
        spawnerBlockEntities.endThreadConnection(databaseConnection);
    }

    @Override
    public void setAutoCommit(DatabaseConnection databaseConnection, boolean b) throws SQLException {
        spawnerBlockEntities.setAutoCommit(databaseConnection, b);
    }

    @Override
    public boolean isAutoCommit(DatabaseConnection databaseConnection) throws SQLException {
        return spawnerBlockEntities.isAutoCommit(databaseConnection);
    }

    @Override
    public void commit(DatabaseConnection databaseConnection) throws SQLException {
        spawnerBlockEntities.commit(databaseConnection);
    }

    @Override
    public void rollBack(DatabaseConnection databaseConnection) throws SQLException {
        spawnerBlockEntities.rollBack(databaseConnection);
    }

    @Override
    public ConnectionSource getConnectionSource() {
        return spawnerBlockEntities.getConnectionSource();
    }

    @Override
    public void setObjectFactory(ObjectFactory<SpawnerBlockEntity> objectFactory) {
        spawnerBlockEntities.setObjectFactory(objectFactory);
    }

    @Override
    public void registerObserver(DaoObserver daoObserver) {
        spawnerBlockEntities.registerObserver(daoObserver);
    }

    @Override
    public void unregisterObserver(DaoObserver daoObserver) {
        spawnerBlockEntities.unregisterObserver(daoObserver);
    }

    @Override
    public String getTableName() {
        return spawnerBlockEntities.getTableName();
    }

    @Override
    public void notifyChanges() {
        spawnerBlockEntities.notifyChanges();
    }

    @Override
    public SpawnerBlockEntity createObjectInstance() throws SQLException {
        return spawnerBlockEntities.createObjectInstance();
    }

    @Override
    public TableInfo<SpawnerBlockEntity, Long> getTableInfo() {
        return spawnerBlockEntities.getTableInfo();
    }

    @Override
    public CloseableIterator<SpawnerBlockEntity> closeableIterator() {
        return spawnerBlockEntities.closeableIterator();
    }

    @Override
    public void forEach(Consumer<? super SpawnerBlockEntity> action) {
        spawnerBlockEntities.forEach(action);
    }

    @Override
    public Spliterator<SpawnerBlockEntity> spliterator() {
        return spawnerBlockEntities.spliterator();
    }
}
