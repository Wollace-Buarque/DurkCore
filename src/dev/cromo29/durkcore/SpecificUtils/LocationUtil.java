package dev.cromo29.durkcore.SpecificUtils;

import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {

    public static ThreadLocalRandom random = ThreadLocalRandom.current();

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }

    public static String serializeLocationInteger(Location location) {
        return location.getWorld().getName() + "@" + location.getBlockX() + "@" + location.getBlockY()
                + "@" + location.getBlockZ() + "@" + (int) location.getYaw() + "@" + (int) location.getPitch();
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + "@" + location.getX() + "@" + location.getY() + "@" + location.getZ()
                + "@" + location.getYaw() + "@" + location.getPitch();
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
            float yaw = Float.parseFloat(getSplited[4]);
            float pitch = Float.parseFloat(getSplited[5]);

            return new Location(Bukkit.getWorld(w), x, y, z, yaw, pitch);
        } catch (Exception ignored) {
            try {
                String[] getSplited = location.split("@");
                String w = getSplited[0];
                double x = Double.parseDouble(getSplited[1]);
                double y = Double.parseDouble(getSplited[2]);
                double z = Double.parseDouble(getSplited[3]);

                return new Location(Bukkit.getWorld(w), x, y, z);
            } catch (Exception exception) {
                return null;
            }
        }
    }

    public static List<Location> getSphere(Location loc, int radius, boolean hollow, boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                for (int y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + radius); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double) (radius * radius) && (!hollow || dist >= (double) ((radius - 1) * (radius - 1)))) {
                        Location location = new Location(loc.getWorld(), x, y + plus_y, z);

                        if (!circleblocks.contains(location)) {
                            circleblocks.add(location);
                        }
                    }
                }
            }
        }

        Location location = new Location(loc.getWorld(), cx, cy + plus_y, cz);

        if (!circleblocks.contains(location)) circleblocks.add(location);

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
                    World world = corner.getWorld();

                    for (int x = bottomBlockX; x <= topBlockX; ++x) {
                        for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                            for (int y = bottomBlockY; y <= topBlockY; ++y) {
                                (new Location(world, x, y, z)).getBlock().setType(material);
                            }
                        }
                    }

                }
            }
        }
    }

    public static boolean containsOnlyMaterialInsideLocation(Location corner, Location corner2, Material material) {
        if (corner != null && corner2 != null) {
            if (corner.getWorld() != corner2.getWorld()) return false;
            else if (material == null) return false;

            else {
                int topBlockX = Math.max(corner.getBlockX(), corner2.getBlockX());
                int bottomBlockX = Math.min(corner.getBlockX(), corner2.getBlockX());
                int topBlockY = Math.max(corner.getBlockY(), corner2.getBlockY());
                int bottomBlockY = Math.min(corner.getBlockY(), corner2.getBlockY());
                int topBlockZ = Math.max(corner.getBlockZ(), corner2.getBlockZ());
                int bottomBlockZ = Math.min(corner.getBlockZ(), corner2.getBlockZ());
                World world = corner.getWorld();

                for (int x = bottomBlockX; x <= topBlockX; ++x) {
                    for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                        for (int y = bottomBlockY; y <= topBlockY; ++y) {
                            if ((new Location(world, x, y, z)).getBlock().getType() != material) {
                                return false;
                            }
                        }
                    }
                }

                return true;
            }
        } else return false;
    }

    public static boolean containsMaterialInsideLocation(Location corner, Location corner2, Material... material) {
        if (corner != null && corner2 != null) {
            if (corner.getWorld() != corner2.getWorld()) return false;
            else if (material == null) return false;
            else {
                List<Material> materials = Arrays.asList(material);

                if (!materials.isEmpty()) {
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
                }
                return false;
            }
        } else return false;
    }

    public static List<Chunk> getChunkCircle(Chunk chunk, int radius, boolean hollow) {
        List<Chunk> chunks = new ArrayList<>();
        int cx = chunk.getX();
        int cz = chunk.getZ();

        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);

                if (dist < (double) (radius * radius) && (!hollow || dist >= (double) ((radius - 1) * (radius - 1)))) {
                    Location location = new Location(chunk.getWorld(), x, 0.0D, z);

                    if (!chunks.contains(location.getChunk())) {
                        chunks.add(location.getChunk());
                    }
                }
            }
        }

        return chunks;
    }

    public static List<String> getChunkCircleXZ(int x, int z, int radius, boolean hollow) {
        List<String> chunks = new ArrayList<>();

        for (int forX = x - radius; forX <= x + radius; ++forX) {
            for (int forZ = z - radius; forZ <= z + radius; ++forZ) {
                double dist = (x - forX) * (x - forX) + (z - forZ) * (z - forZ);

                if (dist < (double) (radius * radius)
                        && (!hollow || dist >= (double) ((radius - 1) * (radius - 1)))
                        && !chunks.contains(forX + "@" + forZ)) {
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
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);

        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    blocks.add(block.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static boolean isLocationSafe(Location location) {
        World world = location.getWorld();
        Block teleBlock = world.getHighestBlockAt(location).getRelative(BlockFace.DOWN);
        Block block1 = world.getBlockAt(location.add(0.0, 1.0, 0.0));
        Block block2 = world.getBlockAt(location.add(0.0, 2.0, 0.0));

        if (!block1.isLiquid() && !block2.isLiquid() && !teleBlock.isLiquid())
            return teleBlock.getType().isSolid() || block1.getType() != Material.AIR || block2.getType() != Material.AIR;
        else return false;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static List<Block> getBlocksAt(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    blocks.add(loc1.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static int getAmountOfBlocksAt(Location loc1, Location loc2) {
        int blocks = 0;

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    blocks++;
                }
            }
        }

        return blocks;
    }

    public static Location getRandomLocation(World world) {
        int centerX = (int) world.getWorldBorder().getCenter().getX();
        int centerZ = (int) world.getWorldBorder().getCenter().getZ();
        int maxX = centerX + (int) world.getWorldBorder().getSize() / 2;
        int maxZ = centerZ + (int) world.getWorldBorder().getSize() / 2;
        int minX = centerX - (int) world.getWorldBorder().getSize() / 2;
        int minZ = centerZ - (int) world.getWorldBorder().getSize() / 2;
        int randomX = NumberUtil.getRandom(minX, maxX);
        int randomZ = NumberUtil.getRandom(minZ, maxZ);

        return new Location(world, randomX, 0.0D, randomZ);
    }

    public static List<Player> getClosestPlayersFromLocation(Location location, double distance) {
        List<Player> result = new ArrayList<>();

        double squaredDistance = distance * distance;
        Iterator<Player> playerIterator = location.getWorld().getPlayers().iterator();

        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();

            if (player.getLocation().add(0.0, 0.85, 0.0).distanceSquared(location) <= squaredDistance) {
                result.add(player);
            }
        }

        return result;
    }

    public static List<Entity> getClosestEntitiesFromLocation(Location location, double radius) {
        double chunkRadius = radius < 16.0 ? 1.0 : (radius - radius % 16.0) / 16.0;
        List<Entity> radiusEntities = new ArrayList<>();

        for (double chX = 0.0D - chunkRadius; chX <= chunkRadius; ++chX) {
            for (double chZ = 0.0D - chunkRadius; chZ <= chunkRadius; ++chZ) {

                int x = (int) location.getX();
                int y = (int) location.getY();
                int z = (int) location.getZ();

                Entity[] entities = (new Location(location.getWorld(), (double) x + chX * 16.0, y, (double) z + chZ * 16.0)).getChunk().getEntities();

                for (Entity entity : entities) {

                    if (entity.getLocation().distance(location) <= radius
                            && entity.getLocation().getBlock() != location.getBlock()) {
                        radiusEntities.add(entity);
                    }
                }
            }
        }

        return radiusEntities;
    }

    public static List<Location> getParticleCircle(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = 6.283185307179586D / (double) amount;
        List<Location> locations = new ArrayList<>();

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

            if (dx != 0.0) {

                if (dx < 0.0) from.setYaw(4.712389F);
                else from.setYaw(1.5707964F);

                from.setYaw(from.getYaw() - (float) Math.atan(dz / dx));

            } else if (dz < 0.0) from.setYaw(3.1415927F);

            double dxz = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dz, 2.0));
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

    public static boolean isInRadius(Player player, Location location, double radius) {
        double x = location.getX() - player.getLocation().getX();
        double z = location.getZ() - player.getLocation().getZ();
        double distance = Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));

        return distance <= radius;
    }

    public static boolean isInRadius3D(Player player, Location location, double radius) {
        double x = location.getX() - player.getLocation().getX();
        double y = location.getY() - player.getLocation().getY();
        double z = location.getZ() - player.getLocation().getZ();
        double distance = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0));

        return distance <= radius;
    }

    public static boolean isInRadius3D(Location midpoint, Location location, double radius) {
        double x = location.getX() - midpoint.getX();
        double y = location.getY() - midpoint.getY();
        double z = location.getZ() - midpoint.getZ();
        double distance = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0));

        return distance <= radius;
    }

    public static Location getRandomLocationArroundPlayer(Player player, int XmaxDistance, int ZmaxDistance) {
        World world = player.getWorld();
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        int randomX = 1 + (int) (Math.random() * (double) (XmaxDistance - 1 + 1));
        int randomZ = 1 + (int) (Math.random() * (double) (ZmaxDistance - 1 + 1));

        x += randomX;

        double y = player.getLocation().clone().add(0.0, 1.0, 0.0).getY();

        z += randomZ;

        return new Location(world, x, y, z);
    }

    public static Location getCenter(Location location) {
        return location.clone().add(0.5, 0.0, 0.5);
    }

    public static LinkedList<Player> getNearby(Location location, double maxDist) {
        LinkedList<Player> nearbyMap = new LinkedList<>();
        Iterator<Player> iterator = location.getWorld().getPlayers().iterator();

        while (true) {
            Player player;
            double distance;
            do {
                do {
                    do {

                        if (!iterator.hasNext()) {
                            return nearbyMap;
                        }

                        player = iterator.next();
                    } while (player.getGameMode() == GameMode.CREATIVE);
                } while (player.isDead());

                distance = location.toVector().subtract(player.getLocation().toVector()).length();
            } while (distance > maxDist);

            for (int i = 0; i < nearbyMap.size(); ++i) {
                if (distance < location.toVector().subtract(nearbyMap.get(i).getLocation().toVector()).length()) {
                    nearbyMap.add(i, player);
                    break;
                }
            }

            if (!nearbyMap.contains(player)) {
                nearbyMap.addLast(player);
            }
        }
    }

    public static Block getHighest(World world, int x, int z) {
        return getHighest(world, x, z, null);
    }

    public static Block getHighest(World world, int x, int z, HashSet<Material> ignore) {
        Block block;

        for (block = world.getHighestBlockAt(x, z); block.getType().isSolid()
                || block.getType().toString().contains("LEAVES")
                || ignore != null
                && ignore.contains(block.getType());
             block = block.getRelative(BlockFace.DOWN)) {
        }

        return block.getRelative(BlockFace.UP);
    }

    public static Player getClosest(Location location, Collection<Player> ignore) {
        Player best = null;
        double bestDist = 0.0;
        Iterator<Player> iterator = location.getWorld().getPlayers().iterator();

        while (true) {
            Player player;
            double distance;
            do {
                do {
                    do {
                        do {
                            if (!iterator.hasNext()) return best;

                            player = iterator.next();
                        } while (player.getGameMode() == GameMode.CREATIVE);
                    } while (player.isDead());
                } while (ignore != null && ignore.contains(player));

                distance = offset(player.getLocation(), location);

            } while (best != null && distance >= bestDist);

            best = player;
            bestDist = distance;
        }
    }

    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static void detonateRandomFirework(Location location) {

        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        int typeRandom = random.nextInt(4) + 1;
        Type type = Type.BALL;

        if (typeRandom == 2) type = Type.BALL_LARGE;
        if (typeRandom == 3) type = Type.BURST;
        if (typeRandom == 4) type = Type.CREEPER;
        if (typeRandom == 5) type = Type.STAR;

        int firstRandom = random.nextInt(17) + 1;
        int secondRandom = random.nextInt(17) + 1;
        Color firstColor = getColor(firstRandom);
        Color secondColor = getColor(secondRandom);

        FireworkEffect effect = FireworkEffect.builder()
                .flicker(false)
                .withColor(firstColor)
                .withFade(secondColor)
                .with(type)
                .trail(random.nextBoolean())
                .build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }

    public static Color getColor(int index) {
        Color color = null;

        if (index == 1) color = Color.AQUA;
        if (index == 2) color = Color.BLACK;
        if (index == 3) color = Color.BLUE;
        if (index == 4) color = Color.FUCHSIA;
        if (index == 5) color = Color.GRAY;
        if (index == 6) color = Color.GREEN;
        if (index == 7) color = Color.LIME;
        if (index == 8) color = Color.MAROON;
        if (index == 9) color = Color.NAVY;
        if (index == 10) color = Color.OLIVE;
        if (index == 11) color = Color.ORANGE;
        if (index == 12) color = Color.PURPLE;
        if (index == 13) color = Color.RED;
        if (index == 14) color = Color.SILVER;
        if (index == 15) color = Color.TEAL;
        if (index == 16) color = Color.WHITE;
        if (index == 17) color = Color.YELLOW;

        return color;
    }

    public static BlockFace getCardinalDirection(Location loc) {
        double rotation = (loc.getYaw() - 90.0F) % 360.0F;

        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 45.0D))
            return BlockFace.WEST;
        if ((45.0D <= rotation) && (rotation < 135.0D))
            return BlockFace.NORTH;
        if ((135.0D <= rotation) && (rotation < 225.0D))
            return BlockFace.EAST;
        if ((225.0D <= rotation) && (rotation < 315.0D))
            return BlockFace.SOUTH;
        if ((315.0D <= rotation) && (rotation < 360.0D)) {
            return BlockFace.WEST;
        }
        return null;
    }
}