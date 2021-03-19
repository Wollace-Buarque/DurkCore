package dev.cromo29.durkcore.SpecificUtils;

import dev.cromo29.durkcore.Util.TXT;

import java.util.*;

public class ListUtil {

    public static List<String> getColorizedStringList(List<String> stringList, Object... args) {
        List<String> toReturn = new ArrayList<>();

        for (String text : stringList) {
            toReturn.add(replace(TXT.parse(text), args));
        }

        return toReturn;
    }

    public static List<String> getColorizedStringList(List<String> stringList) {
        List<String> toReturn = new ArrayList<>();

        for (String text : stringList) {
            toReturn.add(TXT.parse(text));
        }

        return toReturn;
    }

    public static List<String> getColorizedStringList(String... strings) {
        if (strings == null || strings.length == 0) return new ArrayList<>();

        List<String> colorizedList = new ArrayList<>();
        Iterator<String> loop = Arrays.asList(strings).iterator();

        while (loop.hasNext()) {
            colorizedList.add(TXT.parse(loop.next()));
        }

        return colorizedList;
    }

    public static <T> List<T> invertList(List<T> toInvert) {
        Collections.reverse(toInvert);

        return toInvert;
    }

    @SafeVarargs
    public static <T> List<T> getList(T... args) {
        List<T> toReturn = new ArrayList<>();

        if (args == null) {
            return toReturn;
        }

        Collections.addAll(toReturn, args);

        return toReturn;
    }

    public static List<String> getStringList(String... args) {
        List<String> toReturn = new ArrayList<>();

        if (args == null) {
            return toReturn;
        }

        for (String arg : args) {
            toReturn.add(TXT.parse(arg));
        }

        return toReturn;
    }

    public static String[] getStringArray(List<String> args) {
        return args.toArray(new String[]{});
    }

    public static String[] getStringArray(String... args) {
        return getStringList(args).toArray(new String[]{});
    }

    private static String replace(String text, Object... args) {
        Iterator<Object> iterator = Arrays.asList(args).iterator();

        while (iterator.hasNext()) {
            String key = iterator.next() + "";
            String iterValue = iterator.next() + "";

            text = text.replace(key, iterValue);
        }
        return text;
    }

}