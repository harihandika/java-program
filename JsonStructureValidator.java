import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class JsonStructureValidator {

    private JsonStructureValidator() {
    }

    /**
     * @param input any object
     * @return true if input is a Map with String keys and all nested elements are
     *         Map/List/array/primitives; false otherwise
     */
    public static boolean isValidJson(Object input) {
        if (!(input instanceof Map)) {
            return false;
        }
        IdentityHashMap<Object, Boolean> visited = new IdentityHashMap<>();
        return isValidMap((Map<?, ?>) input, visited);
    }

    private static boolean isValidMap(Map<?, ?> map, IdentityHashMap<Object, Boolean> visited) {
        if (visited.containsKey(map)) {
            return false;
        }
        visited.put(map, Boolean.TRUE);

        for (Map.Entry<?, ?> e : map.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();

            if (!(key instanceof String)) {
                return false;
            }

            if (!isValidValue(value, visited)) {
                return false;
            }
        }

        visited.remove(map);
        return true;
    }

    private static boolean isValidValue(Object value, IdentityHashMap<Object, Boolean> visited) {
        if (value == null)
            return true;

        if (value instanceof Map) {
            return isValidMap((Map<?, ?>) value, visited);
        }

        if (value instanceof List) {
            if (visited.containsKey(value))
                return false;
            visited.put(value, Boolean.TRUE);

            for (Object elem : (List<?>) value) {
                if (!isValidValue(elem, visited)) {
                    visited.remove(value);
                    return false;
                }
            }

            visited.remove(value);
            return true;
        }

        if (value.getClass().isArray()) {
            if (visited.containsKey(value))
                return false;
            visited.put(value, Boolean.TRUE);

            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                Object elem = Array.get(value, i);
                if (!isValidValue(elem, visited)) {
                    visited.remove(value);
                    return false;
                }
            }

            visited.remove(value);
            return true;
        }

        if (value instanceof String || value instanceof Number || value instanceof Boolean) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        Map<String, Object> valid = Map.of(
                "user", Map.of("name", "A", "age", 30));

        Map<Object, Object> invalidKey = new HashMap<>();
        invalidKey.put(123, "bad key");

        Map<String, Object> invalidType = new HashMap<>();
        invalidType.put("ok", new Object());

        List<Object> arr = Arrays.asList("a", 1, true, null);
        Map<String, Object> withArray = new HashMap<>();
        withArray.put("items", arr);

        Map<String, Object> a = new HashMap<>();
        Map<String, Object> b = new HashMap<>();
        a.put("b", b);
        b.put("a", a); // cycle

        System.out.println("valid -> expected true: " + isValidJson(valid));
        System.out.println("invalidKey -> expected false: " + isValidJson(invalidKey));
        System.out.println("invalidType -> expected false: " + isValidJson(invalidType));
        System.out.println("withArray -> expected true: " + isValidJson(withArray));
        System.out.println("cycle -> expected false: " + isValidJson(a));
        System.out.println("top-level array -> expected false: " + isValidJson(arr));
    }
}