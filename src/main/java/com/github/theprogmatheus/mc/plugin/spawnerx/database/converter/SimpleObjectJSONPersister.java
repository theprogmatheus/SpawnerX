package com.github.theprogmatheus.mc.plugin.spawnerx.database.converter;

import com.google.gson.Gson;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import lombok.Getter;

import java.sql.SQLException;

public class SimpleObjectJSONPersister extends BaseDataType {

    private static final Gson GSON = new Gson();

    @Getter
    private static final SimpleObjectJSONPersister singleton = new SimpleObjectJSONPersister();

    public SimpleObjectJSONPersister() {
        super(SqlType.LONG_STRING, new Class<?>[]{Object.class});
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject == null)
            return null;
        return GSON.toJson(javaObject);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (sqlArg == null)
            return null;
        return GSON.fromJson(sqlArg.toString(), fieldType.getType());
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String s) throws SQLException {
        return s;
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults databaseResults, int i) throws SQLException {
        return databaseResults.getString(i);
    }

    @Override
    public boolean isAppropriateId() {
        return false;
    }
}
