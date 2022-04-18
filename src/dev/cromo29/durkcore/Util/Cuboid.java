package dev.cromo29.durkcore.Util;

import com.avaje.ebean.validation.NotNull;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable {

    private static final Set<Material> AIR_SET;
    protected final String worldName;
    protected final int x1;
    protected final int y1;
    protected final int z1;
    protected final int x2;
    protected final int y2;
    protected final int z2;

    public Cuboid(Location l1, Location l2) {
        if (!l1.getWorld().equals(l2.getWorld())) {
            throw new IllegalArgumentException("Locations must be on the same world");
        } else {
            this.worldName = l1.getWorld().getName();
            this.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
            this.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
            this.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
            this.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
            this.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
            this.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        }
    }

    public Cuboid(Location l1) {
        this(l1, l1);
    }

    public Cuboid(Cuboid other) {
        this(other.getWorld().getName(), other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.worldName = world.getName();
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    private Cuboid(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    public Cuboid(Map<String, Object> map) {
        this.worldName = (String) map.get("worldName");
        this.x1 = (Integer) map.get("x1");
        this.x2 = (Integer) map.get("x2");
        this.y1 = (Integer) map.get("y1");
        this.y2 = (Integer) map.get("y2");
        this.z1 = (Integer) map.get("z1");
        this.z2 = (Integer) map.get("z2");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();

        map.put("worldName", worldName);
        map.put("x1", x1);
        map.put("y1", y1);
        map.put("z1", z1);
        map.put("x2", x2);
        map.put("y2", y2);
        map.put("z2", z2);

        return map;
    }

    public Location getLowerNE() {
        return new Location(getWorld(), x1, y1, z1);
    }

    public Location getUpperSW() {
        return new Location(getWorld(), x2, y2, z2);
    }

    public List<Block> getBlocks() {
        Iterator<Block> blockI = iterator();
        List<Block> copy = new ArrayList<>();

        while (blockI.hasNext()) {
            copy.add(blockI.next());
        }

        return copy;
    }

    public Location getCenter() {
        int x1 = getUpperX() + 1;
        int y1 = getUpperY() + 1;
        int z1 = getUpperZ() + 1;

        return new Location(getWorld(), (double) getLowerX() + (double) (x1 - getLowerX()) / 2.0D,
                (double) getLowerY() + (double) (y1 - getLowerY()) / 2.0D,
                (double) getLowerZ() + (double) (z1 - getLowerZ()) / 2.0D);
    }

    public World getWorld() {
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            throw new IllegalStateException("World '" + worldName + "' is not loaded");
        } else {
            return world;
        }
    }

    public int getSizeX() {
        return x2 - x1 + 1;
    }

    public int getSizeY() {
        return y2 - y1 + 1;
    }

    public int getSizeZ() {
        return z2 - z1 + 1;
    }

    public int getLowerX() {
        return x1;
    }

    public int getLowerY() {
        return y1;
    }

    public int getLowerZ() {
        return z1;
    }

    public int getUpperX() {
        return x2;
    }

    public int getUpperY() {
        return y2;
    }

    public int getUpperZ() {
        return z2;
    }

    public Block[] corners() {
        Block[] res = new Block[8];
        World world = getWorld();
        res[0] = world.getBlockAt(x1, y1, z1);
        res[1] = world.getBlockAt(x1, y1, z2);
        res[2] = world.getBlockAt(x1, y2, z1);
        res[3] = world.getBlockAt(x1, y2, z2);
        res[4] = world.getBlockAt(x2, y1, z1);
        res[5] = world.getBlockAt(x2, y1, z2);
        res[6] = world.getBlockAt(x2, y2, z1);
        res[7] = world.getBlockAt(x2, y2, z2);
        return res;
    }

    public Cuboid expand(CuboidDirection dir, int amount) {
        switch (dir) {
            case North:
                return new Cuboid(worldName, x1 - amount, y1, z1, x2, y2, z2);
            case South:
                return new Cuboid(worldName, x1, y1, z1, x2 + amount, y2, z2);
            case East:
                return new Cuboid(worldName, x1, y1, z1 - amount, x2, y2, z2);
            case West:
                return new Cuboid(worldName, x1, y1, z1, x2, y2, z2 + amount);
            case Down:
                return new Cuboid(worldName, x1, y1 - amount, z1, x2, y2, z2);
            case Up:
                return new Cuboid(worldName, x1, y1, z1, x2, y2 + amount, z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public Cuboid shift(CuboidDirection dir, int amount) {
        return expand(dir, amount).expand(dir.opposite(), -amount);
    }

    public Cuboid outset(CuboidDirection dir, int amount) {
        Cuboid cuboid;
        switch (dir) {
            case Horizontal:
                cuboid = expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount)
                        .expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount);
                break;
            case Vertical:
                cuboid = expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
                break;
            case Both:
                cuboid = outset(CuboidDirection.Horizontal, amount).outset(CuboidDirection.Vertical, amount);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }

        return cuboid;
    }

    public Cuboid inset(CuboidDirection dir, int amount) {
        return outset(dir, -amount);
    }

    public boolean contains(int x, int y, int z) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    public boolean contains(Block b) {
        return contains(b.getLocation());
    }

    public boolean contains(Location l) {
        return worldName.equals(l.getWorld().getName()) && contains(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public int getVolume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }

    public byte getAverageLightLevel() {
        long total = 0L;
        int n = 0;
        Iterator blockIterator = iterator();

        while (blockIterator.hasNext()) {
            Block block = (Block) blockIterator.next();

            if (block.isEmpty()) {
                total += block.getLightLevel();
                ++n;
            }
        }

        return n > 0 ? (byte) ((int) (total / (long) n)) : 0;
    }

    public Cuboid contract() {
        return contract(CuboidDirection.Down).contract(CuboidDirection.South).contract(CuboidDirection.East)
                .contract(CuboidDirection.Up).contract(CuboidDirection.North).contract(CuboidDirection.West);
    }

    public Cuboid contract(CuboidDirection dir) {
        Cuboid face = getFace(dir.opposite());

        switch (dir) {
            case North:
                while (face.containsOnly(AIR_SET) && face.getLowerX() > getLowerX()) {
                    face = face.shift(CuboidDirection.North, 1);
                }

                return new Cuboid(worldName, x1, y1, z1, face.getUpperX(), y2, z2);
            case South:
                while (face.containsOnly(AIR_SET) && face.getUpperX() < getUpperX()) {
                    face = face.shift(CuboidDirection.South, 1);
                }

                return new Cuboid(worldName, face.getLowerX(), y1, z1, x2, y2, z2);
            case East:
                while (face.containsOnly(AIR_SET) && face.getLowerZ() > getLowerZ()) {
                    face = face.shift(CuboidDirection.East, 1);
                }

                return new Cuboid(worldName, x1, y1, z1, x2, y2, face.getUpperZ());
            case West:
                while (face.containsOnly(AIR_SET) && face.getUpperZ() < getUpperZ()) {
                    face = face.shift(CuboidDirection.West, 1);
                }

                return new Cuboid(worldName, x1, y1, face.getLowerZ(), x2, y2, z2);
            case Down:
                while (face.containsOnly(AIR_SET) && face.getLowerY() > getLowerY()) {
                    face = face.shift(CuboidDirection.Down, 1);
                }

                return new Cuboid(worldName, x1, y1, z1, x2, face.getUpperY(), z2);
            case Up:
                while (face.containsOnly(AIR_SET) && face.getUpperY() < getUpperY()) {
                    face = face.shift(CuboidDirection.Up, 1);
                }

                return new Cuboid(worldName, x1, face.getLowerY(), z1, x2, y2, z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public Cuboid getFace(CuboidDirection dir) {
        switch (dir) {
            case North:
                return new Cuboid(worldName, x1, y1, z1, x1, y2, z2);
            case South:
                return new Cuboid(worldName, x2, y1, z1, x2, y2, z2);
            case East:
                return new Cuboid(worldName, x1, y1, z1, x2, y2, z1);
            case West:
                return new Cuboid(worldName, x1, y1, z2, x2, y2, z2);
            case Down:
                return new Cuboid(worldName, x1, y1, z1, x2, y1, z2);
            case Up:
                return new Cuboid(worldName, x1, y2, z1, x2, y2, z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public boolean containsOnly(Set<Material> mats) {
        Iterator blockIterator = iterator();

        Block block;
        do {
            if (!blockIterator.hasNext()) {
                return true;
            }

            block = (Block) blockIterator.next();
        } while (mats.contains(block.getType()));

        return false;
    }

    public Cuboid getBoundingCuboid(Cuboid other) {
        if (other == null) {
            return this;
        } else {
            int xMin = Math.min(getLowerX(), other.getLowerX());
            int yMin = Math.min(getLowerY(), other.getLowerY());
            int zMin = Math.min(getLowerZ(), other.getLowerZ());
            int xMax = Math.max(getUpperX(), other.getUpperX());
            int yMax = Math.max(getUpperY(), other.getUpperY());
            int zMax = Math.max(getUpperZ(), other.getUpperZ());

            return new Cuboid(worldName, xMin, yMin, zMin, xMax, yMax, zMax);
        }
    }

    public Block getRelativeBlock(int x, int y, int z) {
        return this.getWorld().getBlockAt(x1 + x, y1 + y, z1 + z);
    }

    public Block getRelativeBlock(World w, int x, int y, int z) {
        return w.getBlockAt(x1 + x, y1 + y, z1 + z);
    }

    public List<Chunk> getChunks() {
        List<Chunk> res = Lists.newArrayList();
        World world = getWorld();
        int x1 = getLowerX() & -16;
        int x2 = getUpperX() & -16;
        int z1 = getLowerZ() & -16;
        int z2 = getUpperZ() & -16;

        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                res.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }

        return res;
    }

    public @NotNull Iterator<Block> iterator() {
        return new CuboidIterator(getWorld(), x1, y1, z1, x2, y2, z2);
    }

    public Cuboid clone() {
        return new Cuboid(this);
    }

    public String toString() {
        return "Cuboid: " + worldName + "," + x1 + "," + y1 + "," + z1 + "=>" + x2 + "," + y2 + "," + z2;
    }

    static {
        AIR_SET = new HashSet<>(Arrays.asList(Material.AIR));
    }

    public enum CuboidDirection {
        North, East,
        South, West,
        Up, Down,
        Horizontal, Vertical,
        Both, Unknown;

        CuboidDirection() {
        }

        public CuboidDirection opposite() {
            switch (this) {
                case North:
                    return South;
                case South:
                    return North;
                case East:
                    return West;
                case West:
                    return East;
                case Down:
                    return Up;
                case Up:
                    return Down;
                case Horizontal:
                    return Vertical;
                case Vertical:
                    return Horizontal;
                case Both:
                    return Both;
                default:
                    return Unknown;
            }
        }
    }

    public class CuboidIterator implements Iterator<Block> {

        private World w;
        private int baseX;
        private int baseY;
        private int baseZ;
        private int x;
        private int y;
        private int z;
        private int sizeX;
        private int sizeY;
        private int sizeZ;

        public CuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
            this.w = w;
            this.baseX = x1;
            this.baseY = y1;
            this.baseZ = z1;
            this.sizeX = Math.abs(x2 - x1) + 1;
            this.sizeY = Math.abs(y2 - y1) + 1;
            this.sizeZ = Math.abs(z2 - z1) + 1;
            this.x = this.y = this.z = 0;
        }

        public boolean hasNext() {
            return x < sizeX && y < sizeY && z < sizeZ;
        }

        public Block next() {
            Block block = w.getBlockAt(baseX + x, baseY + y, baseZ + z);

            if (++x >= sizeX) {
                x = 0;
                if (++y >= sizeY) {
                    y = 0;
                    ++z;
                }
            }

            return block;
        }

        public void remove() {
        }
    }
}
