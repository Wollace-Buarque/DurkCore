package dev.cromo29.durkcore.Util;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleMaker {

    private static ConsoleCommandSender sender = Bukkit.getConsoleSender();
    private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    private static void warnNotSupported() {
        sender.sendMessage(TXT.parse("<c>Particula nao suportada!"));
    }

    public static void sendParticle(ParticleEffect effect, Location location, Color color, int amount, boolean normalLocation) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        Location loc = location.clone();

        if (normalLocation) {
            for (int i = 0; i < amount; i++) {
                effect.display(new ParticleEffect.OrdinaryColor(color), loc, 50);
            }
            return;
        }
        for (int i = 0; i < amount; i++) {

            double x = threadLocalRandom.nextDouble(-1, 1);
            double y = threadLocalRandom.nextDouble(-1, 1);

            loc.add(x, y, x);
            effect.display(new ParticleEffect.OrdinaryColor(color), loc, 50);
            loc.subtract(x, y, x);
        }
    }

    public static void sendParticle(ParticleEffect effect, Location location, Color color, int amount) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        Location loc = location.clone();

        for (int i = 0; i < amount; i++) {

            double x = threadLocalRandom.nextDouble(-1, 1);
            double y = threadLocalRandom.nextDouble(-1, 1);

            loc.add(x, y, x);
            effect.display(new ParticleEffect.OrdinaryColor(color), loc, 50);

            loc.subtract(x, y, x);
        }
    }

    public static void sendParticle(ParticleEffect effect, Location location, Color color, int amount, double range) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        Location loc = location.clone();

        for (int i = 0; i < amount; i++) {

            double x = threadLocalRandom.nextDouble(-1, 1);
            double y = threadLocalRandom.nextDouble(-1, 1);

            loc.add(x, y, x);
            effect.display(new ParticleEffect.OrdinaryColor(color), loc, range);
            loc.subtract(x, y, x);
        }
    }

    public static void sendParticle(ParticleEffect effect, Location location, Color color, int amount, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        Location loc = location.clone();

        for (int i = 0; i < amount; i++) {

            double x = threadLocalRandom.nextDouble(-1, 1);
            double y = threadLocalRandom.nextDouble(-1, 1);

            loc.add(x, y, x);
            effect.display(new ParticleEffect.OrdinaryColor(color), loc, players);
            loc.subtract(x, y, x);
        }
    }

    public static void sendParticle(ParticleEffect effect, Location location, Color color, int amount, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        Location loc = location.clone();

        for (int i = 0; i < amount; i++) {

            double x = threadLocalRandom.nextDouble(-1, 1);
            double y = threadLocalRandom.nextDouble(-1, 1);

            loc.add(x, y, x);
            effect.display(new ParticleEffect.OrdinaryColor(color), loc, players);
            loc.subtract(x, y, x);
        }
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.BlockData data, Location location, Vector direction, float speed, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(data, direction, speed, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.BlockData data, Location location, Vector direction, float speed, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(data, direction, speed, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.BlockData data, Location location, Vector direction, float speed, double range) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(data, direction, speed, location, range);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.BlockData data, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(data, offsetX, offsetY, offsetZ, speed, amount, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.BlockData data, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(data, offsetX, offsetY, offsetZ, speed, amount, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.BlockData data, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, double radius) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(data, offsetX, offsetY, offsetZ, speed, amount, location, radius);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.ItemData data, Vector direction, float speed, Location center, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(data, direction, speed, center, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.ItemData data, Vector direction, float speed, Location center, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(data, direction, speed, center, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.ItemData data, Vector direction, Location location, float speed, double range) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(data, direction, speed, location, range);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.ItemData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(data, offsetX, offsetY, offsetZ, speed, amount, center, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.ItemData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(data, offsetX, offsetY, offsetZ, speed, amount, center, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.ItemData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(data, offsetX, offsetY, offsetZ, speed, amount, center, range);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.NoteColor color, Location location, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(color, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.NoteColor color, Location location, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(color, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.NoteColor color, Location location, double radius) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(color, location, radius);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.OrdinaryColor color, Location location, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(color, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.OrdinaryColor color, Location location, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(color, location, players);
    }

    public static void sendParticle(ParticleEffect effect, ParticleEffect.OrdinaryColor color, Location location, double radius) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(color, location, radius);
    }

    public static void sendParticle(ParticleEffect effect, Location location, Vector direction, float speed, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(direction, speed, location, players);
    }

    public static void sendParticle(ParticleEffect effect, Location location, Vector direction, float speed, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(direction, speed, location, players);
    }

    public static void sendParticle(ParticleEffect effect, Location location, Vector direction, float speed, double radius) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(direction, speed, location, radius);
    }

    public static void sendParticle(ParticleEffect effect, Location location, float speed, int amount, double radius) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(0, 0, 0, speed, amount, location, radius);
    }

    public static void sendParticle(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        List<Player> list = Arrays.asList(players);

        if (list.isEmpty()) return;

        effect.display(offsetX, offsetY, offsetZ, speed, amount, location, players);
    }

    public static void sendParticle(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, List<Player> players) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        if (players.isEmpty()) return;

        effect.display(offsetX, offsetY, offsetZ, speed, amount, location, players);
    }

    public static void sendParticle(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, double radius) {
        if (!effect.isSupported()) {
            warnNotSupported();
            return;
        }

        effect.display(offsetX, offsetY, offsetZ, speed, amount, location, radius);
    }
}
