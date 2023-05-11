package stronghold.context;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

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
        return Integer.parseInt(getOpt(map, key));
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

    public static <K> int addIntMap(Map<K, Integer> map, K key, int delta) {
        return map.compute(key, (k, v) -> v == null ? delta : v + delta);
    }

    public static <K> void addIntMap(Map<K, Integer> map, Map<K, Integer> delta) {
        delta.forEach((k, v) -> addIntMap(map, k, v));
    }
}
