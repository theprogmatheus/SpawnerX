package com.github.theprogmatheus.mc.plugin.spawnerx.util;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class LinkedObject<O> {

    private static final Map<Class<? extends LinkedObject>, Map<Object, LinkedObject<?>>> linkers = new ConcurrentHashMap<>();

    private final transient O original;

    public LinkedObject(O original) {
        if (original == null)
            throw new IllegalArgumentException("Original reference cannot be null");
        this.original = original;
    }

    public static <T extends LinkedObject<O>, O> Optional<T> getLink(Class<? extends T> linkerClass, O original) {
        var linkerMap = linkers.get(linkerClass);
        if (linkerMap == null)
            return Optional.empty();
        return Optional.ofNullable((T) linkerMap.get(original));
    }

    public static <T extends LinkedObject<?>> Optional<Map<Object, T>> getLinkerMap(Class<? extends T> linkerClass) {
        return Optional.ofNullable((Map<Object, T>) linkers.get(linkerClass));
    }

    public void link() {
        if (hasLinked())
            throw new RuntimeException("A link to this object already exists, use getLink() to retrieve the link for this object.");
        linkerMap().put(this.original, this);
    }

    public void unlink() {
        linkerMap().remove(this.original);
    }

    public boolean hasLinked() {
        return linkerMap().containsKey(this.original);
    }

    public boolean isBroken() {
        return false;
    }

    public boolean isOk() {
        return !isBroken();
    }

    private Map<Object, LinkedObject<?>> linkerMap() {
        return linkers.computeIfAbsent(getClass(), key -> new ConcurrentHashMap<>());
    }
}
