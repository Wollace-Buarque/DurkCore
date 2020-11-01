package dev.cromo29.durkcore.SpecificUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class MapUtil {

    public static <K, V> Map<K, V> getMap(K key1, V value1, Object... objects) {

        Map<K, V> ret = new LinkedHashMap<>();

        ret.put(key1, value1);

        Iterator<Object> iter = Arrays.asList(objects).iterator();
        while (iter.hasNext()) {
            K key = (K) iter.next();
            V value = (V) iter.next();
            ret.put(key, value);
        }

        return ret;
    }
    public static <V, K> Map<V, K> invertMap(Map<K, V> toInvert) {
        Map<V, K> out = new HashMap(toInvert.size());
        Iterator var2 = toInvert.entrySet().iterator();
        while(var2.hasNext()) {
            Map.Entry<K, V> entry = (Map.Entry)var2.next();
            out.put(entry.getValue(), entry.getKey());
        }
        return out;
    }

    public static <K> Map<K, String> orderByValueString(Map<K, String> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, Double> orderByValueDouble(Map<K, Double> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, BigInteger> orderByValueBigInteger(Map<K, BigInteger> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, BigDecimal> orderByValueBigDecimal(Map<K, BigDecimal> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, Integer> orderByValueInt(Map<K, Integer> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, String> reverseByValueString(Map<K, String> toReverse) {
        return toReverse
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, Double> reverseByValueDouble(Map<K, Double> toReverse) {
        return toReverse
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, BigDecimal> reverseByValueBigDecimal(Map<K, BigDecimal> toReverse) {
        return toReverse
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, BigInteger> reverseByValueBigInteger(Map<K, BigInteger> toReverse) {
        return toReverse
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> Map<K, Integer> reverseByValueInt(Map<K, Integer> toReverse) {
        return toReverse
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <V> Map<String, V> orderByKeyString(Map<String, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <V> Map<BigInteger, V> orderByKeyBigInteger(Map<BigInteger, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <V> Map<BigDecimal, V> orderByKeyBigDecimal(Map<BigDecimal, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <V> Map<Double, V> orderByKeyDouble(Map<Double, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <V> Map<String, V> reverseByKeyString(Map<String, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <V> Map<Double, V> reverseByKeyDouble(Map<Double, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
    public static <V> Map<Integer, V> orderByKeyInt(Map<Integer, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
    public static <V> Map<Integer, V> reverseByKeyInt(Map<Integer, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
    public static <V> Map<BigInteger, V> reverseByKeyBigInteger(Map<BigInteger, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
    public static <V> Map<BigDecimal, V> reverseByKeyBigDecimal(Map<BigDecimal, V> toOrder) {
        return toOrder
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
}
