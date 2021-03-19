package dev.cromo29.durkcore.Util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

public class PlayerFaceToString {

    private PlayerFaceToString() {}

    private static Map<String, String> cachedNames = new WeakHashMap<>();

    public static void uncache(Player player) { uncache(player.getName()); }
    public static void uncache(String playerName) { cachedNames.remove(playerName.toLowerCase()); }

    public static boolean isCached(Player player) { return isCached(player.getName()); }
    public static boolean isCached(String playerName) { return cachedNames.containsKey(playerName.toLowerCase()); }

    public static String getOf(Player player) { return getOf(player, true, new ArrayList<>()); }
    public static String getOf(String playerName) { return getOf(playerName, true, new ArrayList<>()); }

    public static String getOf(Player player, String... stringPrefixSuffix) { return getOf(player, true, Arrays.asList(stringPrefixSuffix)); }
    public static String getOf(String playerName, String...stringPrefixSuffix) { return getOf(playerName, true, Arrays.asList(stringPrefixSuffix)); }

    public static String getOf(Player player, List<String> stringPrefixSuffix) { return getOf(player, true, stringPrefixSuffix); }
    public static String getOf(String playerName, List<String> stringPrefixSuffix) { return getOf(playerName, true, stringPrefixSuffix); }

    public static String getOf(Player player, boolean cacheString) { return getOf(player, cacheString, new ArrayList<>()); }
    public static String getOf(String playerName, boolean cacheString) { return getOf(playerName, cacheString, new ArrayList<>()); }

    public static String getOf(Player player, boolean cacheString, String... stringPrefixSuffix) { return getOf(player, cacheString, Arrays.asList(stringPrefixSuffix)); }
    public static String getOf(String playerName, boolean cacheString, String...stringPrefixSuffix) { return getOf(playerName, cacheString, Arrays.asList(stringPrefixSuffix)); }

    public static String getOf(Player player, boolean cacheString, List<String> stringPrefixSuffix) { return getOf(player.getName(), cacheString, stringPrefixSuffix); }
    public static String getOf(String playerName, boolean cacheString, List<String> stringPrefixSuffix) {

        if (cachedNames.containsKey(playerName.toLowerCase())) return cachedNames.get(playerName.toLowerCase());

        BufferedImage head;

        try {
            URLConnection urlConnection = new URL("https://minotar.net/avatar/" + playerName).openConnection();
            BufferedImage image = ImageIO.read(urlConnection.getInputStream());

            head = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = head.createGraphics();

            graphics.drawImage(image, 0, 0, 8, 8, null);
            graphics.dispose();

        } catch (Exception ignored) { return getOf("MHF_Steve", cacheString, stringPrefixSuffix); }

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        int size = stringPrefixSuffix.size();
        boolean isEmpty = stringPrefixSuffix.isEmpty();

        for (int y = 0; y < head.getHeight(); y++) {

            String prefix = "";
            String suffix = "";

            if (!isEmpty && index < size) prefix = stringPrefixSuffix.get(index);

            index++;

            if (!isEmpty && index < size) suffix = stringPrefixSuffix.get(index);

            stringBuilder.append(prefix);

            for (int x = 0; x < head.getWidth(); x++) {
                ChatColor chatColor = getClosestColor(new Color(head.getRGB(x, y)));
                stringBuilder.append(chatColor).append('\u2588');
            }

            stringBuilder.append(suffix).append("\n");

            index++;
        }

        String string = stringBuilder.toString();

        if (cacheString) cachedNames.put(playerName, string);

        return string;

    }

    private static final Color[] colors = {
            new Color(0, 0, 0),
            new Color(0, 0, 170),
            new Color(0, 170, 0),
            new Color(0, 170, 170),
            new Color(170, 0, 0),
            new Color(170, 0, 170),
            new Color(255, 170, 0),
            new Color(170, 170, 170),
            new Color(85, 85, 85),
            new Color(85, 85, 255),
            new Color(85, 255, 85),
            new Color(85, 255, 255),
            new Color(255, 85, 85),
            new Color(255, 85, 255),
            new Color(255, 255, 85),
            new Color(255, 255, 255),
    };

    private static ChatColor getClosestColor(Color color) {
        double bestMatch = -1;
        int idx = 0;

        for (int i = 0; i < colors.length; i++) {
            if (isEqual(colors[i], color)) return ChatColor.values()[i];

            double distance = getDistance(color, colors[i]);

            if (distance < bestMatch || bestMatch == -1) {
                bestMatch = distance;
                idx = i;
            }
        }
        return ChatColor.values()[idx];
    }

    private static double getDistance(Color firstColor, Color secondColor) {
        double r = (firstColor.getRed() + secondColor.getRed()) / 2.0;
        double red = firstColor.getRed() - secondColor.getRed();
        double green = firstColor.getGreen() - secondColor.getGreen();
        double blue = firstColor.getBlue() - secondColor.getBlue();
        double w1 = 2 + r / 256.0;
        double w2 = 2 + (255 - r) / 256.0;

        return w1 * red * red + 4 * green * green + w2 * blue * blue;
    }

    private static boolean isEqual(Color firstColor, Color secondColor) {
        return Math.abs(firstColor.getRed() - secondColor.getRed()) <= 5
                && Math.abs(firstColor.getGreen() - secondColor.getGreen()) <= 5
                && Math.abs(firstColor.getBlue() - secondColor.getBlue()) <= 5;
    }

}
