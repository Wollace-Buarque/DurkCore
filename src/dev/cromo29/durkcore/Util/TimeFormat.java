package dev.cromo29.durkcore.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimeFormat {

    public static String getTime(long milisegundos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisegundos);

        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy # HH:mm");

        return formatador.format(calendar.getTime()).replace("#", "às");
    }

    public static String format(long time) {

        long dias = TimeUnit.MILLISECONDS.toDays(time);
        long anos = dias / 365;
        dias %= 365;
        long meses = dias / 30;
        dias %= 30;
        long semanas = dias / 7;
        dias %= 7;

        long horas = TimeUnit.MILLISECONDS.toHours(time)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(time));
        long minutos = TimeUnit.MILLISECONDS.toMinutes(time)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));
        long segundos = TimeUnit.MILLISECONDS.toSeconds(time)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));

        StringBuilder sb = new StringBuilder();

        if (anos > 0) sb.append(anos).append(anos == 1 ? " ano" : " anos");
        if (meses > 0)
            sb.append(anos > 0 ? (semanas > 0 ? ", " : " e ") : "").append(meses).append(meses == 1 ? " mês" : " meses");
        if (semanas > 0)
            sb.append(anos > 0 || meses > 0 ? (dias > 0 ? ", " : " e ") : "").append(semanas).append(semanas == 1 ? " semana" : " semanas");
        if (dias > 0)
            sb.append(anos > 0 || meses > 0 || semanas > 0 ? (horas > 0 ? ", " : " e ") : "").append(dias).append(dias == 1 ? " dia" : " dias");
        if (horas > 0)
            sb.append(anos > 0 || meses > 0 || semanas > 0 || dias > 0 ? (minutos > 0 ? ", " : " e ") : "").append(horas).append(horas == 1 ? " hora" : " horas");
        if (minutos > 0)
            sb.append(anos > 0 || meses > 0 || semanas > 0 || dias > 0 || horas > 0 ? (segundos > 0 ? ", " : " e ") : "").append(minutos).append(minutos == 1 ? " minuto" : " minutos");
        if (segundos > 0)
            sb.append(anos > 0 || meses > 0 || semanas > 0 || dias > 0 || horas > 0 || minutos > 0 ? " e " : (sb.length() > 0 ? ", " : "")).append(segundos).append(segundos == 1 ? " segundo" : " segundos");

        return sb.toString().isEmpty() ? "agora" : sb.toString();
    }

    public static String formatSimplified(long time) {

        long dias = TimeUnit.MILLISECONDS.toDays(time);
        long anos = dias / 365;
        dias %= 365;
        long meses = dias / 30;
        dias %= 30;
        long semanas = dias / 7;
        dias %= 7;

        long horas = TimeUnit.MILLISECONDS.toHours(time)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(time));
        long minutos = TimeUnit.MILLISECONDS.toMinutes(time)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));
        long segundos = TimeUnit.MILLISECONDS.toSeconds(time)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));

        StringBuilder sb = new StringBuilder();

        if (anos > 0) sb.append(anos).append("a");
        if (meses > 0)
            sb.append(" ").append(meses).append("m");
        if (semanas > 0)
            sb.append(" ").append(semanas).append("se");
        if (dias > 0)
            sb.append(" ").append(dias).append("d");
        if (horas > 0)
            sb.append(" ").append(horas).append("h");
        if (minutos > 0)
            sb.append(" ").append(minutos).append("m");
        if (segundos > 0)
            sb.append(" ").append(segundos).append("s");

        return sb.toString().isEmpty() ? "agora" : sb.toString();
    }
}