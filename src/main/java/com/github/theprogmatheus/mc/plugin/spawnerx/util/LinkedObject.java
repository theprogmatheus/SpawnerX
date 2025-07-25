package com.github.theprogmatheus.mc.plugin.spawnerx.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public abstract class LinkedObject<O> {

    private static final Map<Class<? extends LinkedObject>, Map<Object, LinkedObject<?>>> linkers = new HashMap<>();

    private final transient O original;

    public LinkedObject(O original) {
        this.original = original;
        this.link();
    }

    public static <T extends LinkedObject<O>, O> Optional<T> getLink(Class<? extends T> linkerClass, O original) {
        var linkerMap = linkers.get(linkerClass);
        if (linkerMap == null)
            return Optional.empty();
        return Optional.ofNullable((T) linkerMap.get(original));
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

    private Map<Object, LinkedObject<?>> linkerMap() {
        return linkers.computeIfAbsent(getClass(), key -> new HashMap<>());
    }
}
