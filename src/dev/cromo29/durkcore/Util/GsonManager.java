package dev.cromo29.durkcore.Util;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.cromo29.durkcore.SpecificUtils.ItemUtil;
import dev.cromo29.durkcore.SpecificUtils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class GsonManager {

    private String name;
    private File location;
    private File file;
    private final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
    private Map<String, Object> data;

    public void purgeData() {
        if (data != null) data.clear();
    }

    public Map<String, Object> cloneData() {
        return data != null ? Maps.newHashMap(data) : null;
    }

    public List<String> getDataPaths() {
        return data != null ? Lists.newArrayList(data.keySet()) : null;
    }

    public List<Object> getDataValues() {
        return data != null ? Lists.newArrayList(data.values()) : null;
    }

    public GsonManager(File location, String name) {
        this.name = name.replace(".json", "") + ".json";
        this.location = location;
        file = new File(this.location, this.name);
    }

    public GsonManager(Plugin plugin, String name) {
        this.name = name.replace(".json", "") + ".json";
        location = plugin.getDataFolder();
        file = new File(location, this.name);
    }

    public GsonManager(String location, String name) {
        this.name = name.replace(".json", "") + ".json";
        this.location = new File(location);
        file = new File(this.location, this.name);
    }

    public GsonManager(File location, File name) {
        this.name = name.getName().replace(".json", "") + ".json";
        this.location = location;
        file = new File(this.location, this.name);
    }

    public GsonManager(Plugin plugin, File name) {
        this.name = name.getName().replace(".json", "") + ".json";
        location = plugin.getDataFolder();
        file = new File(location, this.name);
    }

    public GsonManager(String location, File name) {
        this.name = name.getName().replace(".json", "") + ".json";
        this.location = new File(location);
        file = new File(this.location, this.name);
    }

    public GsonManager prepareGson() {
        try {
            file = new File(location, name);

            if (!location.exists()) location.mkdirs();

            if (!file.exists()) file.createNewFile();

            load();
            return this;
        } catch (Exception exception) {
            print("<c>Nao foi possivel criar o arquivo: " + name, "<c>Diretorio: " + location.getAbsolutePath());
            exception.printStackTrace();
            return this;
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public GsonManager load() {
        try {
            data = Maps.newHashMap();
            Map<String, Object> update = gson.fromJson(Files.newBufferedReader(file.toPath()), data.getClass());

            if (update != null && !update.isEmpty()) data = update;

        } catch (Exception exception) {
            print("<c>Nao foi possivel carregar o arquivo: " + name, "<c>Diretorio: " + location.getAbsolutePath());
            exception.printStackTrace();
        }
        return this;
    }

    public GsonManager save() {
        if (data == null) prepareGson();

        try {
            Files.write(file.toPath(), Collections.singletonList(gson.toJson(data)), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (Exception exception) {
            print("<c>Nao foi possivel salvar o arquivo: " + name, "<c>Diretorio: " + location.getAbsolutePath());
            exception.printStackTrace();
        }
        return this;
    }

    public void delete() {
        if (!file.delete()) file.deleteOnExit();
    }

    public File[] getFiles() {
        return location.listFiles();
    }

    public List<File> getFilesList() {
        return Arrays.asList(Objects.requireNonNull(location.listFiles()));
    }

    public GsonManager reload() {
        save();
        load();
        return this;
    }

    public String getLocationAtFile() {
        return location.getAbsolutePath();
    }

    public Path getLocationPath() {
        return location.toPath();
    }

    public File getFile() {
        return file;
    }

    public Path getLocationPathAtFile() {
        return file.toPath();
    }

    public Map<String, Object> getValues() {
        return data;
    }

    public Map<String, Object> getSections() {
        return data;
    }

    public boolean contains(String path) {
        if (data == null) prepareGson();

        return data.containsKey(path);
    }

    public List<Location> getLocationListOr(String path, List<Location> or) {
        return contains(path) ? get(path).asLocationList() : or;
    }

    public Location getLocationOr(String path, Location or) {
        return contains(path) ? get(path).asLocation() : or;
    }

    public double getDoubleOr(String path, double or) {
        return contains(path) ? get(path).asDouble() : or;
    }

    public int getIntOr(String path, int or) {
        return contains(path) ? get(path).asInt() : or;
    }

    public <T> T getOr(String path, T or) {
        return contains(path) ? (T) get(path).getValue() : or;
    }

    public ItemStack getItemStackOr(String path, ItemStack or) {
        return contains(path) ? get(path).asItemStack() : or;
    }

    public List<ItemStack> getItemStackListOr(String path, List<ItemStack> or) {
        return contains(path) ? get(path).asItemStackList() : or;
    }

    public boolean getBooleanOr(String path, boolean bool) {
        return contains(path) ? get(path).asBoolean() : bool;
    }

    public GettingValue get(String path) {
        if (data == null) prepareGson();

        return new GettingValue(data.get(path));
    }

    public List<String> getStringList(String path) {
        return (new GettingValue(data.get(path))).asList();
    }

    public String convertClassInString(Object clazz, Type stopType) {
        return gson.toJson(clazz, stopType);
    }

    public Object convertStringInClass(String data, Type stopType) {
        return gson.fromJson(data, stopType);
    }

    public List<String> getSection(String path) {
        if (data == null) prepareGson();

        List<String> section = new ArrayList<>();

        Iterator<String> paths = data.keySet().iterator();
        while (paths.hasNext()) {
            String iteratorPath = paths.next();

            if (iteratorPath.startsWith(path + ".")) { // se for o path q eu qro

                String sectionPath = iteratorPath.substring(path.length() + 1); // cortei o path

                if (sectionPath.contains(".")) { // se tiver ponto
                    String finalPath = sectionPath.substring(0, sectionPath.indexOf("."));

                    if (!section.contains(finalPath)) section.add(finalPath); // eu corto até antes do ponto
                } else {
                    if (!section.contains(sectionPath)) section.add(sectionPath); // se n tiver ponto eu ponho tudo
                }
            }
        }

        return section;

    }

    public GsonManager addValuesCaseNotFound(String path, Object value, Object... pathValues) {
        if (!contains(path)) put(path, value);

        if (pathValues != null) {
            Iterator<Object> i = Arrays.asList(pathValues).iterator();

            while (i.hasNext()) {
                String iPath = (String) i.next();
                Object iValue = i.next();

                if (!contains(iPath)) put(iPath, iValue);
            }
        }

        save();
        return this;
    }

    public GsonManager put(String path, Object value) {
        if (data == null) prepareGson();

        data.put(path, value);

        return this;
    }

    public GsonManager remove(String path) {
        if (data == null) prepareGson();

        data.remove(path);

        return this;
    }

    public GsonManager removeAll(String path) {
        Lists.newArrayList(data.keySet()).forEach(dataPath -> {
            if (dataPath.startsWith(path + ".")) data.remove(dataPath);
        });

        return this;
    }

    public boolean containsSection(String path) {

        for (String dataPath : data.keySet())
            if (dataPath.startsWith(path + ".")) return true;

        return true;
    }

    public GsonManager putLocation(String path, Location value) {
        return put(path, serealizeLocationFull(value));
    }

    public GsonManager putLocations(String path, List<Location> value) {
        List<String> serializedLocations = Lists.newArrayList();

        value.forEach((loc) -> serializedLocations.add(serealizeLocationFull(loc)));

        return put(path, serializedLocations);
    }

    public GsonManager put(String path, List<Location> value) {
        List<String> format = new ArrayList<>();
        int size = value.size();
        int loop = 0;

        while (loop < size) {
            Location loc = value.get(loop);

            format.add(serealizeLocationFull(loc));

            loop++;
        }

        return put(path, format);
    }

    public GsonManager putItems(String path, List<ItemStack> value) {
        List<String> format = new ArrayList<>();
        int size = value.size();
        int loop = 0;

        while (loop < size) {
            ItemStack item = value.get(loop);

            if (item != null) format.add(ItemUtil.toString(item));

            loop++;
        }

        return this.put(path, format);
    }

    public GsonManager putItem(String path, ItemStack value) {
        return put(path, ItemUtil.toString(value));
    }

    public GsonManager put(String path, ItemStack value) {
        data.put(path, ItemUtil.toString(value));
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String serealizeLocationFull(Location loc) {
        return LocationUtil.serializeLocation(loc);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static Location unserealizeLocationFull(String s) {
        return LocationUtil.unserializeLocation(s);
    }

    public GsonManager put(String path, Object... value) {
        data.put(path, value);

        return this;
    }

    public GsonManager put(String path, String property, String value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);

        return this;
    }

    public GsonManager put(String path, String property, Integer value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);

        return this;
    }

    public GsonManager put(String path, String property, Double value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);
        return this;
    }

    public GsonManager put(String path, String property, Long value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);

        return this;
    }

    public GsonManager put(String path, String property, Byte value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);

        return this;
    }

    public GsonManager put(String path, String property, Float value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);

        return this;
    }

    public GsonManager put(String path, String property, Boolean value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);

        return this;
    }

    public GsonManager put(String path, String property, Short value) {
        JsonObject json = null;

        if (data.containsKey(path)) json = (JsonObject) data.get(path);

        if (json == null) json = new JsonObject();

        json.addProperty(property, value);
        data.put(path, json);

        return this;
    }

    void print(Object... o) {
        Arrays.asList(o).forEach((obj) -> {
            TXT.print(obj + "");
        });
    }

    public static class GettingValue {

        private Object obj;

        public GettingValue(Object obj) {
            this.obj = obj;
        }

        public String asString() {
            return this.obj + "";
        }

        public List<Location> asLocationList() {
            try {
                List<Location> locs = new ArrayList<>();
                List<String> brute = asList();
                int size = brute.size();
                int loop = 0;

                while (loop < size) {
                    locs.add(unserealizeLocationFull(brute.get(loop)));
                    loop++;
                }

                return locs;
            } catch (Exception exception) {
                return null;
            }
        }

        public List<ItemStack> asItemStackList() {
            try {
                List<ItemStack> items = new ArrayList<>();
                List<String> brute = asList();
                int size = brute.size();
                int loop = 0;

                while (loop < size) {
                    items.add(ItemUtil.fromString(brute.get(loop)));
                    loop++;
                }

                return items;
            } catch (Exception exception) {
                return null;
            }
        }

        public Location asLocation() {
            try {
                String get = asString();
                String[] getSplited = get.split("@");
                String w = getSplited[0];
                double x = Double.parseDouble(getSplited[1]);
                double y = Double.parseDouble(getSplited[2]);
                double z = Double.parseDouble(getSplited[3]);
                float yaw = Float.parseFloat(getSplited[4]);
                float pitch = Float.parseFloat(getSplited[5]);

                return new Location(Bukkit.getWorld(w), x, y, z, yaw, pitch);
            } catch (Exception exception) {
                return null;
            }
        }

        public <K, V> Map<K, V> asMap() {
            return obj == null ? Maps.newHashMap() : (Map<K, V>) obj;
        }

        public <E> List<E> asList() {
            return obj == null ? Lists.newArrayList() : (List<E>) obj;
        }

        public ItemStack asItemStack() {
            return obj == null ? null : ItemUtil.fromString(asString());
        }

        public Boolean asBoolean() {
            return obj == null ? null : Boolean.parseBoolean(asString());
        }

        public Integer asInt() {
            return obj == null ? 0 : asDouble().intValue();
        }

        public Double asDouble() {
            return obj == null ? 0 : Double.parseDouble(asString());
        }

        public Long asLong() {
            return obj == null ? 0 : asDouble().longValue();
        }

        public Byte asByte() {
            return obj == null ? 0 : asDouble().byteValue();
        }

        public Float asFloat() {
            return obj == null ? 0 : asDouble().floatValue();
        }

        public Short asShort() {
            return obj == null ? 0 : asDouble().shortValue();
        }

        public JsonObject asProperty() {
            return obj == null ? null : (JsonObject) obj;
        }

        public Object getValue() {
            return obj;
        }

        public BigDecimal asBigDecimal() {
            return obj == null ? null : BigDecimal.valueOf(asDouble());
        }
    }
}
