package com.github.theprogmatheus.mc.plugin.spawnerx.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Data
public class MobDropController {

    @Getter
    @Setter
    private static MobDropController global = new MobDropController(new HashMap<>());

    private final Map<String, MobDropSnapshotData> dropSnapshots;

    public boolean processDrop(@NotNull MobDrop drop) {
        var result = canDropNow(drop);
        if (result)
            process(drop);
        return result;
    }

    private boolean canDropNow(@NotNull MobDrop drop) {
        long now = System.currentTimeMillis();
        var snapshot = getDropSnapshot(drop);

        long delay = drop.getDelay();
        if (delay > 0) {
            long next = snapshot.timestamp + (delay * 1000L);
            if (now < next)
                return false;
        }

        int limit = drop.getDropLimitByInterval();
        long interval = drop.getDropLimitInterval();
        if (interval > 0 && limit > 0) {
            var globalSnapshot = global.getDropSnapshot(drop);
            long next = globalSnapshot.timestamp + (interval * 1000L);

            if (now >= next) {
                globalSnapshot.counter = 0;
                globalSnapshot.timestamp = System.currentTimeMillis();
            }

            return globalSnapshot.counter < limit;
        }

        return true;
    }

    private void process(@NotNull MobDrop drop) {
        var snapshot = getDropSnapshot(drop);
        snapshot.timestamp = System.currentTimeMillis();
        snapshot.counter++;

        if (drop.getDropLimitInterval() > 0 && drop.getDropLimitByInterval() > 0)
            global.getDropSnapshot(drop).counter++;
    }

    public MobDropSnapshotData getDropSnapshot(@NotNull MobDrop drop) {
        return this.dropSnapshots.computeIfAbsent(drop.getId(), key -> new MobDropSnapshotData());
    }

    public void serializeInFile(@NotNull File file) {
        try (var objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(global.dropSnapshots);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static MobDropController deserializeFromFile(@NotNull File file) {
        if (!file.exists())
            return new MobDropController(new HashMap<>());

        try (var objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            var dropSnapshots = (Map<String, MobDropSnapshotData>) objectInputStream.readObject();
            return new MobDropController(dropSnapshots);
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return new MobDropController(new HashMap<>());
        }
    }

    @Getter
    public static final class MobDropSnapshotData implements Serializable {
        private long timestamp;
        private int counter;
    }
}
