package dev.cromo29.durkcore.SpecificUtils;

import dev.cromo29.durkcore.Util.TXT;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class NumberUtil {

    public static final double LOG = 6.907755278982137D;
    public static final Object[][] VALUES = {
            {"", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG", "QTV"},
            {1D, 1000.0D, 1000000.0D, 1.0E9D, 1.0E12D, 1.0E15D, 1.0E18D, 1.0E21D, 1.0E24D, 1.0E27D, 1.0E30D, 1.0E33D, 1.0E36D, 1.0E39D, 1.0E42D, 1.0E45D, 1.0E48D, 1.0E51D, 1.0E54D, 1.0E57D, 1.0E60D, 1.0E63D, 1.0E66D, 1.0E69D, 1.0E72D}
    };

    public static final DecimalFormat FORMAT = new DecimalFormat("#,###.##", new DecimalFormatSymbols(new Locale("pt", "BR")));

    public static String format(double number) {
        if (number == 0) return FORMAT.format(number);

        int index = (int) (Math.log(number) / LOG);

        return FORMAT.format(number / (double) VALUES[1][index]) + VALUES[0][index];
    }

    public static int getPercentageRounded(double value, double percentage) {
        return (int) getPercentage(value, percentage);
    }

    public static double getPercentage(double value, double percentage) {
        return Math.round(value * percentage / 100d);
    }

    public static double randomPercentage() {
        return ThreadLocalRandom.current().nextDouble() * 100.0;
    }

    public static boolean randomChance(double chance) {
        return randomPercentage() <= chance;
    }

    public static int getValueMax(double value, double maxOfValue, int maxValue) {
        float percent = (float) (value / maxOfValue);
        return (int) (maxValue * percent);
    }


    public static String getProgressBar(double current, double max, int totalBars, String symbol, String completedColor, String notCompletedColor) {
        float percent = (float) current / (float) max;
        int progressBars = (int) ((float) totalBars * percent);
        int leftOver = totalBars - progressBars;
        StringBuilder sb = new StringBuilder();

        sb.append(TXT.parse(completedColor));

        int i;
        for (i = 0; i < progressBars; ++i)
            sb.append(symbol);

        sb.append(TXT.parse(notCompletedColor));

        for (i = 0; i < leftOver; ++i)
            sb.append(symbol);

        return sb.toString();
    }

    public static boolean isValidInt(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan")) {
            return false;
        } else {
            try {
                Integer.parseInt(toCheck);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
    }

    public static boolean isValidDouble(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan")) {
            return false;
        } else {
            try {
                Double.parseDouble(toCheck);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
    }

    public static int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            return 0;
        }
    }

    public static double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception ignored) {
            return 0.0D;
        }
    }

    public static long getLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception ignored) {
            return 0L;
        }
    }

    public static float getFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception ignored) {
            return 0.0F;
        }
    }

    public static double toDouble(long l) {
        return (double) l;
    }

    public static double toDouble(float f) {
        return f;
    }

    public static double toDouble(int i) {
        return i;
    }

    public static int toInt(long l) {
        return (int) l;
    }

    public static int toInt(double d) {
        return (int) d;
    }

    public static int toInt(float f) {
        return (int) f;
    }

    public static float toFloat(int i) {
        return (float) i;
    }

    public static float toFloat(double d) {
        return (float) d;
    }

    public static float toFloat(long l) {
        return (float) l;
    }

    public static long toLong(int i) {
        return i;
    }

    public static long toLong(double d) {
        return (long) d;
    }

    public static long toLong(float f) {
        return (long) f;
    }

    public static BigDecimal getBigDecimal(String number) {
        if (number.equals("")) return new BigDecimal(0);

        return new BigDecimal(number);
    }

    public static String formatNumberSimple(double s) {
        return formatNumberSimple(s, ',');
    }

    public static String formatNumberSimple(double s, char spacer) {
        DecimalFormat f = new DecimalFormat("###,###");
        return f.format(s).replace(",", spacer + "");
    }

    public static String formatNumber(double s) {
        return formatNumber(s, ',', '.');
    }

    public static String formatNumber(double s, char spacer, char decimalSpacer) {
        DecimalFormat f = new DecimalFormat("###,###.##");
        String formated = f.format(s).replace(",", "@")
                .replace(".", "#");

        return formated.replace("#", spacer + "").replace("@", decimalSpacer + "");
    }

    public static int getRandom(int lower, int upper) {
        return ThreadLocalRandom.current().nextInt(upper - lower + 1) + lower;
    }

    public static String hologramMessageFormatDouble(double value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(value);
    }

    public static String formatMoney(double money) {
        int monao = (int) money;

        if (money >= 1000000000)
            return new DecimalFormat("0.00B").format(money * 1.0D / 100000000.0D);
        else if (money >= 1000000)
            return new DecimalFormat("0.00M").format(money * 1.0D / 1000000D);
        else if (money >= 1000)
            return new DecimalFormat("0.00k").format(money * 1.0D / 1000D);
        else if (money >= 1)
            return monao + ".0";
        else
            return "0";
    }
}
