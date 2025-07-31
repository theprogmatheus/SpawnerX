package com.github.theprogmatheus.mc.plugin.spawnerx.util;

public class PacketEventsUtils {

    public static boolean isPacketEventsAvailable() {
        try {
            Class.forName("com.github.retrooper.packetevents.PacketEvents");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
