package dev.cromo29.durkcore.Util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.cromo29.durkcore.SpecificUtils.ItemUtil;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;



public class JsonBuilder {
    public JsonBuilder() {}

    public JsonBuilder(String jsonToMap) { data = (Map<String, Object>)(new Gson()).fromJson(jsonToMap, Map.class); }

    private final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
    private Map<String, Object> data = new HashMap<>();

    public <T> T get(String key) { return (T)data.get(key); }

    public Map getAsMap(String key) { return (Map)data.get(key); }

    public List getAsList(String key) { return (List)data.get(key); }

    public Map<String, Object> getCurrentMap() { return data; }

    public ItemStack getItemStack(String key) { return ItemUtil.fromJson(data.get(key).toString()); }

    public List<ItemStack> getItemStackList(String key) {
        List<ItemStack> items = new ArrayList<>();
        List<String> brute = (List<String>)this.data.get(key);
        int size = brute.size();
        int loop = 0;
        while (loop < size) {
            items.add(ItemUtil.fromJson(brute.get(loop)));
            loop++;
        }
        return items;
    }

    public Location getLocation(String key) { return GsonManager.unserealizeLocationFull(data.get(key).toString()); }
    public List<Location> getLocationList(String key) {
        List<Location> items = new ArrayList<>();
        List<String> brute = (List<String>)this.data.get(key);
        int size = brute.size();
        int loop = 0;
        while (loop < size) {
            items.add(GsonManager.unserealizeLocationFull(brute.get(loop)));
            loop++;
        }
        return items;
    }

    public JsonBuilder put(String key, Object value) { data.put(key, value); return this; }
    public JsonBuilder remove(String key) { data.remove(key); return this; }

    public JsonBuilder putLocation(String key, Location location) { data.put(key, GsonManager.serealizeLocationFull(location)); return this; }

    public JsonBuilder putLocations(String key, List<Location> locations) {

        List<String> serializedLocations = Lists.newArrayList();
        locations.forEach(loc -> serializedLocations.add(GsonManager.serealizeLocationFull(loc)));
        data.put(key, serializedLocations);

        return this;
    }
    public JsonBuilder putItemStack(String key, ItemStack location) { this.data.put(key, ItemUtil.toJson(location)); return this; }

    public JsonBuilder putItemStackList(String key, List<ItemStack> locations) {

        List<String> serializedItems = Lists.newArrayList();
        locations.forEach(itemStack -> serializedItems.add(ItemUtil.toJson(itemStack)));
        data.put(key, serializedItems);

        return this;
    }

    public String build() { return this.gson.toJson(this.data); }

}

