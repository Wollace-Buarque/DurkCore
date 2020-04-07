package dev.cromo29.durkcore.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import dev.cromo29.durkcore.API.DurkPlugin;
import dev.cromo29.durkcore.SpecificUtils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ConfigManager {

    private String fileName;

    public ConfigManager(String location, String fileName) {
        this.fileName = fileName;
        try {
            File fileLocation = new File(location);
            file = new File(fileLocation, fileName);

            if (!fileLocation.exists())
                fileLocation.mkdirs();

            if (!this.file.exists())
                this.file.createNewFile();


            load();
        } catch (Exception e) {
            TXT.print("<c>Nao foi possivel criar o arquivo: " + fileName, "<c>Diretorio: " + this.file.getAbsolutePath());
        }
    }

    private File file;
    private FileConfiguration fileConfiguration;

    public ConfigManager(DurkPlugin plugin, String fileName) {
        this.fileName = fileName;

        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        file = new File(plugin.getDataFolder(), fileName);

        if (!new File(plugin.getDataFolder(), fileName).exists()) plugin.saveResource(fileName, false);

        if (!file.exists()) {
            try {
                file.createNewFile();
                fileConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                fileConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            } catch (Exception ignored) {
            }
        }
    }

    public boolean delete() {
        try {
            file.deleteOnExit();
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contains(String s) {
        return fileConfiguration.contains(s);
    }


    public File getFile() {
        return file;
    }


    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public boolean load() {

        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean save() {
        try {
            fileConfiguration.save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reload() {
        try {
            save();
            load();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setMap(String mapName, Map map) {
        try {

            for (Object ob : map.keySet())
                set(mapName + "." + ob, map.get(ob));

            return save();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<Object, Object> getMap(String mapName) {
        Map<Object, Object> map = new HashMap<>();

        if (!contains(mapName)) return map;

        for (String path : getConfigurationSection(mapName))
            map.put(path, getString(mapName + "." + path));

        return map;
    }

    public boolean set(String path, Object key) {
        try {

            fileConfiguration.set(path, key);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setString(String path, String key) {
        try {
            fileConfiguration.set(path, key);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setItemStack(String path, ItemStack item) {
        try {
            fileConfiguration.set(path, ItemUtil.toString(item));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ItemStack getItemStack(String path) {
        try {
            return ItemUtil.fromJson(fileConfiguration.getString(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean remove(String path) {
        try {
            this.fileConfiguration.set(path, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object get(String path) {
        try {
            return fileConfiguration.get(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getString(String path) {
        try {
            return fileConfiguration.getString(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getInt(String path) {
        try {
            return fileConfiguration.getInt(path);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getDouble(String path) {
        try {
            return fileConfiguration.getDouble(path);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0D;
        }
    }

    public boolean getBoolean(String path) {
        try {
            return fileConfiguration.getBoolean(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getStringList(String path) {
        try {
            return fileConfiguration.getStringList(path);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Set<String> getConfigurationSection(String path) {
        try {
            ConfigurationSection toR = fileConfiguration.getConfigurationSection(path);
            Set<String> to = new HashSet<>();

            if (toR != null)
                to = toR.getKeys(false);

            return to;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public long getLong(String path) {
        try {
            return fileConfiguration.getLong(path);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public boolean setLocation(String path, Location loc) {
        return set(path, loc.getWorld().getName() + "@" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ() + "@" + loc.getYaw() + "@" + loc.getPitch());
    }

    public Location getLocation(String path) {
        try {
            String get = getString(path);
            String[] getSplited = get.split("@");
            String w = getSplited[0];
            double x = Double.parseDouble(getSplited[1]);
            double y = Double.parseDouble(getSplited[2]);
            double z = Double.parseDouble(getSplited[3]);
            float ya = Float.parseFloat(getSplited[4]);
            float p = Float.parseFloat(getSplited[5]);
            return new Location(Bukkit.getWorld(w), x, y, z, ya, p);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setLocationList(String path, List<Location> locs) {
        List<String> format = new ArrayList<>();
        int size = locs.size();
        int loop = 0;
        while (loop < size) {
            Location loc = locs.get(loop);
            format.add(serealizeLocationFull(loc));
            loop++;
        }
        return set(path, format);
    }

    public List<Location> getLocationList(String path) {
        try {
            List<Location> locs = new ArrayList<>();
            List<String> brute = getStringList(path);
            int size = brute.size();
            int loop = 0;
            while (loop < size) {
                locs.add(unserealizeLocationFull(brute.get(loop)));
                loop++;
            }
            return locs;
        } catch (Exception e) {
            return null;
        }
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }


    public static String serealizeLocation(Location loc) {
        return loc.getWorld().getName() + "@" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ();
    }

    public static Location unserealizeLocation(String s) {
        try {
            String[] getSplited = s.split("@");
            String w = getSplited[0];
            double x = Double.parseDouble(getSplited[1]);
            double y = Double.parseDouble(getSplited[2]);
            double z = Double.parseDouble(getSplited[3]);
            return new Location(Bukkit.getWorld(w), x, y, z);
        } catch (Exception e) {
            return null;
        }
    }

    public static String serealizeLocationFull(Location loc) {
        return loc.getWorld().getName() + "@" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ() + "@" + loc.getYaw() + "@" + loc.getPitch();
    }

    public static Location unserealizeLocationFull(String s) {
        try {
            String[] getSplited = s.split("@");
            String w = getSplited[0];
            double x = Double.parseDouble(getSplited[1]);
            double y = Double.parseDouble(getSplited[2]);
            double z = Double.parseDouble(getSplited[3]);
            float ya = Float.parseFloat(getSplited[4]);
            float p = Float.parseFloat(getSplited[5]);
            return new Location(Bukkit.getWorld(w), x, y, z, ya, p);
        } catch (Exception e) {
            return null;
        }
    }
}
