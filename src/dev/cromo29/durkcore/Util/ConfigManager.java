package dev.cromo29.durkcore.Util;

import dev.cromo29.durkcore.API.DurkPlugin;
import dev.cromo29.durkcore.SpecificUtils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConfigManager {

    private String fileName;
    private File file;
    private FileConfiguration fileConfiguration;

    public ConfigManager(String location, String fileName) {
        this.fileName = fileName;

        try {
            File fileLocation = new File(location);

            file = new File(fileLocation, fileName);

            if (!fileLocation.exists()) fileLocation.mkdirs();

            if (!file.exists()) file.createNewFile();

            load();
        } catch (Exception exception) {
            exception.printStackTrace();
            TXT.print("<c>Nao foi possivel criar o arquivo: " + fileName, "<c>Diretorio: " + this.file.getAbsolutePath());
        }

    }

    public ConfigManager(DurkPlugin plugin, String fileName) {
        this.fileName = fileName;

        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        file = new File(plugin.getDataFolder(), fileName);

   //     if (!file.exists()) plugin.saveResource(fileName, false);

        if (!file.exists()) {
            try {
              //  file.createNewFile();
                plugin.saveResource(fileName, false);
                fileConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

                save();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            try {
                fileConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            } catch (Exception ignored) {
            }
        }

    }

    public ConfigManager(JavaPlugin plugin, String fileName) {
        this.fileName = fileName;

        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        file = new File(plugin.getDataFolder(), fileName);

        //  if (!new File(plugin.getDataFolder(), fileName).exists()) plugin.saveResource(fileName, false);

        if (!file.exists()) {
            try {
               // file.createNewFile();
                plugin.saveResource(fileName, false);
                fileConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

                save();
            } catch (Exception exception) {
                exception.printStackTrace();
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
        } catch (Exception exception) {
            exception.printStackTrace();
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
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean save() {
        try {
            fileConfiguration.save(file);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean reload() {
        String oldFile = file.getPath().replace(fileName, "");

        try {
            file = new File(oldFile, fileName);
            fileConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean setMap(String mapName, Map map) {
        try {
            Iterator iterator = map.keySet().iterator();

            while (iterator.hasNext()) {
                Object ob = iterator.next();
                set(mapName + "." + ob, map.get(ob));
            }

            return save();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Map<Object, Object> getMap(String mapName) {
        Map<Object, Object> map = new HashMap<>();
        if (contains(mapName)) {
            Iterator iterator = getConfigurationSection(mapName).iterator();

            while (iterator.hasNext()) {
                String path = (String) iterator.next();
                map.put(path, getString(mapName + "." + path));
            }
        }
        return map;
    }

    public boolean set(String path, Object key) {
        try {
            fileConfiguration.set(path, key);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean addValuesCaseNotFound(String path, Object value, Object... pathValues) {
        try {
            if (!contains(path)) {
                fileConfiguration.set(path, value);
            }

            if (pathValues != null) {
                Iterator<Object> iterator = Arrays.asList(pathValues).iterator();

                while (iterator.hasNext()) {
                    String iPath = (String) iterator.next();
                    Object iValue = iterator.next();

                    if (!contains(iPath)) fileConfiguration.set(iPath, iValue);
                }
            }

            reload();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean setString(String path, String key) {
        try {
            fileConfiguration.set(path, key);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean setItemStack(String path, ItemStack item) {
        try {
            fileConfiguration.set(path, ItemUtil.toJson(item));
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean setItemStackS(String path, ItemStack item) {
        try {
            fileConfiguration.set(path, ItemUtil.toString(item));
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public ItemStack getItemStackS(String path) {
        try {
            return ItemUtil.fromString(fileConfiguration.getString(path));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public ItemStack getItemStack(String path) {
        try {
            return ItemUtil.fromJson(fileConfiguration.getString(path));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean remove(String path) {
        try {
            fileConfiguration.set(path, null);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Object get(String path) {
        try {
            return fileConfiguration.get(path);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getString(String path) {
        try {
            return fileConfiguration.getString(path);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public int getInt(String path) {
        try {
            return fileConfiguration.getInt(path);
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    public double getDouble(String path) {
        try {
            return fileConfiguration.getDouble(path);
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0.0;
        }
    }

    public boolean getBoolean(String path) {
        try {
            return fileConfiguration.getBoolean(path);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<String> getStringList(String path) {
        try {
            return fileConfiguration.getStringList(path);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Set<String> getConfigurationSection(String path) {
        try {
            ConfigurationSection toReturn = fileConfiguration.getConfigurationSection(path);
            Set<String> sections = new HashSet<>();

            if (toReturn != null) sections = toReturn.getKeys(false);

            return sections;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new HashSet<>();
        }
    }

    public long getLong(String path) {
        try {
            return fileConfiguration.getLong(path);
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0L;
        }
    }

    public boolean setLocation(String path, Location loc) {
        return set(path, loc.getWorld().getName() + "@" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ()
                + "@" + loc.getYaw() + "@" + loc.getPitch());
    }

    public Location getLocation(String path) {
        try {
            String get = getString(path);
            String[] getSplited = get.split("@");
            String world = getSplited[0];
            double x = Double.parseDouble(getSplited[1]);
            double y = Double.parseDouble(getSplited[2]);
            double z = Double.parseDouble(getSplited[3]);
            float yaw = Float.parseFloat(getSplited[4]);
            float pitch = Float.parseFloat(getSplited[5]);

            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        } catch (Exception exception) {
            return null;
        }
    }

    public boolean setLocationList(String path, List<Location> locs) {
        List<String> format = new ArrayList<>();

        for (Location location : locs) format.add(serealizeLocationFull(location));

        return set(path, format);
    }

    public List<Location> getLocationList(String path) {
        try {
            List<Location> locs = new ArrayList<>();
            List<String> brute = getStringList(path);

            for (String location : brute) locs.add(unserealizeLocationFull(location));

            return locs;
        } catch (Exception exception) {
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
            String world = getSplited[0];
            double x = Double.parseDouble(getSplited[1]);
            double y = Double.parseDouble(getSplited[2]);
            double z = Double.parseDouble(getSplited[3]);

            return new Location(Bukkit.getWorld(world), x, y, z);
        } catch (Exception exception) {
            return null;
        }
    }

    public static String serealizeLocationFull(Location loc) {
        return loc.getWorld().getName() + "@" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ()
                + "@" + loc.getYaw() + "@" + loc.getPitch();
    }

    public static Location unserealizeLocationFull(String s) {
        try {
            String[] getSplited = s.split("@");
            String world = getSplited[0];
            double x = Double.parseDouble(getSplited[1]);
            double y = Double.parseDouble(getSplited[2]);
            double z = Double.parseDouble(getSplited[3]);
            float yaw = Float.parseFloat(getSplited[4]);
            float pitch = Float.parseFloat(getSplited[5]);

            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        } catch (Exception exception) {
            return null;
        }
    }
}
