package com.github.theprogmatheus.mc.plugin.spawnerx.domain;


import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PlayerProfile extends LinkedObject<UUID> {

    private transient Long dbId;
    private transient Plugin plugin;
    private final PlayerData playerData;
    private transient MobDropController mobDropController;

    public PlayerProfile(@NotNull PlayerData playerData) {
        super(playerData.getId());
        this.playerData = playerData;
        this.mobDropController = new MobDropController(playerData.getDropSnapshots() != null ? playerData.getDropSnapshots() : new HashMap<>());
    }

    public static PlayerProfile fromPlayer(@NotNull Player player) {
        return LinkedObject.getLink(PlayerProfile.class, player.getUniqueId())
                .orElseGet(() -> (PlayerProfile) new PlayerProfile(new PlayerData(player.getUniqueId())).link());

    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getOriginal());
    }

    public @NotNull OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getOriginal());
    }

}
