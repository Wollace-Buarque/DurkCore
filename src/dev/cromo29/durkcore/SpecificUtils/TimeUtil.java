package dev.cromo29.durkcore.SpecificUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeUtil {

    public static String getCurrentTime() {
        return formatFull(System.currentTimeMillis());
    }

    public static boolean isTimeExpired(long time) {
        return time < System.currentTimeMillis();
    }

    public static String formatFull(long time) {
        if (time == 0) return "0 segundos";

        long years = getTimeToFormat(time, Time.YEAR);
        long months = getTimeToFormat(time, Time.MONTH);
        long weeks = getTimeToFormat(time, Time.WEEK);
        long days = getTimeToFormat(time, Time.DAY);
        long hours = getTimeToFormat(time, Time.HOUR);
        long minutes = getTimeToFormat(time, Time.MINUTE);
        long seconds = getTimeToFormat(time, Time.SECOND);
        // long milliseconds = getTimeToFormat(time, Time.MILLIS);

        StringBuilder stringBuilder = new StringBuilder();

        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto, 1 segundo e 1 milissegundo - done
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto e 1 segundo - done
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto e 1 milissegundo
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora e 1 milissegundo - done
        // 1 ano, 1 mes, 1 semana, 1 dia e 1 hora - done
        // 1 ano, 1 mes, 1 semana e 1 dia - done

        // Milissegundo removed

        if (years > 0)
            stringBuilder.append(years).append(years == 1 ? " ano" : " anos");

        if (months > 0)
            stringBuilder.append(
                    years > 0 ? ", " : ""
            ).append(months).append(months == 1 ? " mês" : " meses");

        if (weeks > 0)
            stringBuilder.append(
                    months > 0 ? (days > 0 && hours < 1 && minutes < 1 ? " e " : ", ") : ""
            ).append(weeks).append(weeks == 1 ? " semana" : " semanas");

        if (days > 0)
            stringBuilder.append(
                    weeks > 0 ? (hours > 0 || minutes > 0 || seconds > 0 ? ", " : " e ") : ""
            ).append(days).append(days == 1 ? " dia" : " dias");

        if (hours > 0)
            stringBuilder.append(
                    days > 0 ? (minutes < 1 ? " e " : ", ") : ""
            ).append(hours).append(hours == 1 ? " hora" : " horas");

        if (minutes > 0)
            stringBuilder.append(
                    days > 0 || hours > 0 ? (seconds > 0 ? ", " : " e ") : (stringBuilder.length() > 0 ? ", " : "")
            ).append(minutes).append(minutes == 1 ? " minuto" : " minutos");

        if (seconds > 0)
            stringBuilder.append(
                    (days > 0 || hours > 0 || minutes > 0) ? " e " : (stringBuilder.length() > 0 ? ", " : "")
            ).append(seconds).append(seconds == 1 ? " segundo" : " segundos");

        String s = stringBuilder.toString();
        return s.isEmpty() ? "0 segundos" : s;
    }

    public static long getTimeToFormat(long time, Time type) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);

        switch (type) {
            case MILLIS:
                return calendar.get(Calendar.MILLISECOND);
            case SECOND:
                return calendar.get(Calendar.SECOND);
            case MINUTE:
                return calendar.get(Calendar.MINUTE);
            case HOUR:
                return calendar.get(Calendar.HOUR_OF_DAY);
            case DAY:
                return calendar.get(Calendar.DAY_OF_MONTH);
            case WEEK:
                return calendar.get(Calendar.WEEK_OF_MONTH);
            case MONTH:
                return calendar.get(Calendar.MONTH) + 1;
            case YEAR:
                return calendar.get(Calendar.YEAR);
            default:
                return time;
        }
    }
//    public static long getTime(long time, Time type) {
//        switch (type) {
//            case SECOND: return time * 1_000L; // 1_000 milliseconds = 1 second
//            case MINUTE: return time * 60_000L; // 1_000 * 60
//            case HOUR: return time * 3_600_000L; // 60_000 * 60
//            case DAY: return time * 86_400_000L; // 3_600_000 * 24
//            case WEEK: return time * 604_800_000L; // 86_400_000 * 7
//            case MONTH: return time * 2_592_000_000L; // 86_400_000L * 30
//            case YEAR: return time * 31_104_000_000L; // 2_592_000_000 * 12
//            default: return time;
//        }
//    }

    public static Long convertTime(String string) {
        long time = 0;
        String[] split = string.split(" ");

        for (String splittedString : split) {
            String diminutive = splittedString.replaceAll("[0-9]", "").toLowerCase();
            String numbers = splittedString.replaceAll("[^\\d-]", "");

            int timeInt;
            try {
                timeInt = Integer.parseInt(numbers);
            } catch (NumberFormatException exception) {
                return (long) -1;
            }

            for (TimeMultiplier timeMultiplier : TimeMultiplier.values()) {
                if (!timeMultiplier.getDiminutive().equals(diminutive)) continue;

                time += timeMultiplier.getMultiplier() * timeInt;
            }
        }

        return time;
    }

    private enum TimeMultiplier {

        SECONDS(1000, "s"),
        MINUTES(60 * 1000, "m"),
        HOURS(60 * 60 * 1000, "h"),
        DAYS(24 * 60 * 60 * 1000, "d"),
        WEEKS(7 * 24 * 60 * 60 * 1000, "w"),
        MONTHS(30 * 24 * 60 * 60 * 1000L, "mm"),
        YEARS(365 * 24 * 60 * 60 * 1000L, "y");

        private long multiplier;
        private CharSequence diminutive;

        TimeMultiplier(long multiplier, CharSequence diminutive) {
            this.multiplier = multiplier;
            this.diminutive = diminutive;
        }

        public long getMultiplier() {
            return multiplier;
        }

        public CharSequence getDiminutive() {
            return diminutive;
        }
    }

    public enum Time {
        MILLIS,
        SECOND,
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR
    }
}
