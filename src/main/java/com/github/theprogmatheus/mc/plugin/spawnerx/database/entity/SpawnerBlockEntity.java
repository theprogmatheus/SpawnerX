package com.github.theprogmatheus.mc.plugin.spawnerx.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpawnerBlockEntity {

    private String world;
    private int x, y, z, chunkX, chunkZ;
    private String config;

}
