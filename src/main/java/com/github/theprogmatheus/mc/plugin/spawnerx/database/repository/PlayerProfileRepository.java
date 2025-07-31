package com.github.theprogmatheus.mc.plugin.spawnerx.database.repository;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.PlayerProfileEntity;
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

@Singleton
@Getter
public class PlayerProfileRepository implements Dao<PlayerProfileEntity, Long> {

    private final Dao<PlayerProfileEntity, Long> playerProfiles;

    @Inject
    public PlayerProfileRepository(ConnectionSource connectionSource) throws SQLException {
        this.playerProfiles = DaoManager.createDao(connectionSource, PlayerProfileEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, PlayerProfileEntity.class);
    }

    @Override
    public PlayerProfileEntity queryForId(Long aLong) throws SQLException {
        return playerProfiles.queryForId(aLong);
    }

    @Override
    public PlayerProfileEntity queryForFirst(PreparedQuery<PlayerProfileEntity> preparedQuery) throws SQLException {
        return playerProfiles.queryForFirst(preparedQuery);
    }

    @Override
    public List<PlayerProfileEntity> queryForAll() throws SQLException {
        return playerProfiles.queryForAll();
    }

    @Override
    public PlayerProfileEntity queryForFirst() throws SQLException {
        return playerProfiles.queryForFirst();
    }

    @Override
    public List<PlayerProfileEntity> queryForEq(String s, Object o) throws SQLException {
        return playerProfiles.queryForEq(s, o);
    }

    @Override
    public List<PlayerProfileEntity> queryForMatching(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.queryForMatching(playerProfileEntity);
    }

    @Override
    public List<PlayerProfileEntity> queryForMatchingArgs(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.queryForMatchingArgs(playerProfileEntity);
    }

    @Override
    public List<PlayerProfileEntity> queryForFieldValues(Map<String, Object> map) throws SQLException {
        return playerProfiles.queryForFieldValues(map);
    }

    @Override
    public List<PlayerProfileEntity> queryForFieldValuesArgs(Map<String, Object> map) throws SQLException {
        return playerProfiles.queryForFieldValuesArgs(map);
    }

    @Override
    public PlayerProfileEntity queryForSameId(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.queryForSameId(playerProfileEntity);
    }

    @Override
    public QueryBuilder<PlayerProfileEntity, Long> queryBuilder() {
        return playerProfiles.queryBuilder();
    }

    @Override
    public UpdateBuilder<PlayerProfileEntity, Long> updateBuilder() {
        return playerProfiles.updateBuilder();
    }

    @Override
    public DeleteBuilder<PlayerProfileEntity, Long> deleteBuilder() {
        return playerProfiles.deleteBuilder();
    }

    @Override
    public List<PlayerProfileEntity> query(PreparedQuery<PlayerProfileEntity> preparedQuery) throws SQLException {
        return playerProfiles.query(preparedQuery);
    }

    @Override
    public int create(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.create(playerProfileEntity);
    }

    @Override
    public int create(Collection<PlayerProfileEntity> collection) throws SQLException {
        return playerProfiles.create(collection);
    }

    @Override
    public PlayerProfileEntity createIfNotExists(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.createIfNotExists(playerProfileEntity);
    }

    @Override
    public CreateOrUpdateStatus createOrUpdate(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.createOrUpdate(playerProfileEntity);
    }

    @Override
    public int update(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.update(playerProfileEntity);
    }

    @Override
    public int updateId(PlayerProfileEntity playerProfileEntity, Long aLong) throws SQLException {
        return playerProfiles.updateId(playerProfileEntity, aLong);
    }

    @Override
    public int update(PreparedUpdate<PlayerProfileEntity> preparedUpdate) throws SQLException {
        return playerProfiles.update(preparedUpdate);
    }

    @Override
    public int refresh(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.refresh(playerProfileEntity);
    }

    @Override
    public int delete(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.delete(playerProfileEntity);
    }

    @Override
    public int deleteById(Long aLong) throws SQLException {
        return playerProfiles.deleteById(aLong);
    }

    @Override
    public int delete(Collection<PlayerProfileEntity> collection) throws SQLException {
        return playerProfiles.delete(collection);
    }

    @Override
    public int deleteIds(Collection<Long> collection) throws SQLException {
        return playerProfiles.deleteIds(collection);
    }

    @Override
    public int delete(PreparedDelete<PlayerProfileEntity> preparedDelete) throws SQLException {
        return playerProfiles.delete(preparedDelete);
    }

    @Override
    public CloseableIterator<PlayerProfileEntity> iterator() {
        return playerProfiles.iterator();
    }

    @Override
    public CloseableIterator<PlayerProfileEntity> iterator(int i) {
        return playerProfiles.iterator(i);
    }

    @Override
    public CloseableIterator<PlayerProfileEntity> iterator(PreparedQuery<PlayerProfileEntity> preparedQuery) throws SQLException {
        return playerProfiles.iterator(preparedQuery);
    }

    @Override
    public CloseableIterator<PlayerProfileEntity> iterator(PreparedQuery<PlayerProfileEntity> preparedQuery, int i) throws SQLException {
        return playerProfiles.iterator(preparedQuery, i);
    }

    @Override
    public CloseableWrappedIterable<PlayerProfileEntity> getWrappedIterable() {
        return playerProfiles.getWrappedIterable();
    }

    @Override
    public CloseableWrappedIterable<PlayerProfileEntity> getWrappedIterable(PreparedQuery<PlayerProfileEntity> preparedQuery) {
        return playerProfiles.getWrappedIterable(preparedQuery);
    }

    @Override
    public void closeLastIterator() throws Exception {
        playerProfiles.closeLastIterator();
    }

    @Override
    public GenericRawResults<String[]> queryRaw(String s, String... strings) throws SQLException {
        return playerProfiles.queryRaw(s, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, RawRowMapper<UO> rawRowMapper, String... strings) throws SQLException {
        return playerProfiles.queryRaw(s, rawRowMapper, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, DataType[] dataTypes, RawRowObjectMapper<UO> rawRowObjectMapper, String... strings) throws SQLException {
        return playerProfiles.queryRaw(s, dataTypes, rawRowObjectMapper, strings);
    }

    @Override
    public GenericRawResults<Object[]> queryRaw(String s, DataType[] dataTypes, String... strings) throws SQLException {
        return playerProfiles.queryRaw(s, dataTypes, strings);
    }

    @Override
    public <UO> GenericRawResults<UO> queryRaw(String s, DatabaseResultsMapper<UO> databaseResultsMapper, String... strings) throws SQLException {
        return playerProfiles.queryRaw(s, databaseResultsMapper, strings);
    }

    @Override
    public long queryRawValue(String s, String... strings) throws SQLException {
        return playerProfiles.queryRawValue(s, strings);
    }

    @Override
    public int executeRaw(String s, String... strings) throws SQLException {
        return playerProfiles.executeRaw(s, strings);
    }

    @Override
    public int executeRawNoArgs(String s) throws SQLException {
        return playerProfiles.executeRawNoArgs(s);
    }

    @Override
    public int updateRaw(String s, String... strings) throws SQLException {
        return playerProfiles.updateRaw(s, strings);
    }

    @Override
    public <CT> CT callBatchTasks(Callable<CT> callable) throws Exception {
        return playerProfiles.callBatchTasks(callable);
    }

    @Override
    public String objectToString(PlayerProfileEntity playerProfileEntity) {
        return playerProfiles.objectToString(playerProfileEntity);
    }

    @Override
    public boolean objectsEqual(PlayerProfileEntity playerProfileEntity, PlayerProfileEntity t1) throws SQLException {
        return playerProfiles.objectsEqual(playerProfileEntity, t1);
    }

    @Override
    public Long extractId(PlayerProfileEntity playerProfileEntity) throws SQLException {
        return playerProfiles.extractId(playerProfileEntity);
    }

    @Override
    public Class<PlayerProfileEntity> getDataClass() {
        return playerProfiles.getDataClass();
    }

    @Override
    public FieldType findForeignFieldType(Class<?> aClass) {
        return playerProfiles.findForeignFieldType(aClass);
    }

    @Override
    public boolean isUpdatable() {
        return playerProfiles.isUpdatable();
    }

    @Override
    public boolean isTableExists() throws SQLException {
        return playerProfiles.isTableExists();
    }

    @Override
    public long countOf() throws SQLException {
        return playerProfiles.countOf();
    }

    @Override
    public long countOf(PreparedQuery<PlayerProfileEntity> preparedQuery) throws SQLException {
        return playerProfiles.countOf(preparedQuery);
    }

    @Override
    public void assignEmptyForeignCollection(PlayerProfileEntity playerProfileEntity, String s) throws SQLException {
        playerProfiles.assignEmptyForeignCollection(playerProfileEntity, s);
    }

    @Override
    public <FT> ForeignCollection<FT> getEmptyForeignCollection(String s) throws SQLException {
        return playerProfiles.getEmptyForeignCollection(s);
    }

    @Override
    public void setObjectCache(boolean b) throws SQLException {
        playerProfiles.setObjectCache(b);
    }

    @Override
    public void setObjectCache(ObjectCache objectCache) throws SQLException {
        playerProfiles.setObjectCache(objectCache);
    }

    @Override
    public ObjectCache getObjectCache() {
        return playerProfiles.getObjectCache();
    }

    @Override
    public void clearObjectCache() {
        playerProfiles.clearObjectCache();
    }

    @Override
    public PlayerProfileEntity mapSelectStarRow(DatabaseResults databaseResults) throws SQLException {
        return playerProfiles.mapSelectStarRow(databaseResults);
    }

    @Override
    public GenericRowMapper<PlayerProfileEntity> getSelectStarRowMapper() throws SQLException {
        return playerProfiles.getSelectStarRowMapper();
    }

    @Override
    public RawRowMapper<PlayerProfileEntity> getRawRowMapper() {
        return playerProfiles.getRawRowMapper();
    }

    @Override
    public boolean idExists(Long aLong) throws SQLException {
        return playerProfiles.idExists(aLong);
    }

    @Override
    public DatabaseConnection startThreadConnection() throws SQLException {
        return playerProfiles.startThreadConnection();
    }

    @Override
    public void endThreadConnection(DatabaseConnection databaseConnection) throws SQLException {
        playerProfiles.endThreadConnection(databaseConnection);
    }

    @Override
    public void setAutoCommit(DatabaseConnection databaseConnection, boolean b) throws SQLException {
        playerProfiles.setAutoCommit(databaseConnection, b);
    }

    @Override
    public boolean isAutoCommit(DatabaseConnection databaseConnection) throws SQLException {
        return playerProfiles.isAutoCommit(databaseConnection);
    }

    @Override
    public void commit(DatabaseConnection databaseConnection) throws SQLException {
        playerProfiles.commit(databaseConnection);
    }

    @Override
    public void rollBack(DatabaseConnection databaseConnection) throws SQLException {
        playerProfiles.rollBack(databaseConnection);
    }

    @Override
    public ConnectionSource getConnectionSource() {
        return playerProfiles.getConnectionSource();
    }

    @Override
    public void setObjectFactory(ObjectFactory<PlayerProfileEntity> objectFactory) {
        playerProfiles.setObjectFactory(objectFactory);
    }

    @Override
    public void registerObserver(DaoObserver daoObserver) {
        playerProfiles.registerObserver(daoObserver);
    }

    @Override
    public void unregisterObserver(DaoObserver daoObserver) {
        playerProfiles.unregisterObserver(daoObserver);
    }

    @Override
    public String getTableName() {
        return playerProfiles.getTableName();
    }

    @Override
    public void notifyChanges() {
        playerProfiles.notifyChanges();
    }

    @Override
    public PlayerProfileEntity createObjectInstance() throws SQLException {
        return playerProfiles.createObjectInstance();
    }

    @Override
    public TableInfo<PlayerProfileEntity, Long> getTableInfo() {
        return playerProfiles.getTableInfo();
    }

    @Override
    public CloseableIterator<PlayerProfileEntity> closeableIterator() {
        return playerProfiles.closeableIterator();
    }

    @Override
    public void forEach(Consumer<? super PlayerProfileEntity> action) {
        playerProfiles.forEach(action);
    }

    @Override
    public Spliterator<PlayerProfileEntity> spliterator() {
        return playerProfiles.spliterator();
    }


}
