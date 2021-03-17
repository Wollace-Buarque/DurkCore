package dev.cromo29.durkcore.Util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.cromo29.durkcore.DurkCore;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TXT {

    private static Pattern parsePattern;
    private static Pattern unparsePattern;
    private static Map<String, String> colors = Maps.newHashMap();
    private static List<String> unparse = Lists.newArrayList();

    public static double compareStrings(String stringA, String stringB) {
        return org.apache.commons.lang3.StringUtils.getJaroWinklerDistance(stringA, stringB) * 100.0D;
    }

    public static BukkitTask runLater(Plugin plugin, long after, Runnable runnable) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, after);
    }

    public static BukkitTask runLaterAsynchronously(Plugin plugin, long after, Runnable runnable) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, after);
    }

    public static BukkitTask run(Plugin plugin, long wait, long after, Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, wait, after);
    }

    public static BukkitTask run(Plugin plugin, Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static BukkitTask runAsynchronously(Plugin plugin, long wait, long after, Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, wait, after);
    }

    public static BukkitTask runAsynchronously(Plugin plugin, Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void print(String message) {
        Bukkit.getConsoleSender().sendMessage(parse(message));
    }

    public static void print(String message, Object... s) {
        print(parse(message, s));
    }

    public static String createString(Object[] args, int start) {
        return createString(args, start, " ");
    }

    public static String createString(String[] args, int start) {
        return createString(args, start, " ");
    }

    public static String createString(String[] args, int start, String glue) {
        return createString(args, start, "", glue);
    }

    public static String createString(Object[] args, int start, String glue) {
        return createString(args, start, "", glue);
    }

    public static String createString(Object[] args, int start, String prefix, String suffix) {
        StringBuilder string = new StringBuilder();

        for (int x = start; x < args.length; ++x) {
            string.append(prefix);
            string.append(args[x]);

            if (x != args.length - 1) {
                string.append(suffix);
            }

        }
        return string.toString();
    }

    public int getColumn(int slot) {
        int toReturn = 0;

        if (slot < 9)
            toReturn += 1;
        else toReturn = (slot % 9) + 1;

        return toReturn;
    }

    public String[] getList(String... values) {
        List<String> list = new ArrayList<>();

        for (String s : values) {
            list.add(parse(s));
        }

        return list.toArray(new String[0]);
    }

    public static boolean isValidInt(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan"))
            return false;
        else {
            try {
                Integer.parseInt(toCheck);
                return true;
            } catch (Exception exception) {
                return false;
            }
        }
    }

    public static boolean isValidDouble(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan"))
            return false;
        else {
            try {
                Double.parseDouble(toCheck);
                return true;
            } catch (Exception exception) {
                return false;
            }
        }
    }

    public static boolean isValidBoolean(String toCheck) {
        try {
            Boolean.parseBoolean(toCheck);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static int getEmptySlots(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] cont = inventory.getContents();
        int i = 0;

        for (ItemStack item : cont)
            if (item != null && item.getType() != Material.AIR)
                i++;

        return 36 - i;
    }

    public static boolean isEmptyInventory(Player target) {
        for (ItemStack itens : target.getInventory().getContents())
            if (itens != null
                    || target.getInventory().getHelmet() != null
                    || target.getInventory().getChestplate() != null
                    || target.getInventory().getLeggings() != null
                    || target.getInventory().getBoots() != null)
                return false;
        return true;
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title == null || subtitle == null) return;

        // TIMES

        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);

        // SUBTITLE

        IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + parse(subtitle) + "\"}");
        PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);

        // TITLE

        IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + parse(title) + "\"}");
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);

        // SEND

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packetPlayOutTimes);
        connection.sendPacket(packetPlayOutSubTitle);
        connection.sendPacket(packetPlayOutTitle);
    }

    public static void sendActionBar(Player player, String actionBar) {

        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + parse(actionBar) + "\"}"), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendActionBar(Player player, String actionBar, int stayInSeconds) {
        sendActionBar(player, actionBar);

        new BukkitRunnable() {
            int i = stayInSeconds;

            @Override
            public void run() {
                i--;

                if (i < 0) {
                    cancel();

                    return;
                }

                sendActionBar(player, actionBar);
            }
        }.runTaskTimer(DurkCore.durkCore, 0, 20L);
    }

    public static String parse(String string) {
        if (string == null) return null;

        StringBuffer ret = new StringBuffer();
        Matcher matcher = parsePattern.matcher(string);

        while (matcher.find()) {
            matcher.appendReplacement(ret, colors.get(matcher.group(0)));
        }

        matcher.appendTail(ret);
        return ret.toString();
    }

    public static String unparse(String string) {
        if (string == null) return null;

        StringBuffer ret = new StringBuffer();
        Matcher matcher = unparsePattern.matcher(string);

        while (matcher.find()) {
            matcher.appendReplacement(ret, "");
        }

        matcher.appendTail(ret);
        return ret.toString();
    }

    public static String parse(String string, Object... args) {
        return (args == null || args.length == 0) ? parse(string) : String.format(parse(string), fix(args));
    }

    public static void sendMessages(CommandSender sender, String... msgs) {
        for (String value : msgs) {
            sender.sendMessage(parse(value));
        }
    }

    public static String replace(String text, Object toReplace, Object value, Object... replace) {

        text = text.replace(toReplace + "", value + "");

        Iterator<Object> iter = Arrays.asList(replace).iterator();

        while (iter.hasNext()) {
            String key = iter.next() + "";
            String iterValue = iter.next() + "";

            text = text.replace(key, iterValue);
        }
        return text;
    }

    public static boolean endsWith(String string, String endsWith, boolean ignoreCase) {
        if (string != null && endsWith != null) {

            if (endsWith.length() > string.length())
                return false;
            else {
                String substring = string.substring(string.length() - endsWith.length());

                return ignoreCase ? substring.equalsIgnoreCase(endsWith) : substring.equals(endsWith);
            }
        } else return false;
    }

    public static int getMiddleSlot(Inventory inv) {
        int[] i = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        for (int slot : i)
            if (inv.getItem(slot) == null || inv.getItem(slot).getType() == Material.AIR)
                return slot;
        return -1;
    }

    public static void spawnRandomFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Random r = new Random();
        int rt = r.nextInt(4) + 1;

        FireworkEffect.Type stopType = FireworkEffect.Type.BALL;

        if (rt == 2) stopType = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) stopType = FireworkEffect.Type.BURST;
        if (rt == 4) stopType = FireworkEffect.Type.CREEPER;
        if (rt == 5) stopType = FireworkEffect.Type.STAR;

        int r1i = r.nextInt(18) + 1;
        int r2i = r.nextInt(18) + 1;

        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);

        FireworkEffect effect = FireworkEffect.builder()
                .flicker(r.nextBoolean())
                .withColor(c1)
                .withFade(c2)
                .with(stopType)
                .trail(r.nextBoolean())
                .build();

        fwm.addEffect(effect);
        fwm.setPower(2);
        fw.setFireworkMeta(fwm);
    }

    public static Color getColor(int i) {
        Color c = null;

        if (i == 1) c = Color.AQUA;
        if (i == 2) c = Color.BLACK;
        if (i == 3) c = Color.BLUE;
        if (i == 4) c = Color.FUCHSIA;
        if (i == 5) c = Color.GRAY;
        if (i == 6) c = Color.GREEN;
        if (i == 7) c = Color.LIME;
        if (i == 8) c = Color.MAROON;
        if (i == 9) c = Color.NAVY;
        if (i == 10) c = Color.OLIVE;
        if (i == 11) c = Color.ORANGE;
        if (i == 12) c = Color.PURPLE;
        if (i == 13) c = Color.RED;
        if (i == 14) c = Color.SILVER;
        if (i == 15) c = Color.TEAL;
        if (i == 16) c = Color.WHITE;
        if (i == 17) c = Color.YELLOW;
        if (i == 18) c = Color.fromBGR(9, 255, 0);

        return c;
    }

    private static Object[] fix(Object... obs) {
        List<Object> toFix = new ArrayList<>();

        for (Object o : obs) toFix.add(parse(o + ""));

        obs = toFix.toArray();
        return obs;
    }

    static {
        colors.put("<?>", "�");
        colors.put("<snowman>", "☃");
        colors.put("<wheelchair>", "♿");
        colors.put("<swords>", "⚔");
        colors.put("<warning>", "⚠");
        colors.put("<hammer and pick>", "⚒");
        colors.put("<anchor>", "⚓");
        colors.put("<empty flag>", "⚐");
        colors.put("<flag>", "⚑");
        colors.put("<recicle>", "♺");
        colors.put("<yin yang>", "☯");
        colors.put("<radioactive>", "☣");
        colors.put("<approve>", "✔");
        colors.put("<disapprove>", "✖");
        colors.put("<disapprove2>", "✘");
        colors.put("<plus>", "✚");
        colors.put("<crosshair>", "✛");
        colors.put("<zap>", "⚡");
        colors.put("<star inside ball>", "✪");
        colors.put("<star>", "⭑");
        colors.put("<cube>", "■");
        colors.put("<small cube>", "▪");
        colors.put("<big cube>", "▉");
        colors.put("<empty cube>", "□");
        colors.put("<small ball>", "•");
        colors.put("<ball>", "●");
        colors.put("<empty ball>", "○");
        colors.put("<iene>", "¥");
        colors.put("<heart>", "❤");
        colors.put("<triangle>", "▲");
        colors.put("<left arrow>", "⬅");
        colors.put("<right arrow>", "➡");
        colors.put("<arrow>", "➡");
        colors.put("<down arrow>", "⬇");
        colors.put("<up arrow>", "⬆");
        colors.put("<down left arrow>", "↙");
        colors.put("<down right arrow>", "↘");
        colors.put("<up left arrow>", "↖");
        colors.put("<up right arrow>", "↗");
        colors.put("<arrows>", "↔");
        colors.put("<up arrows", "↕");
        colors.put("<notes>", "♫");
        colors.put("<notes2>", "♬");
        colors.put("<note>", "♪");
        colors.put("<left triangle>", "◀");
        colors.put("<right triangle>", "▶");
        colors.put("<down triangle>", "▼");
        colors.put("<up triangle>", "▲");
        colors.put("<smile face>", "☺");
        colors.put("<smile>", "☺");
        colors.put("<full smile face>", "☻");
        colors.put("<full smile>", "☻");

        colors.put("<0>", "§0");
        colors.put("<1>", "§1");
        colors.put("<2>", "§2");
        colors.put("<3>", "§3");
        colors.put("<4>", "§4");
        colors.put("<5>", "§5");
        colors.put("<6>", "§6");
        colors.put("<7>", "§7");
        colors.put("<8>", "§8");
        colors.put("<9>", "§9");
        colors.put("<a>", "§a");
        colors.put("<b>", "§b");
        colors.put("<c>", "§c");
        colors.put("<d>", "§d");
        colors.put("<e>", "§e");
        colors.put("<f>", "§f");
        colors.put("<l>", "§l");
        colors.put("<o>", "§o");
        colors.put("<n>", "§n");
        colors.put("<m>", "§m");
        colors.put("<k>", "§k");
        colors.put("<r>", "§r");

        for (int i = 48; i <= 122; ++i) {
            char c = (char) i;

            colors.put("§" + c, "§" + c);
            colors.put("&" + c, "§" + c);
            colors.put(("§" + c).toUpperCase(), ("§" + c).toUpperCase());
            colors.put(("&" + c).toUpperCase(), ("§" + c).toUpperCase());

            if (i == 57) {
                i = 96;
            }
        }

        unparse.addAll(colors.values());

        StringBuilder patternStringBuilder = new StringBuilder();

        for (String find : colors.keySet()) {
            patternStringBuilder.append('(');
            patternStringBuilder.append(Pattern.quote(find));
            patternStringBuilder.append(")|");
        }

        String patternString = patternStringBuilder.toString();
        patternString = patternString.substring(0, patternString.length() - 1);

        parsePattern = Pattern.compile(patternString);

        StringBuilder unpatternStringBuilder = new StringBuilder();

        for (String find2 : unparse) {
            unpatternStringBuilder.append('(');
            unpatternStringBuilder.append(Pattern.quote(find2));
            unpatternStringBuilder.append(")|");
        }

        String unpatternString = unpatternStringBuilder.toString();
        unpatternString = unpatternString.substring(0, unpatternString.length() - 1);

        unparsePattern = Pattern.compile(unpatternString);
    }
}
