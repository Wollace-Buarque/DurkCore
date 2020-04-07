package dev.cromo29.durkcore.Util;


import dev.cromo29.durkcore.SpecificUtils.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cooldown {
    public Cooldown() {
    }

    private static String format(long value) {
        return value > 9L ? "" + value : "0" + value;
    }

    public static boolean isTimeExpired(long time) {
        return time - getCurrentTime() <= 0L;
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

    public static String getDate(Long time) {
        if (time == 0L) {
            return "?";
        } else {
            long anos = time / 31104000L;
            time = time - anos * 31104000L;
            long meses = time / 2592000L;
            time = time - meses * 2592000L;
            long dias = time / 86400L;
            time = time - dias * 86400L;
            long horas = time / 3600L;
            time = time - horas * 3600L;
            long minutos = time / 60L;
            time = time - minutos * 60L;
            long segundos = time;
            return format(dias) + "/" + format(meses) + "/" + format(anos) + " às " + format(horas) + ":" + format(minutos) + ":" + format(segundos);
        }
    }

    public static String getTimeString(long time) {
        if (time == 0L) {
            return "0 segundos";
        } else {
            long anos = time / 31104000L;
            time -= anos * 31104000L;
            long meses = time / 2592000L;
            time -= meses * 2592000L;
            long dias = time / 86400L;
            time -= dias * 86400L;
            long horas = time / 3600L;
            time -= horas * 3600L;
            long minutos = time / 60L;
            time -= minutos * 60L;
            StringBuilder sb = new StringBuilder();
            if (anos > 0L) {
                sb.append(", ").append(anos).append(" ").append(anos == 1L ? "ano" : "anos");
            }

            if (meses > 0L) {
                sb.append(", ").append(meses).append(" ").append(meses == 1L ? "mês" : "meses");
            }

            if (dias > 0L) {
                sb.append(", ").append(dias).append(" ").append(dias == 1L ? "dia" : "dias");
            }

            if (horas > 0L) {
                sb.append(", ").append(horas).append(" ").append(horas == 1L ? "hora" : "horas");
            }

            if (minutos > 0L) {
                sb.append(",  e ").append(minutos).append(" ").append(minutos == 1L ? "minuto" : "minutos");
            }

            if (time > 0L) {
                sb.append(" e ").append(time).append(" ").append(time == 1L ? "segundo" : "segundos");
            }

            return sb.toString().replaceFirst(", ", "").replaceFirst(" e ", "");
        }
    }

    public static long getCurrentTime() {
        try {
            long seconds = 0L;
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:dd:MM:yyyy");
            String[] dates = format.format(now).split(":");
            if (Long.parseLong(dates[0]) > 0L) {
                seconds += 3600L * Long.parseLong(dates[0]);
            }

            if (Long.parseLong(dates[1]) > 0L) {
                seconds += 60L * Long.parseLong(dates[1]);
            }

            if (Long.parseLong(dates[2]) > 0L) {
                seconds += Long.parseLong(dates[2]);
            }

            if (Long.parseLong(dates[3]) > 0L) {
                seconds += 86400L * Long.parseLong(dates[3]);
            }

            if (Long.parseLong(dates[4]) > 0L) {
                seconds += 2592000L * Long.parseLong(dates[4]);
            }

            if (Long.parseLong(dates[5]) > 0L) {
                seconds += 31104000L * Long.parseLong(dates[5]);
            }

            return seconds;
        } catch (Exception var5) {
            return 0L;
        }
    }

    public static long getDateType(long time, Cooldown.Time t) {
        switch (t) {
            case YEAR:
                return 31104000L * time;
            case MONTH:
                return 2592000L * time;
            case DAY:
                return 86400L * time;
            case HOUR:
                return 3600L * time;
            case MINUTE:
                return 60L * time;
            default:
                return time;
        }
    }

    public static int[] getTimeInt(long time) {
        if (time == 0L) {
            return new int[]{0, 0, 0, 0, 0, 0};
        } else {
            long anos = time / 31104000L;
            time -= anos * 31104000L;
            long meses = time / 2592000L;
            time -= meses * 2592000L;
            long dias = time / 86400L;
            time -= dias * 86400L;
            long horas = time / 3600L;
            time -= horas * 3600L;
            long minutos = time / 60L;
            time -= minutos * 60L;
            return new int[]{NumberUtil.toInt(anos), NumberUtil.toInt(meses), NumberUtil.toInt(dias), NumberUtil.toInt(horas), NumberUtil.toInt(minutos), NumberUtil.toInt(time)};
        }
    }

    public static int[] getTimeInt() {
        long time = getCurrentTime();
        if (time == 0L) {
            return new int[]{0, 0, 0, 0, 0, 0};
        } else {
            long anos = time / 31104000L;
            time -= anos * 31104000L;
            long meses = time / 2592000L;
            time -= meses * 2592000L;
            long dias = time / 86400L;
            time -= dias * 86400L;
            long horas = time / 3600L;
            time -= horas * 3600L;
            long minutos = time / 60L;
            time -= minutos * 60L;
            return new int[]{NumberUtil.toInt(anos), NumberUtil.toInt(meses), NumberUtil.toInt(dias), NumberUtil.toInt(horas), NumberUtil.toInt(minutos), NumberUtil.toInt(time)};
        }
    }

    public static String getTimeStringSimplified() {
        long time = getCurrentTime();
        if (time == 0L) {
            return "0";
        } else {
            long anos = time / 31104000L;
            time -= anos * 31104000L;
            long meses = time / 2592000L;
            time -= meses * 2592000L;
            long dias = time / 86400L;
            time -= dias * 86400L;
            long horas = time / 3600L;
            time -= horas * 3600L;
            long minutos = time / 60L;
            time -= minutos * 60L;
            StringBuilder sb = new StringBuilder();
            if (anos > 0L) {
                sb.append(anos).append("a");
            }

            if (meses > 0L) {
                sb.append(meses).append("m");
            }

            if (dias > 0L) {
                sb.append(dias).append("d");
            }

            if (horas > 0L) {
                sb.append(horas).append("h");
            }

            if (minutos > 0L) {
                sb.append(minutos).append("m");
            }

            if (time > 0L) {
                sb.append(time).append("s");
            }

            return sb.toString();
        }
    }

    public static String getTimeStringSimplified(long time) {
        if (time == 0L) {
            return "0";
        } else {
            long anos = time / 31104000L;
            time -= anos * 31104000L;
            long meses = time / 2592000L;
            time -= meses * 2592000L;
            long dias = time / 86400L;
            time -= dias * 86400L;
            long horas = time / 3600L;
            time -= horas * 3600L;
            long minutos = time / 60L;
            time -= minutos * 60L;
            StringBuilder sb = new StringBuilder();
            if (anos > 0L) {
                sb.append(anos).append("a");
            }

            if (meses > 0L) {
                sb.append(meses).append("m");
            }

            if (dias > 0L) {
                sb.append(dias).append("d");
            }

            if (horas > 0L) {
                sb.append(horas).append("h");
            }

            if (minutos > 0L) {
                sb.append(minutos).append("m");
            }

            if (time > 0L) {
                sb.append(time).append("s");
            }

            return sb.toString();
        }
    }

    public static int getTimeInt(int[] i, Cooldown.Time t) {
        try {
            return t == Cooldown.Time.YEAR ? i[0] : (t == Cooldown.Time.MONTH ? i[1] : (t == Cooldown.Time.DAY ? i[2] : (t == Cooldown.Time.HOUR ? i[3] : (t == Cooldown.Time.MINUTE ? i[4] : (t == Cooldown.Time.SECOND ? i[5] : 0)))));
        } catch (Exception var3) {
            return 0;
        }
    }

    public static enum Time {
        DAY,
        MONTH,
        YEAR,
        HOUR,
        MINUTE,
        SECOND;

        private Time() {
        }
    }
}
