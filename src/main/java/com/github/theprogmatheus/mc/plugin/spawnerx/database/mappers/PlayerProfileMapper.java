package com.github.theprogmatheus.mc.plugin.spawnerx.database.mappers;

import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.PlayerProfileEntity;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.PlayerProfile;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerProfileMapper implements ObjectMapper<PlayerProfileEntity, PlayerProfile> {

    @Override
    public PlayerProfile mapTo(@NotNull PlayerProfileEntity playerProfileEntity) {
        PlayerProfile profile = new PlayerProfile(playerProfileEntity.getPlayerData());
        profile.setDbId(playerProfileEntity.getId());
        return profile;
    }

    @Override
    public PlayerProfileEntity mapFrom(@NotNull PlayerProfile playerProfile) {
        PlayerProfileEntity playerProfileEntity = new PlayerProfileEntity();
        if (playerProfile.getDbId() != null)
            playerProfileEntity.setId(playerProfile.getDbId());
        playerProfileEntity.setPlayerData(playerProfile.getPlayerData());
        playerProfileEntity.setUuidString(playerProfile.getOriginal().toString());
        return playerProfileEntity;
    }

}
