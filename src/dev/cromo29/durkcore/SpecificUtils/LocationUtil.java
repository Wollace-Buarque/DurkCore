package dev.cromo29.durkcore.SpecificUtils;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class LocationUtil {
    public static ThreadLocalRandom random = ThreadLocalRandom.current();

    public LocationUtil() {
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }

    public static String serializeLocationInteger(Location location) {
        return location.getWorld().getName() + "@" + location.getBlockX() + "@" + location.getBlockY() + "@" + location.getBlockZ() + "@" + (int) location.getYaw() + "@" + (int) location.getPitch();
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + "@" + location.getX() + "@" + location.getY() + "@" + location.getZ() + "@" + location.getYaw() + "@" + location.getPitch();
    }

    public static String serializeSimpleLocation(Location location) {
        return location.getWorld().getName() + "@" + location.getX() + "@" + location.getY() + "@" + location.getZ();
    }

    public static Location unserializeLocation(String location) {
        try {
            String[] getSplited = location.split("@");
            String w = getSplited[0];
            double x = Double.parseDouble(getSplited[1]);
            double y = Double.parseDouble(getSplited[2]);
            double z = Double.parseDouble(getSplited[3]);
            float ya = Float.parseFloat(getSplited[4]);
            float p = Float.parseFloat(getSplited[5]);
            return new Location(Bukkit.getWorld(w), x, y, z, ya, p);
        } catch (Exception var12) {
            try {
                String[] getSplited = location.split("@");
                String w = getSplited[0];
                double x = Double.parseDouble(getSplited[1]);
                double y = Double.parseDouble(getSplited[2]);
                double z = Double.parseDouble(getSplited[3]);
                return new Location(Bukkit.getWorld(w), x, y, z);
            } catch (Exception var11) {
                return null;
            }
        }
    }

    public static List<Location> getSphere(Location loc, int radius, boolean hollow, boolean sphere, int plus_y) {
        List<Location> circleblocks = Lists.newArrayList();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                for (int y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + radius); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double) (radius * radius) && (!hollow || dist >= (double) ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        if (!circleblocks.contains(l)) {
                            circleblocks.add(l);
                        }
                    }
                }
            }
        }

        Location l = new Location(loc.getWorld(), cx, cy + plus_y, cz);
        if (!circleblocks.contains(l)) {
            circleblocks.add(l);
        }

        return circleblocks;
    }

    public static void setBlockMaterialAtLocation(Location corner, Location corner2, Material material) {
        if (corner != null && corner2 != null) {
            if (corner.getWorld() == corner2.getWorld()) {
                if (material != null) {
                    int topBlockX = Math.max(corner.getBlockX(), corner2.getBlockX());
                    int bottomBlockX = Math.min(corner.getBlockX(), corner2.getBlockX());
                    int topBlockY = Math.max(corner.getBlockY(), corner2.getBlockY());
                    int bottomBlockY = Math.min(corner.getBlockY(), corner2.getBlockY());
                    int topBlockZ = Math.max(corner.getBlockZ(), corner2.getBlockZ());
                    int bottomBlockZ = Math.min(corner.getBlockZ(), corner2.getBlockZ());
                    World w = corner.getWorld();

                    for (int x = bottomBlockX; x <= topBlockX; ++x) {
                        for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                            for (int y = bottomBlockY; y <= topBlockY; ++y) {
                                (new Location(w, x, y, z)).getBlock().setType(material);
                            }
                        }
                    }

                }
            }
        }
    }

    public static boolean containsOnlyMaterialInsideLocation(Location corner, Location corner2, Material material) {
        if (corner != null && corner2 != null) {
            if (corner.getWorld() != corner2.getWorld()) {
                return false;
            } else if (material == null) {
                return false;
            } else {
                int topBlockX = Math.max(corner.getBlockX(), corner2.getBlockX());
                int bottomBlockX = Math.min(corner.getBlockX(), corner2.getBlockX());
                int topBlockY = Math.max(corner.getBlockY(), corner2.getBlockY());
                int bottomBlockY = Math.min(corner.getBlockY(), corner2.getBlockY());
                int topBlockZ = Math.max(corner.getBlockZ(), corner2.getBlockZ());
                int bottomBlockZ = Math.min(corner.getBlockZ(), corner2.getBlockZ());
                World w = corner.getWorld();

                for (int x = bottomBlockX; x <= topBlockX; ++x) {
                    for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                        for (int y = bottomBlockY; y <= topBlockY; ++y) {
                            if ((new Location(w, x, y, z)).getBlock().getType() != material) {
                                return false;
                            }
                        }
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean containsMaterialInsideLocation(Location corner, Location corner2, Material... material) {
        if (corner != null && corner2 != null) {
            if (corner.getWorld() != corner2.getWorld()) {
                return false;
            } else if (material == null) {
                return false;
            } else {
                List<Material> materials = Lists.newArrayList(material);
                if (materials.isEmpty()) {
                    return false;
                } else {
                    int topBlockX = Math.max(corner.getBlockX(), corner2.getBlockX());
                    int bottomBlockX = Math.min(corner.getBlockX(), corner2.getBlockX());
                    int topBlockY = Math.max(corner.getBlockY(), corner2.getBlockY());
                    int bottomBlockY = Math.min(corner.getBlockY(), corner2.getBlockY());
                    int topBlockZ = Math.max(corner.getBlockZ(), corner2.getBlockZ());
                    int bottomBlockZ = Math.min(corner.getBlockZ(), corner2.getBlockZ());

                    for (int x = bottomBlockX; x <= topBlockX; ++x) {
                        for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                            for (int y = bottomBlockY; y <= topBlockY; ++y) {
                                if (materials.contains((new Location(corner.getWorld(), x, y, z)).getBlock().getType())) {
                                    return true;
                                }
                            }
                        }
                    }

                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static List<Chunk> getChunkCircle(Chunk chunk, int radius, boolean hollow) {
        List<Chunk> chunks = Lists.newArrayList();
        int cx = chunk.getX();
        int cz = chunk.getZ();

        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                if (dist < (double) (radius * radius) && (!hollow || dist >= (double) ((radius - 1) * (radius - 1)))) {
                    Location l = new Location(chunk.getWorld(), x, 0.0D, z);
                    if (!chunks.contains(l.getChunk())) {
                        chunks.add(l.getChunk());
                    }
                }
            }
        }

        return chunks;
    }

    public static List<String> getChunkCircleXZ(int x, int z, int radius, boolean hollow) {
        List<String> chunks = Lists.newArrayList();

        for (int forX = x - radius; forX <= x + radius; ++forX) {
            for (int forZ = z - radius; forZ <= z + radius; ++forZ) {
                double dist = (x - forX) * (x - forX) + (z - forZ) * (z - forZ);
                if (dist < (double) (radius * radius) && (!hollow || dist >= (double) ((radius - 1) * (radius - 1))) && !chunks.contains(forX + "@" + forZ)) {
                    chunks.add(forX + "@" + forZ);
                }
            }
        }

        return chunks;
    }

    public static List<String> getNearbyChunksXZ(int x, int z, int radius) {
        List<String> chunks = ListUtil.getList(new String[0]);

        for (int loopX = x - radius; loopX <= x + radius; ++loopX) {
            for (int loopZ = z - radius; loopZ <= z + radius; ++loopZ) {
                if (!chunks.contains(loopX + "@" + loopZ)) {
                    chunks.add(loopX + "@" + loopZ);
                }
            }
        }

        return chunks;
    }

    public static List<Chunk> getNearbyChunks(Chunk chunk, int radius) {
        List<Chunk> chunks = ListUtil.getList(new Chunk[0]);
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        for (int x = chunkX - radius; x <= chunkX + radius; ++x) {
            for (int z = chunkZ - radius; z <= chunkZ + radius; ++z) {
                if (!chunks.contains(chunk.getWorld().getChunkAt(x, z))) {
                    chunks.add(chunk.getWorld().getChunkAt(x, z));
                }
            }
        }

        return chunks;
    }

    public static List<Block> getNearbyBlocks(Block block, int radius) {
        int iterations = radius * 2 + 1;
        List<Block> blocks = new ArrayList(iterations * iterations * iterations);

        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    blocks.add(block.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean isLocationSafe(Location loc) {
        World world = loc.getWorld();
        Block teleblock = world.getHighestBlockAt(loc).getRelative(BlockFace.DOWN);
        Block block1 = world.getBlockAt(loc.add(0.0D, 1.0D, 0.0D));
        Block block2 = world.getBlockAt(loc.add(0.0D, 2.0D, 0.0D));
        if (!block1.isLiquid() && !block2.isLiquid() && !teleblock.isLiquid()) {
            return teleblock.getType().isSolid() || block1.getType() != Material.AIR || block2.getType() != Material.AIR;
        } else {
            return false;
        }
    }

    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = Lists.newArrayList();

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public List<Block> getBlocksAt(Location loc1, Location loc2) {
        List<Block> blocks = Lists.newArrayList();
        int topBlockX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int bottomBlockX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int topBlockY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int bottomBlockY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int topBlockZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        int bottomBlockZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; ++x) {
            for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                for (int y = bottomBlockY; y <= topBlockY; ++y) {
                    blocks.add(loc1.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public int getAmountOfBlocksAt(Location loc1, Location loc2) {
        int blocks = 0;
        int topBlockX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int bottomBlockX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int topBlockY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int bottomBlockY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int topBlockZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        int bottomBlockZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; ++x) {
            for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                for (int y = bottomBlockY; y <= topBlockY; ++y) {
                    ++blocks;
                }
            }
        }

        return blocks;
    }

    public static Location getRandomLocation(World w) {
        int centerX = (int) w.getWorldBorder().getCenter().getX();
        int centerZ = (int) w.getWorldBorder().getCenter().getZ();
        int maxX = centerX + (int) w.getWorldBorder().getSize() / 2;
        int maxZ = centerZ + (int) w.getWorldBorder().getSize() / 2;
        int minX = centerX - (int) w.getWorldBorder().getSize() / 2;
        int minZ = centerZ - (int) w.getWorldBorder().getSize() / 2;
        int randomX = NumberUtil.getRandom(minX, maxX);
        int randomZ = NumberUtil.getRandom(minZ, maxZ);
        return new Location(w, randomX, 0.0D, randomZ);
    }

    public static List<Player> getClosestPlayersFromLocation(Location location, double distance) {
        List<Player> result = Lists.newArrayList();
        double d2 = distance * distance;
        Iterator var6 = location.getWorld().getPlayers().iterator();

        while (var6.hasNext()) {
            Player player = (Player) var6.next();
            if (player.getLocation().add(0.0D, 0.85D, 0.0D).distanceSquared(location) <= d2) {
                result.add(player);
            }
        }

        return result;
    }

    public static List<Entity> getClosestEntitiesFromLocation(Location location, double radius) {
        double chunkRadius = radius < 16.0D ? 1.0D : (radius - radius % 16.0D) / 16.0D;
        List<Entity> radiusEntities = Lists.newArrayList();

        for (double chX = 0.0D - chunkRadius; chX <= chunkRadius; ++chX) {
            for (double chZ = 0.0D - chunkRadius; chZ <= chunkRadius; ++chZ) {
                int x = (int) location.getX();
                int y = (int) location.getY();
                int z = (int) location.getZ();
                Entity[] var13 = (new Location(location.getWorld(), (double) x + chX * 16.0D, y, (double) z + chZ * 16.0D)).getChunk().getEntities();
                Entity[] var14 = var13;
                int var15 = var13.length;

                for (int var16 = 0; var16 < var15; ++var16) {
                    Entity e = var14[var16];
                    if (e.getLocation().distance(location) <= radius && e.getLocation().getBlock() != location.getBlock()) {
                        radiusEntities.add(e);
                    }
                }
            }
        }

        return radiusEntities;
    }

    public static List<Location> getParticleCircle(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = 6.283185307179586D / (double) amount;
        List<Location> locations = Lists.newArrayList();

        for (int i = 0; i < amount; ++i) {
            double angle = (double) i * increment;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            locations.add(new Location(world, x, center.getY(), z));
        }

        return locations;
    }

    public static Location lookAt(Location from, Location to, float aiming) {
        if (!(aiming <= 0.0F)) {
            from = from.clone();
            double dx = to.getX() - from.getX();
            double dy = to.getY() - from.getY();
            double dz = to.getZ() - from.getZ();
            if (dx != 0.0D) {
                if (dx < 0.0D) {
                    from.setYaw(4.712389F);
                } else {
                    from.setYaw(1.5707964F);
                }

                from.setYaw(from.getYaw() - (float) Math.atan(dz / dx));
            } else if (dz < 0.0D) {
                from.setYaw(3.1415927F);
            }

            double dxz = Math.sqrt(Math.pow(dx, 2.0D) + Math.pow(dz, 2.0D));
            from.setPitch((float) (-Math.atan(dy / dxz)));
            float yawAiming = 360.0F - aiming * 360.0F / 100.0F;
            if (yawAiming > 0.0F) {
                yawAiming = (float) NumberUtil.getRandom((int) yawAiming, (int) (-yawAiming));
            }

            float pitchAiming = 180.0F - aiming * 180.0F / 100.0F;
            if (pitchAiming > 0.0F) {
                pitchAiming = (float) NumberUtil.getRandom((int) pitchAiming, (int) (-pitchAiming));
            }

            from.setYaw(-from.getYaw() * 180.0F / 3.1415927F + yawAiming);
            from.setPitch(from.getPitch() * 180.0F / 3.1415927F + pitchAiming);
        }
        return from;
    }

    public static boolean isInRadius(Player player, Location loc, double radius) {
        double x = loc.getX() - player.getLocation().getX();
        double z = loc.getZ() - player.getLocation().getZ();
        double distance = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(z, 2.0D));
        return distance <= radius;
    }

    public static boolean isInRadius3D(Player player, Location loc, double radius) {
        double x = loc.getX() - player.getLocation().getX();
        double y = loc.getY() - player.getLocation().getY();
        double z = loc.getZ() - player.getLocation().getZ();
        double distance = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D) + Math.pow(z, 2.0D));
        return distance <= radius;
    }

    public static boolean isInRadius3D(Location midpoint, Location loc, double radius) {
        double x = loc.getX() - midpoint.getX();
        double y = loc.getY() - midpoint.getY();
        double z = loc.getZ() - midpoint.getZ();
        double distance = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D) + Math.pow(z, 2.0D));
        return distance <= radius;
    }

    public static Location getRandomLocationArroundPlayer(Player player, int XmaxDistance, int ZmaxDistance) {
        World world = player.getWorld();
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        int randomX = 1 + (int) (Math.random() * (double) (XmaxDistance - 1 + 1));
        int randomZ = 1 + (int) (Math.random() * (double) (ZmaxDistance - 1 + 1));
        x += randomX;
        double y = player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getY();
        z += randomZ;
        return new Location(world, x, y, z);
    }

    public static Location getCenter(Location loc) {
        return loc.clone().add(0.5D, 0.0D, 0.5D);
    }

    public static LinkedList<Player> getNearby(Location loc, double maxDist) {
        LinkedList<Player> nearbyMap = Lists.newLinkedList();
        Iterator var4 = loc.getWorld().getPlayers().iterator();

        while (true) {
            Player cur;
            double dist;
            do {
                do {
                    do {
                        if (!var4.hasNext()) {
                            return nearbyMap;
                        }

                        cur = (Player) var4.next();
                    } while (cur.getGameMode() == GameMode.CREATIVE);
                } while (cur.isDead());

                dist = loc.toVector().subtract(cur.getLocation().toVector()).length();
            } while (dist > maxDist);

            for (int i = 0; i < nearbyMap.size(); ++i) {
                if (dist < loc.toVector().subtract(nearbyMap.get(i).getLocation().toVector()).length()) {
                    nearbyMap.add(i, cur);
                    break;
                }
            }

            if (!nearbyMap.contains(cur)) {
                nearbyMap.addLast(cur);
            }
        }
    }

    public static Block getHighest(World world, int x, int z) {
        return getHighest(world, x, z, null);
    }

    public static Block getHighest(World world, int x, int z, HashSet<Material> ignore) {
        Block block;
        for (block = world.getHighestBlockAt(x, z); block.getType().isSolid() || block.getType().toString().contains("LEAVES") || ignore != null && ignore.contains(block.getType()); block = block.getRelative(BlockFace.DOWN)) {
        }

        return block.getRelative(BlockFace.UP);
    }

    public static Player getClosest(Location loc, Collection<Player> ignore) {
        Player best = null;
        double bestDist = 0.0D;
        Iterator var5 = loc.getWorld().getPlayers().iterator();

        while (true) {
            Player cur;
            double dist;
            do {
                do {
                    do {
                        do {
                            if (!var5.hasNext()) {
                                return best;
                            }

                            cur = (Player) var5.next();
                        } while (cur.getGameMode() == GameMode.CREATIVE);
                    } while (cur.isDead());
                } while (ignore != null && ignore.contains(cur));

                dist = offset(cur.getLocation(), loc);
            } while (best != null && dist >= bestDist);

            best = cur;
            bestDist = dist;
        }
    }

    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static void detonateRandomFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        int rt = random.nextInt(4) + 1;
        Type type = Type.BALL;
        if (rt == 2) {
            type = Type.BALL_LARGE;
        }

        if (rt == 3) {
            type = Type.BURST;
        }

        if (rt == 4) {
            type = Type.CREEPER;
        }

        if (rt == 5) {
            type = Type.STAR;
        }

        int r1i = random.nextInt(18) + 1;
        int r2i = random.nextInt(18) + 1;
        Color c1 = TXT.getColor(r1i);
        Color c2 = TXT.getColor(r2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(random.nextBoolean()).build();
        fwm.addEffect(effect);
        fwm.setPower(0);
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }
}