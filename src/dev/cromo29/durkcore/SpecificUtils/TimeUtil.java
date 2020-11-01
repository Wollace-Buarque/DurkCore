package dev.cromo29.durkcore.SpecificUtils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

    private TimeUtil() {
    }

    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss");
        return format.format(now).replace("@", "às");
    }

    public static String getDateSimplified() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(now);
    }

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

        StringBuilder sb = new StringBuilder();

        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto, 1 segundo e 1 milissegundo - done
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto e 1 segundo - done
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto e 1 milissegundo
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora, 1 minuto
        // 1 ano, 1 mes, 1 semana, 1 dia, 1 hora e 1 milissegundo - done
        // 1 ano, 1 mes, 1 semana, 1 dia e 1 hora - done
        // 1 ano, 1 mes, 1 semana e 1 dia - done

        // Milissegundo removed

        if (years > 0)
            sb.append(years).append(years == 1 ? " ano" : " anos");

        if (months > 0)
            sb.append(
                    years > 0 ? ", " : ""
            ).append(months).append(months == 1 ? " mês" : " meses");

        if (weeks > 0)
            sb.append(
                    months > 0 ? (days > 0 && hours < 1 && minutes < 1 ? " e " : ", ") : ""
            ).append(weeks).append(weeks == 1 ? " semana" : " semanas");

        if (days > 0)
            sb.append(
                    weeks > 0 ? (hours > 0 || minutes > 0 || seconds > 0 ? ", " : " e ") : ""
            ).append(days).append(days == 1 ? " dia" : " dias");

        if (hours > 0)
            sb.append(
                    days > 0 ? (minutes < 1 ? " e " : ", ") : ""
            ).append(hours).append(hours == 1 ? " hora" : " horas");

        if (minutes > 0)
            sb.append(
                    days > 0 || hours > 0 ? (seconds > 0 ? ", " : " e ") : (sb.length() > 0 ? ", " : "")
            ).append(minutes).append(minutes == 1 ? " minuto" : " minutos");

        if (seconds > 0)
            sb.append(
                    (days > 0 || hours > 0 || minutes > 0) ? " e " : (sb.length() > 0 ? ", " : "")
            ).append(seconds).append(seconds == 1 ? " segundo" : " segundos");

        String s = sb.toString();
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
