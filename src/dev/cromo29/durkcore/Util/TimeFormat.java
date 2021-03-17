package dev.cromo29.durkcore.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimeFormat {

    public static String getTime(long milliseconds, boolean showSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        String format = showSeconds ? "dd/MM/yyyy @ HH:mm:ss" : "dd/MM/yyyy @ HH:mm";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(calendar.getTime()).replace("@", "às");
    }

    public static String getTime(long milliseconds) {
        return getTime(milliseconds, false);
    }

    public static String format(long milliseconds) {

        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);

        long years = days / 365;
        days %= 365;

        long months = days / 30;
        days %= 30;

        long weeks = days / 7;
        days %= 7;

        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));

        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));

        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));

        StringBuilder sb = new StringBuilder();

        if (years > 0) {
            sb.append(years).append(years == 1 ? " ano" : " anos");
        }

        if (months > 0) {
            sb.append(years > 0 ? (weeks > 0 ? ", " : " e ") : "").append(months).append(months == 1 ? " mês" : " meses");
        }

        if (weeks > 0) {
            sb.append(years > 0 || months > 0 ? (days > 0 ? ", " : " e ") : "").append(weeks).append(weeks == 1 ? " semana" : " semanas");
        }

        if (days > 0) {
            sb.append(years > 0 || months > 0 || weeks > 0 ? (hours > 0 ? ", " : " e ") : "").append(days).append(days == 1 ? " dia" : " dias");
        }

        if (hours > 0) {
            sb.append(years > 0 || months > 0 || weeks > 0 || days > 0 ? (minutes > 0 ? ", " : " e ") : "").append(hours).append(hours == 1 ? " hora" : " horas");
        }

        if (minutes > 0) {
            sb.append(years > 0 || months > 0 || weeks > 0 || days > 0 || hours > 0 ? (seconds > 0 ? ", " : " e ") : "").append(minutes).append(minutes == 1 ? " minuto" : " minutos");
        }

        if (seconds > 0) {
            sb.append(years > 0 || months > 0 || weeks > 0 || days > 0 || hours > 0 || minutes > 0 ? " e " : (sb.length() > 0 ? ", " : "")).append(seconds).append(seconds == 1 ? " segundo" : " segundos");
        }

        return sb.toString().isEmpty() ? "agora" : sb.toString();
    }

    public static String formatSimplified(long time) {

        long days = TimeUnit.MILLISECONDS.toDays(time);

        long years = days / 365;
        days %= 365;

        long months = days / 30;
        days %= 30;

        long weeks = days / 7;
        days %= 7;

        long hours = TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(time));

        long minutes = TimeUnit.MILLISECONDS.toMinutes(time)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));

        long seconds = TimeUnit.MILLISECONDS.toSeconds(time)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));

        StringBuilder sb = new StringBuilder();

        if (years > 0) {
            sb.append(years).append("a");
        }

        if (months > 0) {
            sb.append(" ").append(months).append("m");
        }

        if (weeks > 0) {
            sb.append(" ").append(weeks).append("se");
        }

        if (days > 0) {
            sb.append(" ").append(days).append("d");
        }

        if (hours > 0) {
            sb.append(" ").append(hours).append("h");
        }

        if (minutes > 0) {
            sb.append(" ").append(minutes).append("m");
        }

        if (seconds > 0) {
            sb.append(" ").append(seconds).append("s");
        }

        return sb.toString().isEmpty() ? "agora" : sb.toString();
    }
}