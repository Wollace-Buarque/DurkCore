package dev.cromo29.durkcore.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * créditos a: https://github.com/TheMFjulio/MFLib/blob/master/src/main/java/com/mateus/mflib/util/ItemBuilder.java
 */
public class NMSReflect {
    private static final String VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3];
    private static final HashMap<String, Class> CLASSES = new HashMap<>();

    public static Class getNMSClass(String name) {
        if (!CLASSES.containsKey(name)) {
            try {
                CLASSES.put(name, Class.forName("net.minecraft.server." + VERSION + "." + name));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return CLASSES.get(name);
    }

    public static Class getCraftBukkitClass(String prefix, String name) {
        if (!CLASSES.containsKey(prefix + "." + name)) {
            try {
                CLASSES.put(prefix + "." + name, Class.forName("org.bukkit.craftbukkit." + VERSION + "." + prefix + "." + name));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return CLASSES.get(prefix + "." + name);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"))
                    .invoke(playerConnection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}