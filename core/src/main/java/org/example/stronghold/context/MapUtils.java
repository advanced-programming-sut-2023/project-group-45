package org.example.stronghold.context;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapUtils {

    public static String getOpt(Map<String, String> map, String key) {
        return checkNotNull(map.get(key), "Missing option --" + key);
    }

    public static void copyOptTo(Map<String, String> from, Map<String, Object> to, String... keys) {
        for (String key : keys) {
            to.put(key, getOpt(from, key));
        }
    }

    public static boolean getBoolOpt(Map<String, String> map, String key) {
        if (!map.containsKey(key)) {
            return false;
        }
        String value = getOpt(map, key).toLowerCase();
        if (value.equals("true") || value.equals("yes")) {
            return true;
        }
        if (value.equals("false") || value.equals("no")) {
            return false;
        }
        throw new IllegalArgumentException(
            "Invalid boolean value for --" + key + ", got " + getOpt(map, key));
    }

    public static int getIntOpt(Map<String, String> map, String key) {
        try {
            return Integer.parseInt(getOpt(map, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid integer value for --" + key + ", got " + getOpt(map, key));
        }
    }

    public static IntPair getIntPairOpt(Map<String, String> map, String keyX, String keyY) {
        return new IntPair(getIntOpt(map, keyX), getIntOpt(map, keyY));
    }

    public static Object getReq(Map<String, Object> map, String key) {
        return checkNotNull(map.get(key), "Missing key in operator request: %s", key);
    }

    public static <T> T getReqAs(Map<String, Object> map, String key, Class<T> clazz) {
        Object obj = getReq(map, key);
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        throw new IllegalArgumentException("Object is not castable to " + clazz.getName());
    }

    public static String getReqString(Map<String, Object> map, String key) {
        return getReqAs(map, key, String.class);
    }

    public static <K> void addIntMap(Map<K, Integer> map, K key, int delta) {
        map.compute(key, (k, v) -> v == null ? delta : v + delta);
    }

    public static <K> void addIntMap(Map<K, Integer> map, Map<K, Integer> delta) {
        delta.forEach((k, v) -> addIntMap(map, k, v));
    }

    public static <K> void subtractIntMap(Map<K, Integer> map, Map<K, Integer> delta) {
        delta.forEach((k, v) -> addIntMap(map, k, -v));
    }

    public static <K> boolean geqIntMap(Map<K, Integer> map, Map<K, Integer> delta) {
        for (K key : delta.keySet()) {
            if (map.getOrDefault(key, 0) < delta.get(key)) {
                return false;
            }
        }
        return true;
    }

    public static <E> E randomChoice(List<E> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
