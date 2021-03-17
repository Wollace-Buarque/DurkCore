package dev.cromo29.durkcore.SpecificUtils;

import com.google.common.collect.Lists;
import dev.cromo29.durkcore.Util.TXT;

import java.util.*;

public class ListUtil {

    public static List<String> getColorizedStringList(List<String> stringList, Object... replacers) {
        List<String> toReturn = new ArrayList<>();

        for (String text : stringList) {
            toReturn.add(replace(TXT.parse(text), replacers));
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
        if (strings == null || strings.length == 0) return Lists.newArrayList();

        List<String> colorizedList = Lists.newArrayList();
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
        List<T> toReturn = Lists.newArrayList();

        if (args == null) {
            return toReturn;
        }

        Collections.addAll(toReturn, args);

        return toReturn;
    }

    public static List<String> getStringList(String... args) {
        List<String> toReturn = Lists.newArrayList();

        if (args == null) {
            return toReturn;
        }

        for (String arg : args) {
            toReturn.add(TXT.parse(arg));
        }

        return toReturn;
    }

    public static String[] getStringArray(String... args) {
        return getStringList(args).toArray(new String[]{});
    }

    private  static String replace(String text, Object... replace) {
        Iterator<Object> iter = Arrays.asList(replace).iterator();

        while (iter.hasNext()) {
            String key = iter.next() + "";
            String iterValue = iter.next() + "";

            text = text.replace(key, iterValue);
        }
        return text;
    }

}