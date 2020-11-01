package dev.cromo29.durkcore.Util;

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
        this.worldName = (String)map.get("worldName");
        this.x1 = (Integer)map.get("x1");
        this.x2 = (Integer)map.get("x2");
        this.y1 = (Integer)map.get("y1");
        this.y2 = (Integer)map.get("y2");
        this.z1 = (Integer)map.get("z1");
        this.z2 = (Integer)map.get("z2");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("worldName", this.worldName);
        map.put("x1", this.x1);
        map.put("y1", this.y1);
        map.put("z1", this.z1);
        map.put("x2", this.x2);
        map.put("y2", this.y2);
        map.put("z2", this.z2);
        return map;
    }

    public Location getLowerNE() {
        return new Location(this.getWorld(), this.x1, this.y1, this.z1);
    }

    public Location getUpperSW() {
        return new Location(this.getWorld(), this.x2, this.y2, this.z2);
    }

    public List<Block> getBlocks() {
        Iterator<Block> blockI = this.iterator();
        ArrayList copy = new ArrayList();

        while(blockI.hasNext()) {
            copy.add(blockI.next());
        }

        return copy;
    }

    public Location getCenter() {
        int x1 = this.getUpperX() + 1;
        int y1 = this.getUpperY() + 1;
        int z1 = this.getUpperZ() + 1;
        return new Location(this.getWorld(), (double)this.getLowerX() + (double)(x1 - this.getLowerX()) / 2.0D, (double)this.getLowerY() + (double)(y1 - this.getLowerY()) / 2.0D, (double)this.getLowerZ() + (double)(z1 - this.getLowerZ()) / 2.0D);
    }

    public World getWorld() {
        World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            throw new IllegalStateException("World '" + this.worldName + "' is not loaded");
        } else {
            return world;
        }
    }

    public int getSizeX() {
        return this.x2 - this.x1 + 1;
    }

    public int getSizeY() {
        return this.y2 - this.y1 + 1;
    }

    public int getSizeZ() {
        return this.z2 - this.z1 + 1;
    }

    public int getLowerX() {
        return this.x1;
    }

    public int getLowerY() {
        return this.y1;
    }

    public int getLowerZ() {
        return this.z1;
    }

    public int getUpperX() {
        return this.x2;
    }

    public int getUpperY() {
        return this.y2;
    }

    public int getUpperZ() {
        return this.z2;
    }

    public Block[] corners() {
        Block[] res = new Block[8];
        World w = this.getWorld();
        res[0] = w.getBlockAt(this.x1, this.y1, this.z1);
        res[1] = w.getBlockAt(this.x1, this.y1, this.z2);
        res[2] = w.getBlockAt(this.x1, this.y2, this.z1);
        res[3] = w.getBlockAt(this.x1, this.y2, this.z2);
        res[4] = w.getBlockAt(this.x2, this.y1, this.z1);
        res[5] = w.getBlockAt(this.x2, this.y1, this.z2);
        res[6] = w.getBlockAt(this.x2, this.y2, this.z1);
        res[7] = w.getBlockAt(this.x2, this.y2, this.z2);
        return res;
    }

    public Cuboid expand(CuboidDirection dir, int amount) {
        switch(dir) {
            case North:
                return new Cuboid(this.worldName, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2);
            case South:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2);
            case East:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2);
            case West:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount);
            case Down:
                return new Cuboid(this.worldName, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2);
            case Up:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public Cuboid shift(CuboidDirection dir, int amount) {
        return this.expand(dir, amount).expand(dir.opposite(), -amount);
    }

    public Cuboid outset(CuboidDirection dir, int amount) {
        Cuboid c;
        switch(dir) {
            case Horizontal:
                c = this.expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount).expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount);
                break;
            case Vertical:
                c = this.expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
                break;
            case Both:
                c = this.outset(CuboidDirection.Horizontal, amount).outset(CuboidDirection.Vertical, amount);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }

        return c;
    }

    public Cuboid inset(CuboidDirection dir, int amount) {
        return this.outset(dir, -amount);
    }

    public boolean contains(int x, int y, int z) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
    }

    public boolean contains(Block b) {
        return this.contains(b.getLocation());
    }

    public boolean contains(Location l) {
        return this.worldName.equals(l.getWorld().getName()) && this.contains(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public int getVolume() {
        return this.getSizeX() * this.getSizeY() * this.getSizeZ();
    }

    public byte getAverageLightLevel() {
        long total = 0L;
        int n = 0;
        Iterator var4 = this.iterator();

        while(var4.hasNext()) {
            Block b = (Block)var4.next();
            if (b.isEmpty()) {
                total += b.getLightLevel();
                ++n;
            }
        }

        return n > 0 ? (byte)((int)(total / (long)n)) : 0;
    }

    public Cuboid contract() {
        return this.contract(CuboidDirection.Down).contract(CuboidDirection.South).contract(CuboidDirection.East).contract(CuboidDirection.Up).contract(CuboidDirection.North).contract(CuboidDirection.West);
    }

    public Cuboid contract(CuboidDirection dir) {
        Cuboid face = this.getFace(dir.opposite());
        switch(dir) {
            case North:
                while(face.containsOnly(AIR_SET) && face.getLowerX() > this.getLowerX()) {
                    face = face.shift(CuboidDirection.North, 1);
                }

                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, face.getUpperX(), this.y2, this.z2);
            case South:
                while(face.containsOnly(AIR_SET) && face.getUpperX() < this.getUpperX()) {
                    face = face.shift(CuboidDirection.South, 1);
                }

                return new Cuboid(this.worldName, face.getLowerX(), this.y1, this.z1, this.x2, this.y2, this.z2);
            case East:
                while(face.containsOnly(AIR_SET) && face.getLowerZ() > this.getLowerZ()) {
                    face = face.shift(CuboidDirection.East, 1);
                }

                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, face.getUpperZ());
            case West:
                while(face.containsOnly(AIR_SET) && face.getUpperZ() < this.getUpperZ()) {
                    face = face.shift(CuboidDirection.West, 1);
                }

                return new Cuboid(this.worldName, this.x1, this.y1, face.getLowerZ(), this.x2, this.y2, this.z2);
            case Down:
                while(face.containsOnly(AIR_SET) && face.getLowerY() > this.getLowerY()) {
                    face = face.shift(CuboidDirection.Down, 1);
                }

                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, face.getUpperY(), this.z2);
            case Up:
                while(face.containsOnly(AIR_SET) && face.getUpperY() < this.getUpperY()) {
                    face = face.shift(CuboidDirection.Up, 1);
                }

                return new Cuboid(this.worldName, this.x1, face.getLowerY(), this.z1, this.x2, this.y2, this.z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public Cuboid getFace(CuboidDirection dir) {
        switch(dir) {
            case North:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
            case South:
                return new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
            case East:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
            case West:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
            case Down:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
            case Up:
                return new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    public boolean containsOnly(Set<Material> mats) {
        Iterator var2 = this.iterator();

        Block b;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            b = (Block)var2.next();
        } while(mats.contains(b.getType()));

        return false;
    }

    public Cuboid getBoundingCuboid(Cuboid other) {
        if (other == null) {
            return this;
        } else {
            int xMin = Math.min(this.getLowerX(), other.getLowerX());
            int yMin = Math.min(this.getLowerY(), other.getLowerY());
            int zMin = Math.min(this.getLowerZ(), other.getLowerZ());
            int xMax = Math.max(this.getUpperX(), other.getUpperX());
            int yMax = Math.max(this.getUpperY(), other.getUpperY());
            int zMax = Math.max(this.getUpperZ(), other.getUpperZ());
            return new Cuboid(this.worldName, xMin, yMin, zMin, xMax, yMax, zMax);
        }
    }

    public Block getRelativeBlock(int x, int y, int z) {
        return this.getWorld().getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    public Block getRelativeBlock(World w, int x, int y, int z) {
        return w.getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    public List<Chunk> getChunks() {
        List<Chunk> res = Lists.newArrayList();
        World w = this.getWorld();
        int x1 = this.getLowerX() & -16;
        int x2 = this.getUpperX() & -16;
        int z1 = this.getLowerZ() & -16;
        int z2 = this.getUpperZ() & -16;

        for(int x = x1; x <= x2; x += 16) {
            for(int z = z1; z <= z2; z += 16) {
                res.add(w.getChunkAt(x >> 4, z >> 4));
            }
        }

        return res;
    }

    public Iterator<Block> iterator() {
        return new CuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public Cuboid clone() {
        return new Cuboid(this);
    }

    public String toString() {
        return "Cuboid: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2;
    }

    static {
        AIR_SET = new HashSet(Arrays.asList(Material.AIR));
    }

    public enum CuboidDirection {
        North,
        East,
        South,
        West,
        Up,
        Down,
        Horizontal,
        Vertical,
        Both,
        Unknown;

        CuboidDirection() {
        }

        public CuboidDirection opposite() {
            switch(this) {
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
            return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
        }

        public Block next() {
            Block b = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
            if (++this.x >= this.sizeX) {
                this.x = 0;
                if (++this.y >= this.sizeY) {
                    this.y = 0;
                    ++this.z;
                }
            }

            return b;
        }

        public void remove() {
        }
    }
}
