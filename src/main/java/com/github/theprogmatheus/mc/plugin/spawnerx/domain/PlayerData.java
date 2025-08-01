package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerData {

    private final UUID id;
    private boolean hideSpawnerAnim;

}
