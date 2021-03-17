package dev.cromo29.durkcore.Util;

import java.util.ArrayList;
import java.util.List;

public class BreakLine {

    private List<String> message = new ArrayList<>();

    public BreakLine addWordOnBreak(String word, String message) {
        if (message == null || message.equals("")) {
            return this;
        }

        message = message.replace("<a>", "§a");
        message = message.replace("<b>", "§b");
        message = message.replace("<c>", "§c");
        message = message.replace("<d>", "§d");
        message = message.replace("<e>", "§e");
        message = message.replace("<f>", "§f");
        message = message.replace("<l>", "§l");
        message = message.replace("<m>", "§m");
        message = message.replace("<n>", "§n");
        message = message.replace("<o>", "§o");

        for (int i = 0; i < 10; i++)
            message = message.replace("<" + i + ">", "§" + i);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        int charIndex = 0;
        int lastSpaceIndex = 0;

        String breakedMessage = null;
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

            int MAX_PX = 320;

            if (messagePxSize >= MAX_PX) {
                breakedMessage = "\n" + word + recentColorCode + message.substring(lastSpaceIndex + 1);
                message = message.substring(0, lastSpaceIndex + 1);
                break;
            }
            charIndex++;
        }

        this.message.add(message);

        if (breakedMessage != null) addWordOnBreak(word, breakedMessage);

        return this;
    }

    public String get() {
        StringBuilder x = new StringBuilder();

        message.forEach(x::append);

        message.clear();

        return x.toString();
    }

}
