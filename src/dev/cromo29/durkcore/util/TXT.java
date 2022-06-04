package dev.cromo29.durkcore.util;

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

    private static final Pattern PARSE_PATTERN;
    private static final Pattern UNPARSE_PATTERN;
    private static final Map<String, String> COLORS = new HashMap<>();
    private static final List<String> UNPARSE = new ArrayList<>();

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
        Matcher matcher = PARSE_PATTERN.matcher(string);

        while (matcher.find()) {
            matcher.appendReplacement(ret, COLORS.get(matcher.group(0)));
        }

        matcher.appendTail(ret);
        return ret.toString();
    }

    public static String unparse(String string) {
        if (string == null) return null;

        StringBuffer ret = new StringBuffer();
        Matcher matcher = UNPARSE_PATTERN.matcher(string);

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
        COLORS.put("<?>", "�");
        COLORS.put("<snowman>", "☃");
        COLORS.put("<wheelchair>", "♿");
        COLORS.put("<swords>", "⚔");
        COLORS.put("<warning>", "⚠");
        COLORS.put("<hammer and pick>", "⚒");
        COLORS.put("<anchor>", "⚓");
        COLORS.put("<empty flag>", "⚐");
        COLORS.put("<flag>", "⚑");
        COLORS.put("<recicle>", "♺");
        COLORS.put("<yin yang>", "☯");
        COLORS.put("<radioactive>", "☣");
        COLORS.put("<approve>", "✔");
        COLORS.put("<disapprove>", "✖");
        COLORS.put("<disapprove2>", "✘");
        COLORS.put("<plus>", "✚");
        COLORS.put("<crosshair>", "✛");
        COLORS.put("<zap>", "⚡");
        COLORS.put("<star inside ball>", "✪");
        COLORS.put("<star>", "⭑");
        COLORS.put("<cube>", "■");
        COLORS.put("<small cube>", "▪");
        COLORS.put("<big cube>", "▉");
        COLORS.put("<empty cube>", "□");
        COLORS.put("<small ball>", "•");
        COLORS.put("<ball>", "●");
        COLORS.put("<empty ball>", "○");
        COLORS.put("<iene>", "¥");
        COLORS.put("<heart>", "❤");
        COLORS.put("<triangle>", "▲");
        COLORS.put("<left arrow>", "⬅");
        COLORS.put("<right arrow>", "➡");
        COLORS.put("<arrow>", "➡");
        COLORS.put("<down arrow>", "⬇");
        COLORS.put("<up arrow>", "⬆");
        COLORS.put("<down left arrow>", "↙");
        COLORS.put("<down right arrow>", "↘");
        COLORS.put("<up left arrow>", "↖");
        COLORS.put("<up right arrow>", "↗");
        COLORS.put("<arrows>", "↔");
        COLORS.put("<up arrows", "↕");
        COLORS.put("<notes>", "♫");
        COLORS.put("<notes2>", "♬");
        COLORS.put("<note>", "♪");
        COLORS.put("<left triangle>", "◀");
        COLORS.put("<right triangle>", "▶");
        COLORS.put("<down triangle>", "▼");
        COLORS.put("<up triangle>", "▲");
        COLORS.put("<smile face>", "☺");
        COLORS.put("<smile>", "☺");
        COLORS.put("<full smile face>", "☻");
        COLORS.put("<full smile>", "☻");

        COLORS.put("<0>", "§0");
        COLORS.put("<1>", "§1");
        COLORS.put("<2>", "§2");
        COLORS.put("<3>", "§3");
        COLORS.put("<4>", "§4");
        COLORS.put("<5>", "§5");
        COLORS.put("<6>", "§6");
        COLORS.put("<7>", "§7");
        COLORS.put("<8>", "§8");
        COLORS.put("<9>", "§9");
        COLORS.put("<a>", "§a");
        COLORS.put("<b>", "§b");
        COLORS.put("<c>", "§c");
        COLORS.put("<d>", "§d");
        COLORS.put("<e>", "§e");
        COLORS.put("<f>", "§f");
        COLORS.put("<l>", "§l");
        COLORS.put("<o>", "§o");
        COLORS.put("<n>", "§n");
        COLORS.put("<m>", "§m");
        COLORS.put("<k>", "§k");
        COLORS.put("<r>", "§r");

        for (int i = 48; i <= 122; ++i) {
            char c = (char) i;

            COLORS.put("§" + c, "§" + c);
            COLORS.put("&" + c, "§" + c);
            COLORS.put(("§" + c).toUpperCase(), ("§" + c).toUpperCase());
            COLORS.put(("&" + c).toUpperCase(), ("§" + c).toUpperCase());

            if (i == 57) {
                i = 96;
            }
        }

        UNPARSE.addAll(COLORS.values());

        StringBuilder patternStringBuilder = new StringBuilder();

        for (String find : COLORS.keySet()) {
            patternStringBuilder.append('(');
            patternStringBuilder.append(Pattern.quote(find));
            patternStringBuilder.append(")|");
        }

        String patternString = patternStringBuilder.toString();
        patternString = patternString.substring(0, patternString.length() - 1);

        PARSE_PATTERN = Pattern.compile(patternString);

        StringBuilder unpatternStringBuilder = new StringBuilder();

        for (String find : UNPARSE) {
            unpatternStringBuilder.append('(');
            unpatternStringBuilder.append(Pattern.quote(find));
            unpatternStringBuilder.append(")|");
        }

        String unpatternString = unpatternStringBuilder.toString();
        unpatternString = unpatternString.substring(0, unpatternString.length() - 1);

        UNPARSE_PATTERN = Pattern.compile(unpatternString);
    }
}
