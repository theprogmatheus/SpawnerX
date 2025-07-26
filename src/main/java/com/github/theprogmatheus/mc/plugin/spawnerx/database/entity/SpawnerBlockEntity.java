package com.github.theprogmatheus.mc.plugin.spawnerx.database.entity;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.converter.SimpleObjectJSONPersister;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.BlockLocationKey;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable(tableName = "spawnerx_spawner_block")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpawnerBlockEntity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String config;

    @DatabaseField(canBeNull = false, persisterClass = SimpleObjectJSONPersister.class)
    private BlockLocationKey location;
}
