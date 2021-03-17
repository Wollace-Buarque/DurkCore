package dev.cromo29.durkcore.SpecificUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;
import dev.cromo29.durkcore.Translation.EntityTypeName;
import dev.cromo29.durkcore.Util.JsonBuilder;
import dev.cromo29.durkcore.Util.RU;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemUtil {

    public static ItemStack fromLegibleString(String text) {
        String[] split = text.split(" ");
        short durability = -1;
        String data = split[0];

        if (data.contains(":")) {
            String[] datasplit = data.split(":");
            data = datasplit[0];
            durability = Short.parseShort(datasplit[1]);
        }

        Material material = data.matches("[0-9]+")
                ? Material.getMaterial(Integer.parseInt(data))
                : Material.getMaterial(data.toUpperCase());

        // Throw exception if the material provided was wrong
        if (material == null) throw new IllegalArgumentException("Invalid material " + data);

        int amount;
        try {
            amount = split.length == 1 ? 1 : Integer.parseInt(split[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid amount \"" + split[1] + "\"", ex);
        }
        ItemStack item = new ItemStack(material, amount, (short) Math.max(0, durability));
        ItemMeta meta = item.getItemMeta();

        // No meta data was provided, we can return here
        if (split.length < 3) return item;

        // Go through all the item meta specified
        for (int i = 2; i < split.length; i++) {
            String[] information = split[i].split(":");

            // Data, name, or lore has been specified
            switch (information[0].toLowerCase()) {
                case "name":
                    // Replace '_' with spaces
                    String name = information[1].replace(' ', ' ');
                    meta.setDisplayName(TXT.parse(name));
                    break;
                case "lore":
                    // If lore was specified 2x for some reason, don't overwrite
                    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                    String[] loreLines = information[1].split("\\|");

                    // Format all the lines and add them as lore
                    for (String line : loreLines) {
                        line = line.replace('_', ' '); // Replace '_' with space
                        lore.add(TXT.parse(line));
                    }

                    meta.setLore(lore);
                    break;
                case "data":
                    short dataValue = Short.parseShort(information[1]);
                    item.setDurability(dataValue);
                default:
                    // Try parsing enchantment if it was nothing else
                    Enchantment ench = Enchantment.getByName(information[0].toUpperCase());
                    int level = Integer.parseInt(information[1]);

                    if (ench != null) {
                        meta.addEnchant(ench, level, true);
                    } else {
                        throw new IllegalArgumentException("Invalid enchantment " + information[0]);
                    }
                    break;
            }
        }

        // Set the meta and return created item line
        item.setItemMeta(meta);
        return item;
    }

    private enum MetaType {
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
            Object nmsItemStack = getObfuscationClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);

            getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            outputStream = new ByteArrayOutputStream();

            getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, OutputStream.class).invoke(null, nbtTagCompound, outputStream);
        } catch (Exception ignored) {
        }
        return BaseEncoding.base64().encode(outputStream.toByteArray());
    }

    public static ItemStack fromString(String itemStackString) {
        if (itemStackString == null) return null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStackString));

        Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
        Class<?> nmsItemStackClass = getNMSClass("ItemStack");
        Object nbtTagCompound = null;
        ItemStack itemStack = null;

        try {
            nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", InputStream.class).invoke(null, inputStream);
            Object craftItemStack = nmsItemStackClass.getMethod("createStack", nbtTagCompoundClass).invoke(null, nbtTagCompound);

            itemStack = (ItemStack) getObfuscationClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
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
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
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
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
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

                } else if (meta instanceof BannerMeta) {

                    json.put("metaType", MetaType.BANNER.name());

                    // NOVO - TESTAR

                    json.put("baseColor", ((BannerMeta) meta).getBaseColor());
                    json.put("patterns", ((BannerMeta) meta).getPatterns());

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

                } else if (meta instanceof FireworkEffectMeta) {

                    json.put("metaType", MetaType.FIREWORK_EFFECT.name());

                } else if (meta instanceof LeatherArmorMeta) {

                    json.put("leatherArmorRed", ((LeatherArmorMeta) meta).getColor().getRed());
                    json.put("leatherArmorBlue", ((LeatherArmorMeta) meta).getColor().getBlue());
                    json.put("leatherArmorGreen", ((LeatherArmorMeta) meta).getColor().getGreen());
                    json.put("metaType", MetaType.LEATHER_ARMOR.name());

                } else if (meta instanceof PotionMeta) {

//                    Map<String, String> customEffects = Maps.newHashMap();
//                    PotionMeta potionMeta = (PotionMeta) meta;
//                    potionMeta.getCustomEffects().forEach(potionEffect -> customEffects.put(potionEffect.getType().getName(), potionEffect.getAmplifier()+"-"+potionEffect.getDuration()));
//                    json.put("mainEffect",
//                            potionMeta.getBasePotionData().getType().getEffectType() == null ? "none" : potionMeta.getBasePotionData().getType().getEffectType().getName()
//                    );
//                    json.put("customEffects", customEffects);
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
                } catch (Exception e) {
                }
                json.put("unbreakable", unbreakable);

            }

            if (!json.getCurrentMap().containsKey("metaType")) json.put("metaType", MetaType.ITEM.name());
            Map<String, String> enchantmentMap = Maps.newHashMap();
            item.getEnchantments().forEach((ench, level) -> enchantmentMap.put(ench.getName(), level + ""));
            json.put("enchantments", enchantmentMap);

            return json.build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }

    }

    @Deprecated
    public static ItemStack fromJson(String itemJson) {
        try {

            Map<String, Object> jsonMap = new JsonBuilder(itemJson).getCurrentMap();

            Material stopType = Material.valueOf(jsonMap.get("material").toString());
            int amount = Double.valueOf(jsonMap.get("amount").toString()).intValue();
            short durability = Double.valueOf(jsonMap.get("durability").toString()).shortValue();

            MetaType metaType = MetaType.valueOf(jsonMap.get("metaType").toString());

            ItemStack makeItem = new ItemStack(stopType, amount, durability);

            if (jsonMap.containsKey("lore")) {

                ItemMeta meta = makeItem.getItemMeta();

                switch (metaType) {
                    case BOOK:

                        BookMeta bookMeta = (BookMeta) meta;
                        bookMeta.setAuthor(jsonMap.get("author").toString());
                        bookMeta.setPages((List<String>) jsonMap.get("pages"));
                        bookMeta.setTitle(jsonMap.get("title").toString());
                        break;

                    case ITEM:
                        break;
                    case SKULL:

                        ((SkullMeta) meta).setOwner(jsonMap.get("owner").toString());
                        break;

                    case BANNER: // FIXED?

                        BannerMeta bannerMeta = (BannerMeta) meta;

                        bannerMeta.setBaseColor(DyeColor.valueOf(jsonMap.get("baseColor").toString()));
                        bannerMeta.setPatterns((List<Pattern>) jsonMap.get("patterns"));

                        break;

                    case POTION: // TODO

//                        PotionMeta potionMeta = (PotionMeta) meta;
//                        if (!jsonMap.get("mainEffect").equals("none")) potionMeta.setMainEffect(
//                                PotionEffectType.getByName(jsonMap.get("mainEffect").toString())
//                        );
//                        Map<String, String> customEffects = (Map<String, String>) jsonMap.get("customEffects");
//                        customEffects.forEach((potionEffect, data) -> {
//                            String[] splitData = data.split("-");
//                            potionMeta.addCustomEffect(new PotionEffect(
//                                    PotionEffectType.getByName(potionEffect),
//                                    Double.valueOf(splitData[0]).intValue(),
//                                    Double.valueOf(splitData[1]).intValue()
//                            ), false);
//                        });


                        break;

                    case LEATHER_ARMOR:

                        ((LeatherArmorMeta) meta).setColor(Color.deserialize(
                                MapUtil.getMap(
                                        "RED", Double.valueOf(jsonMap.get("leatherArmorRed").toString()).intValue(),
                                        "BLUE", Double.valueOf(jsonMap.get("leatherArmorBlue").toString()).intValue(),
                                        "GREEN", Double.valueOf(jsonMap.get("leatherArmorGreen").toString()).intValue()
                                )
                        ));
                        break;

                    case FIREWORK_EFFECT: // TODO
                        // a
                        break;
                    case ENCHANTMENT_STORAGE:

                        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
                        ((Map<String, String>) jsonMap.get("storedEnchantments")).forEach((enchantment, level) ->
                                enchantmentStorageMeta.addStoredEnchant(Enchantment.getByName(enchantment), Double.valueOf(level).intValue(), false)
                        );
                        break;

                }

                meta.setLore((List<String>) jsonMap.get("lore"));

                List<ItemFlag> itemFlags = Lists.newArrayList();
                ((List<String>) jsonMap.get("itemFlags")).forEach(itemFlag -> itemFlags.add(ItemFlag.valueOf(itemFlag)));
                meta.addItemFlags(itemFlags.toArray(new ItemFlag[]{}));

                ((Map<String, String>) jsonMap.get("enchantments")).forEach((ench, level) -> makeItem.addUnsafeEnchantment(Enchantment.getByName(ench), Double.valueOf(level).intValue()));

                if (!jsonMap.get("displayName").equals(stopType.name()))
                    meta.setDisplayName(jsonMap.get("displayName").toString());

                try {
                    meta.spigot().setUnbreakable(Boolean.valueOf(jsonMap.get("unbreakable").toString()));
                } catch (Exception ignored) {
                }
                makeItem.setItemMeta(meta);

            }

            return makeItem;

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static Class<?> NBTTagCompoundClass;
    private static Class<?> NBTTagListClass;
    private static Class<?> NBTTagStringClass;
    private static Class<?> NBTTagIntClass;
    private static Class<?> NBTTagDoubleClass;
    private static Method asNMSCopy;
    private static Method asBukkitCopy;
    private static Method asCraftMirror;
    private static Method getRepairCost;
    private static Method setRepairCost;
    private static Method setNBTTagCompound;
    private static Method hasNBTTagCompound;
    private static Method getNBTTagCompound;
    private static Method hasKey;
    private static Method getString;
    private static Method getBoolean;
    private static Method setString;
    private static Method setBoolean;
    private static Method getNBTBase;
    private static Method getNBTList;
    private static Method hasTag;
    private static Method setNBTBaseCompound;
    private static Method addNBTBaseTag;
    private static Method createTag;
    private static Method craftMagicNumbers$getItem;


    public static boolean isTool(Material material) {
        String name = material.name();

        return name.contains("_SWORD")
                || name.contains("_PICKAXE")
                || name.contains("_AXE")
                || name.contains("_SPADE")
                || name.contains("_SHOVEL")
                || name.contains("_HOE");
    }

    public static boolean isDiamondTool(Material material) {
        return material.name().contains("DIAMOND_") && isTool(material);
    }

    public static boolean isIronTool(Material material) {
        return material.name().contains("IRON_") && isTool(material);
    }

    public static boolean isStoneTool(Material material) {
        return material.name().contains("STONE_") && isTool(material);
    }

    public static boolean isGoldenTool(Material material) {
        return material.name().contains("GOLD_") && isTool(material);
    }

    public static boolean isWoodenTool(Material material) {
        return material.name().contains("WOOD_") && isTool(material);
    }

    public static boolean isArmor(Material material) {
        String name = material.name();
        return
                name.contains("_HELMET")
                        || name.contains("_CHESTPLATE")
                        || name.contains("_LEGGINGS")
                        || name.contains("_BOOTS");
    }

    public static boolean isDiamondArmor(Material material) {
        return material.name().contains("DIAMOND_") && isArmor(material);
    }

    public static boolean isIronArmor(Material material) {
        return material.name().contains("IRON_") && isArmor(material);
    }

    public static boolean isStoneArmor(Material material) {
        return material.name().contains("STONE_") && isArmor(material);
    }

    public static boolean isChainmailArmor(Material material) {
        return material.name().contains("CHAINMAIL_") && isArmor(material);
    }

    public static boolean isGoldenArmor(Material material) {
        return material.name().contains("GOLD_") && isArmor(material);
    }

    public static boolean isLeatherArmor(Material material) {
        return material.name().contains("LEATHER_") && isArmor(material);
    }

    public static boolean hasItem(Material material) {

        if (material == Material.ENCHANTED_BOOK) return true;
        if (material == Material.SOIL || material == Material.BURNING_FURNACE) return false;
        try {
            return craftMagicNumbers$getItem.invoke(null, material) != null;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isMaterial(String materialEnumName) {
        return tryEnumValueOf(materialEnumName.toUpperCase()) != null;
    }

    public static boolean isValidItemStack(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }

    public static Material asMaterial(String materialEnumName) {
        return tryEnumValueOf(materialEnumName.toUpperCase());
    }

    public static String translateEntityTypeName(EntityType entityType) {
        try {
            return EntityTypeName.valueOf(entityType).getName();
        } catch (Exception ignored) {
            return entityType.name();
        }
    }

    private static <T extends Enum<T>> T tryEnumValueOf(String enumName) {
        try {
            return Enum.valueOf((Class<T>) Material.class, enumName);
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack setAttributeNBT(ItemStack item, String attribute, double value, int operation) {
        int least = new Random().nextInt(8192);
        int most = new Random().nextInt(8192);

        try {
            Object NBTTagCompound;
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);

            if (hasNBTTag)
                NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
            else NBTTagCompound = NBTTagCompoundClass.newInstance();

            Object AttributeModifiers;
            Object Modifier = NBTTagCompoundClass.newInstance();
            boolean hasAttribute = (boolean) hasTag.invoke(NBTTagCompound, "AttributeModifiers");

            if (hasAttribute)
                AttributeModifiers = getNBTList.invoke(NBTTagCompound, "AttributeModifiers", 10);
            else AttributeModifiers = NBTTagListClass.newInstance();


            Object AttributeName = createTag.invoke(null, (byte) 8);
            Field fieldAttributeName = NBTTagStringClass.getDeclaredField("data");
            fieldAttributeName.setAccessible(true);
            fieldAttributeName.set(AttributeName, attribute);

            Object Name = createTag.invoke(null, (byte) 8);
            Field fieldName = NBTTagStringClass.getDeclaredField("data");
            fieldName.setAccessible(true);
            fieldName.set(Name, attribute);

            Object Amount = createTag.invoke(null, (byte) 6);
            Field fieldAmount = NBTTagDoubleClass.getDeclaredField("data");
            fieldAmount.setAccessible(true);
            fieldAmount.set(Amount, value);

            Object Operation = createTag.invoke(null, (byte) 3);
            Field fieldOperation = NBTTagIntClass.getDeclaredField("data");
            fieldOperation.setAccessible(true);
            fieldOperation.set(Operation, operation);

            Object UUIDLeast = createTag.invoke(null, (byte) 3);
            Field fieldUUIDLeast = NBTTagIntClass.getDeclaredField("data");
            fieldUUIDLeast.setAccessible(true);
            fieldUUIDLeast.set(UUIDLeast, least);

            Object UUIDMost = createTag.invoke(null, (byte) 3);
            Field fieldUUIDMost = NBTTagIntClass.getDeclaredField("data");
            fieldUUIDMost.setAccessible(true);
            fieldUUIDMost.set(UUIDMost, most);

            setNBTBaseCompound.invoke(Modifier, "AttributeName", AttributeName);
            setNBTBaseCompound.invoke(Modifier, "Name", Name);
            setNBTBaseCompound.invoke(Modifier, "Amount", Amount);
            setNBTBaseCompound.invoke(Modifier, "Operation", Operation);
            setNBTBaseCompound.invoke(Modifier, "UUIDLeast", UUIDLeast);
            setNBTBaseCompound.invoke(Modifier, "UUIDMost", UUIDMost);

            Object NBTBase = getNBTBase.invoke(Modifier);
            addNBTBaseTag.invoke(AttributeModifiers, NBTBase);
            setNBTBaseCompound.invoke(NBTTagCompound, "AttributeModifiers", AttributeModifiers);
            setNBTTagCompound.invoke(CraftItemStack, NBTTagCompound);

            return (ItemStack) asCraftMirror.invoke(null, CraftItemStack);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static boolean hasInfo(ItemStack item, String key) {
        try {
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);

            if (hasNBTTag) {
                Object NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
                return (boolean) hasKey.invoke(NBTTagCompound, key);
            }
            return false;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getInfo(ItemStack item, String key) {
        try {
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);

            if (hasNBTTag) {
                Object NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
                return getString.invoke(NBTTagCompound, key).toString();
            }

            return null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static ItemStack saveInfo(ItemStack item, String key, String value) {
        try {
            Object NBTTagCompound;
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);

            if (hasNBTTag)
                NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
            else NBTTagCompound = NBTTagCompoundClass.newInstance();

            setString.invoke(NBTTagCompound, key, value);
            setNBTTagCompound.invoke(CraftItemStack, NBTTagCompound);

            return (ItemStack) asCraftMirror.invoke(null, CraftItemStack);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static boolean isUnbreakable(ItemStack item) {
        try {
            Object NBTTagCompound;
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);

            if (hasNBTTag)
                NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
            else return false;

            return (boolean) getBoolean.invoke(NBTTagCompound, "Unbreakable");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public static ItemStack setUnbreakable(ItemStack item, boolean bool) {
        if (item.getType().getMaxDurability() != 0 && item.getDurability() != 0)
            item.setDurability((short) 0);

        try {
            Object NBTTagCompound;
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);

            if (hasNBTTag)
                NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
            else NBTTagCompound = NBTTagCompoundClass.newInstance();

            setBoolean.invoke(NBTTagCompound, "Unbreakable", bool);
            setNBTTagCompound.invoke(CraftItemStack, NBTTagCompound);

            return (ItemStack) asCraftMirror.invoke(null, CraftItemStack);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static int getRepairCost(ItemStack item) {
        try {
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            int cost = (int) getRepairCost.invoke(CraftItemStack);

            if (item.getType().getMaxDurability() != 0) {
                double durability = item.getDurability();
                double maxDurability = item.getType().getMaxDurability();
                double durabilityPercent = (durability * 100.0) / maxDurability;

                if (durabilityPercent <= 25.0) cost += 1;
                else if (durabilityPercent <= 50.0) cost += 2;
                else if (durabilityPercent <= 75.0) cost += 3;
                else if (durabilityPercent <= 100.0) cost += 4;
            }

            return cost;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return 40;
        }
    }

    public static ItemStack setRepairCost(ItemStack item, int cost) {
        try {
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            setRepairCost.invoke(CraftItemStack, cost);

            return (ItemStack) asBukkitCopy.invoke(null, CraftItemStack);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return item;
        }
    }

    static {
        try {

            craftMagicNumbers$getItem = RU.getMethod(RU.getBukkitClass("util.CraftMagicNumbers"), "getItem", Material.class);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {

            // Item Classes
            Class<?> itemStackClass = RU.getNMSClass("ItemStack");
            Class<?> craftItemStackClass = RU.getBukkitClass("inventory.CraftItemStack");

            // NBTTag Classes
            NBTTagCompoundClass = RU.getNMSClass("NBTTagCompound");
            NBTTagListClass = RU.getNMSClass("NBTTagList");
            NBTTagStringClass = RU.getNMSClass("NBTTagString");
            NBTTagIntClass = RU.getNMSClass("NBTTagInt");
            NBTTagDoubleClass = RU.getNMSClass("NBTTagDouble");
            Class<?> NBTBaseClass = RU.getNMSClass("NBTBase");

            // Item Handle Methods
            asNMSCopy = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
            asBukkitCopy = craftItemStackClass.getDeclaredMethod("asBukkitCopy", itemStackClass);
            asCraftMirror = craftItemStackClass.getDeclaredMethod("asCraftMirror", itemStackClass);

            // Repair cost Methods
            getRepairCost = itemStackClass.getDeclaredMethod("getRepairCost");
            setRepairCost = itemStackClass.getMethod("setRepairCost", int.class);

            // Item NBTTag Methods
            getNBTTagCompound = itemStackClass.getDeclaredMethod("getTag");
            hasNBTTagCompound = itemStackClass.getDeclaredMethod("hasTag");
            setNBTTagCompound = itemStackClass.getDeclaredMethod("setTag", NBTTagCompoundClass);

            // Basic NBTTag Handle Methods
            hasKey = NBTTagCompoundClass.getDeclaredMethod("hasKey", String.class);
            getString = NBTTagCompoundClass.getDeclaredMethod("getString", String.class);
            getBoolean = NBTTagCompoundClass.getDeclaredMethod("getBoolean", String.class);
            setString = NBTTagCompoundClass.getDeclaredMethod("setString", String.class, String.class);
            setBoolean = NBTTagCompoundClass.getDeclaredMethod("setBoolean", String.class, boolean.class);

            // Advance NBTTag Handle
            getNBTBase = NBTTagCompoundClass.getDeclaredMethod("clone");
            getNBTList = NBTTagCompoundClass.getDeclaredMethod("getList", String.class, int.class);
            hasTag = NBTTagCompoundClass.getDeclaredMethod("hasKey", String.class);
            setNBTBaseCompound = NBTTagCompoundClass.getDeclaredMethod("set", String.class, NBTBaseClass);
            addNBTBaseTag = NBTTagListClass.getDeclaredMethod("add", NBTBaseClass);
            createTag = NBTBaseClass.getDeclaredMethod("createTag", byte.class);
            createTag.setAccessible(true);

        } catch (Throwable ignored) {
        }
    }

}