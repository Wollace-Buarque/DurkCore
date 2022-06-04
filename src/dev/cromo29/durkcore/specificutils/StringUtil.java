package dev.cromo29.durkcore.specificutils;

import dev.cromo29.durkcore.util.DefaultFontInfo;
import dev.cromo29.durkcore.util.TXT;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class StringUtil {

    private final static int CENTER_PX = 154, MAX_PX = 250;

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    public static boolean isStringLength(String string, int minimumLength, int maximumLength) {
        return string.matches("^\\w{" + minimumLength + "," + maximumLength + "}$");
    }

    public static double compareStrings(String stringA, String stringB) {
        return org.apache.commons.lang3.StringUtils.getJaroWinklerDistance(stringA, stringB) * 100.0D;
    }

    public static int compareStringEqualityPercent(String stringA, String stringB) {
        int len0 = stringA.length() + 1;
        int len1 = stringB.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (stringA.charAt(i - 1) == stringB.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    public static void sendCenteredWithBreak(CommandSender sender, String... messages) {
        Arrays.asList(messages).forEach(message -> sendCenteredWithBreak(sender, message));
    }

    public static void sendCenteredWithBreak(CommandSender sender, String message) {
        if (message == null || message.equals("")) {
            sender.sendMessage("");
            return;
        }

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        int charIndex = 0;
        int lastSpaceIndex = 0;
        String toSendAfter = null;
        String recentColorCode = "";

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode) {
                previousCode = false;
                recentColorCode = "§" + c;

                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;

            } else if (c == ' ') lastSpaceIndex = charIndex;

            else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }

            if (messagePxSize >= MAX_PX) {
                toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1);
                message = message.substring(0, lastSpaceIndex + 1);
                break;
            }
            charIndex++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;

        StringBuilder sb = new StringBuilder();

        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        sender.sendMessage(TXT.parse(sb + message));

        if (toSendAfter != null) sendCenteredWithBreak(sender, toSendAfter);
    }

    public static void sendCentered(CommandSender sender, String... messages) {
        Arrays.asList(messages).forEach(message -> sendCentered(sender, message));
    }

    public static void sendCentered(CommandSender sender, String message) {
        if (message == null || message.equals("")) {
            sender.sendMessage("");
            return;
        }

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;

        StringBuilder sb = new StringBuilder();

        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        sender.sendMessage(TXT.parse(sb.toString() + message));
    }

    public static String getCentered(String message) {
        if (message == null || message.equals("")) {
            return "";
        }

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;

        StringBuilder stringBuilder = new StringBuilder();

        while (compensated < toCompensate) {
            stringBuilder.append(" ");
            compensated += spaceLength;
        }

        return TXT.parse(stringBuilder + message);
    }
}
