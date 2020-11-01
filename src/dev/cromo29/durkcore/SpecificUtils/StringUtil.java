package dev.cromo29.durkcore.SpecificUtils;

import dev.cromo29.durkcore.Util.DefaultFontInfo;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.command.CommandSender;

public class StringUtil {

    private final static int CENTER_PX = 154;
    private final static int MAX_PX = 250;

    public static void sendCenteredWithBreak(CommandSender sender, String... messages) {
        for (String message : messages)
            sendCenteredWithBreak(sender, message);
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
            if (c == '§' || c == '&') {
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

        sender.sendMessage(TXT.parse(sb.toString() + message));

        if (toSendAfter != null) sendCenteredWithBreak(sender, toSendAfter);
    }

    public static void sendCentered(CommandSender sender, String... messages) {
        for (String message : messages)
            sendCentered(sender, message);
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
            if (c == '§' || c == '&') {
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
            if (c == '§' || c == '&') {
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

        return TXT.parse(sb.toString() + message);
    }
}
