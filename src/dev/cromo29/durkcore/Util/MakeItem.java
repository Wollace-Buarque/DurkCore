package dev.cromo29.durkcore.Util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.cromo29.durkcore.SpecificUtils.ItemUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class MakeItem {

    public static MakeItem getGreenSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/361e5b333c2a3868bb6a58b6674a2639323815738e77e053977419af3f77");
    }

    public static MakeItem getGraySkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/f2f085c6b3cb228e5ba81df562c4786762f3c257127e9725c77b7fd301d37");
    }

    public static MakeItem getYellowSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/14c4141c1edf3f7e41236bd658c5bc7b5aa7abf7e2a852b647258818acd70d8");
    }

    public static MakeItem getPurpleSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/593f67f9f730d42fda8de69565ea55892c5f85d9cae6dd6fcba5d26f1e7238d1");
    }

    public static MakeItem getPinkSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/3ef0c5773df560cc3fc73b54b5f08cd69856415ab569a37d6d44f2f423df20");
    }

    public static MakeItem getOrangeSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/e79add3e5936a382a8f7fdc37fd6fa96653d5104ebcadb0d4f7e9d4a6efc454");
    }

    public static MakeItem getBlueSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/bef7b6829fc48758cb25ab93f28bf794d92ace0161f809a2aadd3bb12b23014");
    }

    public static MakeItem getDBlueSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/a2cd272eeb38bf783a98a46fa1e2e8d462d852fbaaedef0dce2c1f717a2a");
    }

    public static MakeItem getAzureSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/bfaf7aab1e177ad38e51bfc19ab662149c31953a569a40caa81f7a4932069");
    }

    public static MakeItem getWhiteSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/366a5c98928fa5d4b5d5b8efb490155b4dda3956bcaa9371177814532cfc");
    }

    public static MakeItem getBlackSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/967a2f218a6e6e38f2b545f6c17733f4ef9bbb288e75402949c052189ee");
    }

    public static MakeItem getRedSkull() {
        return getCustomMakeItemSkullURL(
                "http://textures.minecraft.net/texture/5fde3bfce2d8cb724de8556e5ec21b7f15f584684ab785214add164be7624b");
    }

    private ItemStack ik;
    public static String green_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI3Y2E0NmY2YTliYjg5YTI0ZmNhZjRjYzBhY2Y1ZTgyODVhNjZkYjc1MjEzNzhlZDI5MDlhZTQ0OTY5N2YifX19";
    public static String pink_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VmMGM1NzczZGY1NjBjYzNmYzczYjU0YjVmMDhjZDY5ODU2NDE1YWI1NjlhMzdkNmQ0NGYyZjQyM2RmMjAifX19";
    public static String blue_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVmN2I2ODI5ZmM0ODc1OGNiMjVhYjkzZjI4YmY3OTRkOTJhY2UwMTYxZjgwOWEyYWFkZDNiYjEyYjIzMDE0In19fQ==";
    public static String red_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI5MzJiNjZkZWNhZWZmNmViZGM3YzViZTZiMjQ2N2FhNmYxNGI3NDYzODhhMDZhMmUxZTFhODQ2M2U5ZTEyMiJ9fX0=";
    public static String black = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY3YTJmMjE4YTZlNmUzOGYyYjU0NWY2YzE3NzMzZjRlZjliYmIyODhlNzU0MDI5NDljMDUyMTg5ZWUifX19";
    public static String gray_dark = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4ZjMyMzQ2MmZiNDM0ZTkyOGJkNjcyODYzOGM5NDRlZTNkODEyZTE2MmI5YzZiYTA3MGZjYWM5YmY5In19fQ==";
    public static String orange = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmMyZTk3MmFmYTkxMTViNmQzMjA3NWIxZjFiN2ZlZDdhYTI5YTUzNDFjMTAyNDI4ODM2MWFiZThlNjliNDYifX19";
    public static String blue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJjZDI3MmVlYjM4YmY3ODNhOThhNDZmYTFlMmU4ZDQ2MmQ4NTJmYmFhZWRlZjBkY2UyYzFmNzE3YTJhIn19fQ==";
    public static String yellow = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY0MTY4MmY0MzYwNmM1YzlhZDI2YmM3ZWE4YTMwZWU0NzU0N2M5ZGZkM2M2Y2RhNDllMWMxYTI4MTZjZjBiYSJ9fX0=";
    public static String red = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZkZTNiZmNlMmQ4Y2I3MjRkZTg1NTZlNWVjMjFiN2YxNWY1ODQ2ODRhYjc4NTIxNGFkZDE2NGJlNzYyNGIifX19";
    public static String yellow_green = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhmZmYyMmM2ZTY1NDZkMGQ4ZWI3Zjk3NjMzOTg0MDdkZDJhYjgwZjc0ZmUzZDE2YjEwYTk4M2VjYWYzNDdlIn19fQ==";
    public static String p_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU5M2E2ZjIzZjViMTEwZWMxYzdhYjY2YzQwZmJjOWI4MmY0ZjVjOTg3MjY1YWE2N2M5NWEyZDNmNGM5NyJ9fX0=";
    public static String d_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQxMWE3ZTFiYzdiOWI5NzYyM2UwNDc4MjE5YjFhMzIzODNlZTA3ZWIwZTZhNjZmNWVmOWY4NTU1OTNkZjAifX19";
    public static String t_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5N2FlNTRjMWQzZGUyY2U0YTVmMGY0OTQzOTgyNTM4YThhN2M2OWYxNzRiZTU3ZDc0YTMzZWU5YmM1ZjY3In19fQ==";
    public static String s_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY1N2JkMTg2OGRhNjczNmNjNGM0MzUzOTg5ZTZhNDU3NDM3YmRkZDM5MmM4MmMyNDhkMTQyMDcwYThkZDgzIn19fQ==";
    private static Field profileField;

    public MakeItem(Material material, byte data) {
        ik = new ItemStack(material, 1, data);
    }

    public MakeItem(Material material) {
        ik = new ItemStack(material);
    }

    public MakeItem(String owner, String name) {
        ik = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) ik.getItemMeta();

        skullMeta.setDisplayName(name);
        skullMeta.setOwner(owner);

        ik.setItemMeta(skullMeta);
    }

    public MakeItem(String owner) {
        ik = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) ik.getItemMeta();

        skullMeta.setOwner(owner);

        ik.setItemMeta(skullMeta);
    }

    public boolean isPlayerSkull() {
        return ik.getItemMeta() instanceof SkullMeta;
    }

    public void setOwner(String owner) {
        if (ik.getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) ik.getItemMeta();

            skullMeta.setOwner(owner);

            ik.setItemMeta(skullMeta);
        }
    }

    public MakeItem setData(int data) {
        ik.setDurability((short) data);
        return this;
    }

    public MakeItem setUnbreakable(boolean unbreakable) {
        ItemMeta meta = ik.getItemMeta();

        meta.spigot().setUnbreakable(unbreakable);

        ik.setItemMeta(meta);
        return this;
    }

    public MakeItem addEnchantment(Enchantment enchant, int level) {
        ik.addUnsafeEnchantment(enchant, level);
        return this;
    }

    public MakeItem removeItemFlags(ItemFlag... itemFlags) {
        ItemMeta meta = ik.getItemMeta();

        meta.removeItemFlags(itemFlags);

        ik.setItemMeta(meta);
        return this;
    }

    public MakeItem addItemFlags(ItemFlag... itemFlags) {
        ItemMeta meta = ik.getItemMeta();

        meta.addItemFlags(itemFlags);

        ik.setItemMeta(meta);
        return this;
    }

    public MakeItem hideAttributes() {
        return removeAttributes();
    }

    public MakeItem removeAttributes() {
        ItemMeta meta = ik.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);

        ik.setItemMeta(meta);
        return this;
    }

    public static ItemStack getCustomSkullURL(String url) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] data = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

        profile.getProperties().put("textures", new Property("textures", new String(data)));

        try {
            if (profileField == null)
                profileField = meta.getClass().getDeclaredField("profile");

            profileField.setAccessible(true);
            profileField.set(meta, profile);
            item.setItemMeta(meta);

            return item;
        } catch (Exception var6) {
            var6.printStackTrace();
            return new MakeItem("steve").build();
        }
    }

    public static MakeItem getCustomMakeItemSkullURL(String url) {
        return new MakeItem(getCustomSkullURL(url));
    }

    public static ItemStack getCustomSkullTexture(String texture) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            if (profileField == null)
                profileField = meta.getClass().getDeclaredField("profile");

            profileField.setAccessible(true);
            profileField.set(meta, profile);
            item.setItemMeta(meta);

            return item;
        } catch (Exception var5) {
            var5.printStackTrace();
            return new MakeItem("steve").build();
        }
    }

    public static MakeItem getCustomMakeItemSkullTexture(String texture) {
        return new MakeItem(getCustomSkullTexture(texture));
    }

    public static ItemStack getCustomSkull(String url) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        if (url == null || url.isEmpty())
            return skull;

        if (!url.startsWith("http://textures.minecraft.net/texture/"))
            url = "http://textures.minecraft.net/texture/" + url;

        try {
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(url.getBytes()), null);

            profile.getProperties().put("textures", new Property("textures", new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()))));

            Field profileField = skullMeta.getClass().getDeclaredField("profile");

            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
            skull.setItemMeta(skullMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return skull;
    }

    public MakeItem of(String url) {
        return new MakeItem(getCustomSkull(url));
    }

    public MakeItem setTexture(String url) {
        return new MakeItem(getCustomSkull(url));
    }

    public MakeItem(ItemStack ik) {
        this.ik = ik.clone();
    }

    public MakeItem setAmount(int amount) {
        ik.setAmount(amount);
        return this;
    }

    public MakeItem setName(String name) {
        ItemMeta im = ik.getItemMeta();

        im.setDisplayName(TXT.parse(name));

        ik.setItemMeta(im);
        return this;
    }

    public MakeItem setMaterial(Material material) {
        ik.setType(material);
        return this;
    }

    @Deprecated
    public MakeItem setMaterial(int id) {
        ik.setTypeId(id);
        return this;
    }

    public static boolean hasGlow(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasEnchant(new MakeItem.Glow());
    }

    public MakeItem setGlow(boolean glow) {
        MakeItem.Glow enchant = new MakeItem.Glow();
        ItemMeta itemMeta = ik.getItemMeta();

        if (glow)
            itemMeta.addEnchant(enchant, 1, true);
        else itemMeta.removeEnchant(enchant);

        ik.setItemMeta(itemMeta);

        return this;
    }

    public boolean isLeatherArmor() {
        return ik.getItemMeta() instanceof LeatherArmorMeta;
    }

    public MakeItem setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta meta = (LeatherArmorMeta) ik.getItemMeta();

            meta.setColor(color);

            ik.setItemMeta(meta);
        } catch (Exception ignored) {
        }
        return this;
    }

    public MakeItem setColor(Color color) {
        return setLeatherArmorColor(color);
    }

    public void clearLore() {
        ItemMeta im = ik.getItemMeta();

        im.setLore(new ArrayList<>());

        ik.setItemMeta(im);
    }

    public List<String> getLore() {
        ItemMeta im = ik.getItemMeta();

        return im.hasLore() ? im.getLore() : new ArrayList<>();
    }

    public MakeItem setLore(List<String> lore) {
        ItemMeta im = ik.getItemMeta();
        List<String> loreList = new ArrayList<>();

        for (String text : lore) loreList.add(TXT.parse(text));

        im.setLore(loreList);

        ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLoreList(String... name) {
        for (String text : name) addLore(text);

        return this;
    }

    public MakeItem addLore(List<String> lore) {
        ItemMeta im = ik.getItemMeta();
        List<String> loreList = im.getLore() == null ? new ArrayList<>() : im.getLore();

        for (String text : lore)
            loreList.add(TXT.parse(text));

        im.setLore(loreList);

        ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLore(String lore) {
        ItemMeta im = ik.getItemMeta();
        List<String> loreList = im.getLore() == null ? new ArrayList<>() : im.getLore();

        if (im.hasLore())
            loreList = im.getLore();

        if (lore.contains("/n")) {
            String[] arrayOfString;
            int j = (arrayOfString = lore.split("/n")).length;

            for (int i = 0; i < j; i++) {
                String x = arrayOfString[i];
                loreList.add(TXT.parse(x));
            }

        } else loreList.add(TXT.parse(lore));

        im.setLore(loreList);

        ik.setItemMeta(im);
        return this;
    }

    public MakeItem remLore(int amount) {
        ItemMeta im = ik.getItemMeta();
        List<String> loreLIst = new ArrayList<>();

        if (im.hasLore())
            loreLIst = im.getLore();

        for (int i = 0; i < amount; i++)
            if (!loreLIst.isEmpty())
                loreLIst.remove(loreLIst.size() - 1);

        im.setLore(loreLIst);

        ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLore(String[] lore) {
        ItemMeta im = ik.getItemMeta();
        List<String> loreList = new ArrayList<>();

        if (im.hasLore())
            loreList = im.getLore();

        for (String text : lore)
            loreList.add(TXT.parse(text));

        im.setLore(loreList);

        ik.setItemMeta(im);
        return this;
    }

    /**
     * créditos a: https://github.com/TheMFjulio/MFLib/blob/master/src/main/java/com/mateus/mflib/util/ItemBuilder.java
     */
    public MakeItem setNBTTag(String key, String value) {
        try {
            Object nmsCopy = NMSReflect.getCraftBukkitClass("inventory", "CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, ik);
            Object nbtTagCompound = NMSReflect.getNMSClass("NBTTagCompound").getConstructor().newInstance();
            boolean b = nmsCopy.getClass().getMethod("getTag").invoke(nmsCopy) != null;
            Object nbtTag = b ? nmsCopy.getClass().getMethod("getTag").invoke(nmsCopy) : nbtTagCompound;
            Constructor nbsString = NMSReflect.getNMSClass("NBTTagString").getConstructor(String.class);
            nbtTag.getClass().getMethod("set", String.class, NMSReflect.getNMSClass("NBTBase"))
                    .invoke(nbtTag, key, nbsString.newInstance(value));
            nmsCopy.getClass().getMethod("setTag", NMSReflect.getNMSClass("NBTTagCompound")).invoke(nmsCopy, nbtTag);
            this.ik = (ItemStack) NMSReflect.getCraftBukkitClass("inventory", "CraftItemStack").getMethod("asBukkitCopy", NMSReflect.getNMSClass("ItemStack"))
                    .invoke(null, nmsCopy);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * créditos a: https://github.com/TheMFjulio/MFLib/blob/master/src/main/java/com/mateus/mflib/util/NBTGetter.java
     */
    public static String getNBTTag(ItemStack item, String key) {
        try {
            Object nmsCopy = NMSReflect.getCraftBukkitClass("inventory", "CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            if (nmsCopy.getClass().getMethod("getTag").invoke(nmsCopy) != null) {
                Object tagCompound = nmsCopy.getClass().getMethod("getTag").invoke(nmsCopy);
                return (String) tagCompound.getClass().getMethod("getString", String.class).invoke(tagCompound, key);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemStack build() {
        return ik;
    }

    public String buildSerialized() {
        return ItemUtil.toString(build());
    }

    public static boolean checkIsSimilar(ItemStack ik1, ItemStack ik2) {
        if (ik1.getType() == ik2.getType()) {
            if (ik1.hasItemMeta() && ik2.hasItemMeta()) {
                if (ik1.getItemMeta().hasDisplayName() && ik2.getItemMeta().hasDisplayName()) {
                    if (ik1.getItemMeta().getDisplayName().equals(ik2.getItemMeta().getDisplayName())) {
                        if (ik1.getItemMeta().hasLore() && ik2.getItemMeta().hasLore()) {
                            return ik1.getItemMeta().getLore().equals(ik2.getItemMeta().getLore());

                        } else return false;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }

    public static boolean checkIsSimilar(ItemStack ik1, ItemStack ik2, boolean use_lore) {
        if (ik1.getType() == ik2.getType()) {
            if (ik1.hasItemMeta() && ik2.hasItemMeta()) {
                if (ik1.getItemMeta().hasDisplayName() && ik2.getItemMeta().hasDisplayName()) {
                    if (ik1.getItemMeta().getDisplayName().equals(ik2.getItemMeta().getDisplayName())) {
                        if (use_lore) {
                            if (ik1.getItemMeta().hasLore() && ik2.getItemMeta().hasLore()) {
                                return ik1.getItemMeta().getLore().equals(ik2.getItemMeta().getLore());

                            } else return false;
                        } else return true;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }

    private static class Glow extends Enchantment {
        public Glow() {
            super(200);
        }

        public boolean canEnchantItem(ItemStack i) {
            return false;
        }

        public boolean conflictsWith(Enchantment e) {
            return false;
        }

        public EnchantmentTarget getItemTarget() {
            return null;
        }

        public int getMaxLevel() {
            return 1;
        }

        public String getName() {
            return "Glow I";
        }

        public int getStartLevel() {
            return 1;
        }
    }
}
