package dev.cromo29.durkcore.SpecificUtils;

import com.google.common.collect.*;
import dev.cromo29.durkcore.Util.TXT;

import java.util.*;

public class ListUtil {
    public static List<String> getColorizedStringList(List<String> stringList) {
        for (int i = 0; i < stringList.size() - 1; ++i) {
            stringList.set(i, TXT.parse(stringList.get(i)));
        }
        return stringList;
    }

    public static List<String> getColorizedStringList(String... strings) {
        if (strings == null || strings.length == 0) {
            return Lists.newArrayList();
        }
        List<String> colorizedList = Lists.newArrayList();
        Iterator<String> loop = Arrays.asList(strings).iterator();
        while (loop.hasNext())
            colorizedList.add(TXT.parse(loop.next()));

        return colorizedList;
    }

    public static <T> List<T> invertList(List<T> toInvert) {
        Collections.reverse(toInvert);
        return toInvert;
    }

    @SafeVarargs
    public static <T> List<T> getList(T... args) {
        List<T> l = new ArrayList<>();
        if (args == null) {
            return l;
        }
        Collections.addAll(l, args);
        return l;
    }

    public static List<String> getStringList(String... args) {
        List<String> l = new ArrayList<>();
        if (args == null) {
            return l;
        }
        for (String arg : args) {
            l.add(TXT.parse(arg));
        }
        return l;
    }

    public static String[] getStringArray(String... args) {
        return getStringList(args).toArray(new String[0]);
    }
}