package com.github.theprogmatheus.mc.plugin.spawnerx.domain;


import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PlayerProfile extends LinkedObject<UUID> {

    private transient Long dbId;
    private final PlayerData playerData;

    public PlayerProfile(@NotNull PlayerData playerData) {
        super(playerData.getId());
        this.playerData = playerData;
    }

}
