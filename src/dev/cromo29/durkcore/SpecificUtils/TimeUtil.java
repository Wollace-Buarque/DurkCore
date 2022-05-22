package dev.cromo29.durkcore.specificutils;

public class TimeUtil {

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

        private final long multiplier;
        private final CharSequence diminutive;

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
