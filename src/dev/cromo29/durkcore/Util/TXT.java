package dev.cromo29.durkcore.Util;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TXT {

    private TXT() {
    }

    private static Pattern parsePattern;
    private static Pattern unparsePattern;
    private static Map<String, String> colors = new HashMap<>();
    private static List<String> unparse = new ArrayList<>();

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

            if (endsWith.length() > string.length()) return false;

            String substring = string.substring(string.length() - endsWith.length());

            return ignoreCase ? substring.equalsIgnoreCase(endsWith) : substring.equals(endsWith);

        } else return false;
    }

    public static int getMiddleSlot(Inventory inv) {
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        for (int slot : slots) {

            if (inv.getItem(slot) == null || inv.getItem(slot).getType() == Material.AIR) return slot;
        }

        return -1;
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

        for (String find : unparse) {
            unpatternStringBuilder.append('(');
            unpatternStringBuilder.append(Pattern.quote(find));
            unpatternStringBuilder.append(")|");
        }

        String unpatternString = unpatternStringBuilder.toString();
        unpatternString = unpatternString.substring(0, unpatternString.length() - 1);

        unparsePattern = Pattern.compile(unpatternString);
    }
}
