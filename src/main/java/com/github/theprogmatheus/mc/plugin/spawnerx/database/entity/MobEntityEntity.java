package com.github.theprogmatheus.mc.plugin.spawnerx.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@DatabaseTable(tableName = "spawnerx_mob_entity")
@Data
public class MobEntityEntity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String mobId;

    @DatabaseField
    private int stackedAmount;

}
