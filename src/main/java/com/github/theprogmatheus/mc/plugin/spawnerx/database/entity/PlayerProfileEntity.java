package com.github.theprogmatheus.mc.plugin.spawnerx.database.entity;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.converter.SimpleObjectJSONPersister;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.PlayerData;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable(tableName = "spawnerx_players")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerProfileEntity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String uuidString;

    @DatabaseField(canBeNull = false, persisterClass = SimpleObjectJSONPersister.class)
    private PlayerData playerData;

}
