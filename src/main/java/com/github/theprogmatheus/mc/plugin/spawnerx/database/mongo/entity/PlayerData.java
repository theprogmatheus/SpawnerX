package com.github.theprogmatheus.mc.plugin.spawnerx.database.mongo.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import org.bson.types.ObjectId;

@Entity
@Data
public class PlayerData {

    @Id
    private ObjectId id;

    private String name;
}
