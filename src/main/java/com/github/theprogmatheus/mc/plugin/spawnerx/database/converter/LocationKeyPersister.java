package com.github.theprogmatheus.mc.plugin.spawnerx.database.converter;

import com.github.theprogmatheus.mc.plugin.spawnerx.domain.BlockLocationKey;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import lombok.Getter;

import java.sql.SQLException;

public class LocationKeyPersister extends BaseDataType {

    @Getter
    private static final LocationKeyPersister singleton = new LocationKeyPersister();

    public LocationKeyPersister() {
        super(SqlType.STRING, new Class<?>[]{BlockLocationKey.class});
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject == null)
            return null;

        BlockLocationKey key = (BlockLocationKey) javaObject;
        return "%s:%d:%d:%d".formatted(key.getWorld(), key.getX(), key.getY(), key.getZ());
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (sqlArg == null)
            return null;

        String[] parts = sqlArg.toString().split(":");
        if (parts.length != 4)
            throw new SQLException("Invalid BlockLocationKey string format: " + sqlArg);

        return new BlockLocationKey(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String s) throws SQLException {
        return s;
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults databaseResults, int columnPos) throws SQLException {
        return databaseResults.getString(columnPos);
    }
}
