package com.github.theprogmatheus.mc.plugin.spawnerx.kdtree;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class KDNode<V> {

    Location loc;
    V value;
    KDNode<V> left, right;

}
