package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChunkCoord {

    private String world;
    private int x, z;

    public static ChunkCoord fromChunk(@NotNull Chunk chunk) {
        return new ChunkCoord(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }
}
