package stronghold.context;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Data;

@Data
public class ListPair<K, V> {
    private final List<K> keys = new ArrayList<>();
    private final List<V> values = new ArrayList<>();

    public void put(K key, V value) {
        keys.add(key);
        values.add(value);
    }

    public int size() {
        return keys.size();
    }

    public void forEach(BiConsumer<K, V> action) {
        for (int i = 0; i < keys.size(); i++) {
            action.accept(keys.get(i), values.get(i));
        }
    }
}
