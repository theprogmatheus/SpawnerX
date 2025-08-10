package com.github.theprogmatheus.mc.plugin.spawnerx.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerProfileEntity {

    private UUID uuid;
    private byte[] data;

}
