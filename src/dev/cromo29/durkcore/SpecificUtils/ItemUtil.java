package dev.cromo29.durkcore.SpecificUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import dev.cromo29.durkcore.Util.JsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtil {
    public enum MetaType {
        ITEM,
        SKULL,
        BANNER,
        BOOK,
        ENCHANTMENT_STORAGE,
        FIREWORK_EFFECT,
        LEATHER_ARMOR,
        POTION
    }

    public static String toString(ItemStack itemStack) {
        if (itemStack == null) return null;
        ByteArrayOutputStream outputStream = null;
        try {
            Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
            Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
            Object nmsItemStack = getObfuscationClass("inventory.CraftItemStack").getMethod("asNMSCopy", new Class[]{ItemStack.class}).invoke(null, itemStack);
            getNMSClass("ItemStack").getMethod("save", new Class[]{nbtTagCompoundClass}).invoke(nmsItemStack, nbtTagCompound);
            outputStream = new ByteArrayOutputStream();
            getNMSClass("NBTCompressedStreamTools").getMethod("a", new Class[]{nbtTagCompoundClass, OutputStream.class}).invoke(null, nbtTagCompound, outputStream);
        } catch (Exception ignored) {
        }
        return BaseEncoding.base64().encode(outputStream.toByteArray());
    }

    public static ItemStack fromString(String itemStackString) {
        if (itemStackString == null) return null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStackString));
        Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
        Class<?> nmsItemStackClass = getNMSClass("ItemStack");
        Object nbtTagCompound;
        ItemStack itemStack = null;
        try {
            nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", new Class[]{InputStream.class}).invoke(null, inputStream);
            Object craftItemStack = nmsItemStackClass.getMethod("createStack", new Class[]{nbtTagCompoundClass}).invoke(null, nbtTagCompound);
            itemStack = (ItemStack) getObfuscationClass("inventory.CraftItemStack").getMethod("asBukkitCopy", new Class[]{nmsItemStackClass}).invoke(null, new Object[]{craftItemStack});
        } catch (Exception ignored) {
        }
        return itemStack;
    }

    private static Class<?> getNMSClass(String className) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "net.minecraft.server." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nmsClass;
    }

    private static Class<?> getObfuscationClass(String className) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "org.bukkit.craftbukkit." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nmsClass;
    }

    @Deprecated
    public static String toJson(ItemStack item) {
        if (item == null) return null;


        try {
            JsonBuilder json = new JsonBuilder();

            json.put("material", item.getType().name());
            json.put("amount", item.getAmount());
            json.put("durability", item.getDurability());

            if (item.hasItemMeta()) {

                ItemMeta meta = item.getItemMeta();

                if (meta instanceof SkullMeta) {

                    json.put("owner", ((SkullMeta) meta).getOwner());
                    json.put("metaType", MetaType.SKULL.name());
                } else if (meta instanceof org.bukkit.inventory.meta.BannerMeta) {

                    json.put("metaType", MetaType.BANNER.name());
                } else if (meta instanceof BookMeta) {

                    json.put("author", ((BookMeta) meta).getAuthor());
                    json.put("pages", ((BookMeta) meta).getPages());
                    json.put("title", ((BookMeta) meta).getTitle());
                    json.put("metaType", MetaType.BOOK.name());
                } else if (meta instanceof EnchantmentStorageMeta) {

                    Map<String, String> enchantmentMap = Maps.newHashMap();
                    ((EnchantmentStorageMeta) meta).getStoredEnchants().forEach((ench, level) -> enchantmentMap.put(ench.getName(), level + ""));
                    json.put("storedEnchantments", enchantmentMap);
                    json.put("metaType", MetaType.ENCHANTMENT_STORAGE.name());
                } else if (meta instanceof org.bukkit.inventory.meta.FireworkEffectMeta) {

                    json.put("metaType", MetaType.FIREWORK_EFFECT.name());
                } else if (meta instanceof LeatherArmorMeta) {

                    json.put("leatherArmorRed", ((LeatherArmorMeta) meta).getColor().getRed());
                    json.put("leatherArmorBlue", ((LeatherArmorMeta) meta).getColor().getBlue());
                    json.put("leatherArmorGreen", ((LeatherArmorMeta) meta).getColor().getGreen());
                    json.put("metaType", MetaType.LEATHER_ARMOR.name());
                } else if (meta instanceof org.bukkit.inventory.meta.PotionMeta) {


                    json.put("metaType", MetaType.POTION.name());
                }


                json.put("lore", !meta.hasLore() ? Lists.newArrayList() : meta.getLore());

                List<String> itemFlags = Lists.newArrayList();
                meta.getItemFlags().forEach(itemFlag -> itemFlags.add(itemFlag.name()));
                json.put("itemFlags", itemFlags);

                json.put("displayName", meta.hasDisplayName() ? meta.getDisplayName() : item.getType().name());
                boolean unbreakable = false;
                try {
                    unbreakable = meta.spigot().isUnbreakable();
                } catch (Exception ignored) {
                }
                json.put("unbreakable", unbreakable);
            }


            if (!json.getCurrentMap().containsKey("metaType")) json.put("metaType", MetaType.ITEM.name());
            Map<String, String> enchantmentMap = Maps.newHashMap();
            item.getEnchantments().forEach((ench, level) -> enchantmentMap.put(ench.getName(), level + ""));
            json.put("enchantments", enchantmentMap);

            return json.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @Deprecated
    public static ItemStack fromJson(String itemJson) {
        try {
            Map<String, Object> jsonMap = (new JsonBuilder(itemJson)).getCurrentMap();

            Material type = Material.valueOf(jsonMap.get("material").toString());
            int amount = Double.valueOf(jsonMap.get("amount").toString()).intValue();
            short durability = Double.valueOf(jsonMap.get("durability").toString()).shortValue();

            MetaType metaType = MetaType.valueOf(jsonMap.get("metaType").toString());

            ItemStack makeItem = new ItemStack(type, amount, durability);

            if (jsonMap.containsKey("lore")) {
                EnchantmentStorageMeta enchantmentStorageMeta;
                BookMeta bookMeta;
                ItemMeta meta = makeItem.getItemMeta();

                switch (metaType) {

                    case BOOK:
                        bookMeta = (BookMeta) meta;
                        bookMeta.setAuthor(jsonMap.get("author").toString());
                        bookMeta.setPages((List<String>) jsonMap.get("pages"));
                        bookMeta.setTitle(jsonMap.get("title").toString());
                        break;


                    case SKULL:
                        ((SkullMeta) meta).setOwner(jsonMap.get("owner").toString());
                        break;


                    case LEATHER_ARMOR:
                        ((LeatherArmorMeta) meta).setColor(Color.deserialize(
                                MapUtil.getMap("RED",
                                        Double.valueOf(jsonMap.get("leatherArmorRed").toString()).intValue(), new Object[]{"BLUE",
                                                Double.valueOf(jsonMap.get("leatherArmorBlue").toString()).intValue(), "GREEN",
                                                Double.valueOf(jsonMap.get("leatherArmorGreen").toString()).intValue()})));
                        break;


                    case ENCHANTMENT_STORAGE:
                        enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
                        ((Map) jsonMap.get("storedEnchantments")).forEach((enchantment, level) ->
                                enchantmentStorageMeta.addStoredEnchant(Enchantment.getByName((String) enchantment), ((Double) level).intValue(), false));
                        break;
                }


                meta.setLore((List<String>) jsonMap.get("lore"));

                List<ItemFlag> itemFlags = Lists.newArrayList();
                ((List) jsonMap.get("itemFlags")).forEach(itemFlag -> itemFlags.add(ItemFlag.valueOf((String) itemFlag)));
                meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));

                ((Map) jsonMap.get("enchantments")).forEach((ench, level) -> makeItem.addUnsafeEnchantment(Enchantment.getByName((String) ench), ((Double) level).intValue()));

                if (!jsonMap.get("displayName").equals(type.name()))
                    meta.setDisplayName(jsonMap.get("displayName").toString());

                try {
                    meta.spigot().setUnbreakable(Boolean.parseBoolean(jsonMap.get("unbreakable").toString()));
                } catch (Exception ignored) {
                }
                makeItem.setItemMeta(meta);
            }


            return makeItem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}