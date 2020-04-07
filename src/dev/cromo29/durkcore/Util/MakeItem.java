package dev.cromo29.durkcore.Util;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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

public class MakeItem {

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

    public MakeItem(Material material) {
        ik = new ItemStack(material);
    }

    public MakeItem(Material material, byte data) {
        ik = new ItemStack(material, 1, data);
    }

    public MakeItem addItemFlag(ItemFlag flag) {
        ItemMeta im = ik.getItemMeta();
        im.addItemFlags(flag);
        ik.setItemMeta(im);
        return this;
    }

    public void setOwner(String owner) {
        if (ik.getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) ik.getItemMeta();
            skullMeta.setOwner(owner);
            ik.setItemMeta(skullMeta);
        }

    }

    public MakeItem(String owner) {
        ik = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) ik.getItemMeta();
        skullMeta.setOwner(owner);
        ik.setItemMeta(skullMeta);
    }

    public MakeItem setData(int data) {
        ik.setDurability((short) data);
        return this;
    }

    public MakeItem(String owner, String name) {
        ik = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) ik.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwner(owner);
        ik.setItemMeta(skullMeta);
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

    public MakeItem removeAttributes() {
        ItemMeta meta = ik.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
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
            return null;
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
            return null;
        }
    }

    public static MakeItem getCustomMakeItemSkullTexture(String texture) {
        return new MakeItem(getCustomSkullTexture(texture));
    }

    public MakeItem(ItemStack ik) {
        this.ik = ik.clone();
    }

    public MakeItem setAmout(int amount) {
        ik.setAmount(amount);
        return this;
    }

    public MakeItem setName(String name) {
        ItemMeta im = ik.getItemMeta();
        im.setDisplayName(TXT.parse(name));
        ik.setItemMeta(im);
        return this;
    }

    public MakeItem setMaterial(Material m) {
        ik.setType(m);
        return this;
    }

    @Deprecated
    public MakeItem setMaterial(int m) {
        ik.setTypeId(m);
        return this;
    }

    public static boolean hasGlow(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasEnchant(new MakeItem.Glow());
    }

    public MakeItem setGlow(boolean glow) {
        MakeItem.Glow ench = new MakeItem.Glow();
        ItemMeta itemMeta;

        if (glow) {
            itemMeta = ik.getItemMeta();
            itemMeta.addEnchant(ench, 1, true);
            ik.setItemMeta(itemMeta);
        } else {
            itemMeta = ik.getItemMeta();
            itemMeta.removeEnchant(ench);
            ik.setItemMeta(itemMeta);
        }

        return this;
    }

    public MakeItem setColor(Color color) {
        try {
            LeatherArmorMeta meta = (LeatherArmorMeta) ik.getItemMeta();
            meta.setColor(color);
            ik.setItemMeta(meta);
        } catch (Exception ignored) {}

        return this;
    }

    public MakeItem setLore(List<String> lore) {
        ItemMeta im = ik.getItemMeta();
        ArrayList<String> lorer = new ArrayList();
        Iterator var4 = lore.iterator();

        while (var4.hasNext()) {
            String r = (String) var4.next();
            lorer.add(TXT.parse(r));
        }

        im.setLore(lorer);
        ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLoreList(String... name) {
        String[] arrayOfString = name;
        int j = name.length;

        for (int i = 0; i < j; ++i) {
            String s = arrayOfString[i];
            addLore(s);
        }

        return this;
    }

    public MakeItem addLore(List<String> lore) {
        ItemMeta im = ik.getItemMeta();
        List<String> lorer = im.getLore() == null ? Lists.newArrayList() : im.getLore();
        Iterator var4 = lore.iterator();

        while (var4.hasNext()) {
            String r = (String) var4.next();
            lorer.add(TXT.parse(r));
        }

        im.setLore(lorer);
        ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLore(String lore) {
        ItemMeta im = ik.getItemMeta();
        List<String> lorer = im.getLore() == null ? Lists.newArrayList() : im.getLore();
        if (im.hasLore()) {
            lorer = im.getLore();
        }

        if (lore.contains("/n")) {
            String[] arrayOfString;
            int j = (arrayOfString = lore.split("/n")).length;

            for (int i = 0; i < j; ++i) {
                String x = arrayOfString[i];
                lorer.add(TXT.parse(x));
            }
        } else
            lorer.add(TXT.parse(lore));


        im.setLore(lorer);
        ik.setItemMeta(im);
        return this;
    }

    public MakeItem remLore(int amount) {
        ItemMeta im = ik.getItemMeta();
        List<String> lorer = new ArrayList();
        if (im.hasLore()) {
            lorer = im.getLore();
        }

        for (int i = 0; i < amount; ++i)
            if (!lorer.isEmpty())
                ((List) lorer).remove(lorer.size() - 1);


        im.setLore(lorer);
        ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLore(String[] lore) {
        ItemMeta im = ik.getItemMeta();
        List<String> lorer = new ArrayList();
        if (im.hasLore()) {
            lorer = im.getLore();
        }

        String[] arrayOfString = lore;
        int j = lore.length;

        for (int i = 0; i < j; ++i) {
            String x = arrayOfString[i];
            lorer.add(TXT.parse(x));
        }

        im.setLore(lorer);
        ik.setItemMeta(im);
        return this;
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
