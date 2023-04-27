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
}
