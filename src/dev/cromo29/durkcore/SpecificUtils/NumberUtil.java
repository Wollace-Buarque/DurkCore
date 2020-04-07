package dev.cromo29.durkcore.SpecificUtils;

import dev.cromo29.durkcore.Util.TXT;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

public class NumberUtil {

    private static String[] formats = new String[]{"-", "-", "K", "M", "B", "T", "Q", "QT", "S", "ST", "O", "N", "D", "UD", "DD", "TD", "QD", "QTD", "SD", "STD", "OD", "ND", "V"};

    public static String format(Object value) {
        int ii;
        try {
            String val = (new DecimalFormat("###,###")).format(value).replace(".", ",");
            ii = val.indexOf(",");
            ii = val.split(",").length;
            return ii == -1 ? val : (val.substring(0, ii + 2) + formats[ii]).replace(",0", "");
        } catch (Exception var6) {
            String val = (new DecimalFormat("###,###")).format(value).replace(".", ",");
            ii = val.indexOf(",");
            if (ii == -1) {
                return val;
            } else {
                String num = val.substring(0, 1);
                String finalVal = val.substring(1).replace(",", "");
                return num + "e" + finalVal.length();
            }
        }
    }

    public static int getPercentage(double value, double percentage) {
        return (int) Math.round(value * percentage / 100.0D);
    }

    public static int getValueMax(double value, double maxOfValue, int maxValue) {
        float percent = (float) (value / maxOfValue);
        return (int) ((float) maxValue * percent);
    }

    public static String getProgressBar(int current, int max, int totalBars, String symbol, String completedColor, String notCompletedColor) {
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
            } catch (Exception var2) {
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
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception var2) {
            return 0;
        }
    }

    public static double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception var2) {
            return 0.0D;
        }
    }

    public static long getLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception var2) {
            return 0L;
        }
    }

    public static float getFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception var2) {
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
        return number.equals("") ? new BigDecimal(0) : new BigDecimal(number);
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
        String formated = f.format(s).replace(",", "@").replace(".", "#");
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
