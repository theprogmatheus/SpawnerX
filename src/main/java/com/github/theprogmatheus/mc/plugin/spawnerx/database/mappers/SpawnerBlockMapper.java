package com.github.theprogmatheus.mc.plugin.spawnerx.database.mappers;

import com.github.theprogmatheus.mc.plugin.spawnerx.SpawnerX;
import com.github.theprogmatheus.mc.plugin.spawnerx.database.entity.SpawnerBlockEntity;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.MobConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlock;
import com.github.theprogmatheus.mc.plugin.spawnerx.domain.SpawnerBlockConfig;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.LinkedObject;
import com.github.theprogmatheus.mc.plugin.spawnerx.util.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnerBlockMapper implements ObjectMapper<SpawnerBlockEntity, SpawnerBlock> {

    private final SpawnerX plugin;

    @Override
    public SpawnerBlock mapTo(@NotNull SpawnerBlockEntity spawnerBlockEntity) {
        var configId = spawnerBlockEntity.getConfig().toLowerCase();
        var location = spawnerBlockEntity.getLocation();
        var configLink = LinkedObject.getLink(SpawnerBlockConfig.class, configId);
        var spawnerBlock = configLink
                .map(config -> new SpawnerBlock(location, config))
                .orElseGet(() -> new SpawnerBlock(location, new SpawnerBlockConfig(this.plugin, configId, new MobConfig(EntityType.UNKNOWN.name(), EntityType.UNKNOWN))));

        spawnerBlock.setDbId(spawnerBlockEntity.getId());
        spawnerBlock.setStackedAmount(spawnerBlockEntity.getStackedAmount());
        return spawnerBlock;
    }

    @Override
    public SpawnerBlockEntity mapFrom(@NotNull SpawnerBlock spawnerBlock) {
        return new SpawnerBlockEntity(spawnerBlock.getDbId(), spawnerBlock.getConfig().getId(), spawnerBlock.getOriginal(), spawnerBlock.getStackedAmount());
    }
}
