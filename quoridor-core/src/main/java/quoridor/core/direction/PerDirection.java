package quoridor.core.direction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import lombok.Value;

public class PerDirection<V> {
    private final List<V> values;

    public PerDirection() {
        values = Lists.newArrayListWithCapacity(Direction.values().length);
        for (int i = 0; i < Direction.values().length; ++i) {
            values.add(null);
        }
    }

    public V get(Directed directed) {
        return values.get(directed.getDirection().ordinal());
    }

    public PerDirection<V> set(Directed directed, V value) {
        values.set(directed.getDirection().ordinal(), value);
        return this;
    }

    public static <V> PerDirection<V> of(Function <Direction, V> function) {
        PerDirection<V> result = new PerDirection<>();
        for (Direction direction : Direction.values()) {
            result.set(direction, function.apply(direction));
        }
        return result;
    }

    public void forEachValue(Consumer<V> consumer) {
        values.forEach(consumer::accept);
    }

    public void forEachEntry(Consumer<Entry<V>> consumer) {
        for (Direction direction : Direction.values()) {
            consumer.accept(new Entry<>(direction, get(direction)));
        }
    }

    public <R> PerDirection<R> map(Function<V, R> function) {
        PerDirection<R> result = new PerDirection<>();
        for (Direction direction : Direction.values()) {
            result.set(direction, function.apply(get(direction)));
        }
        return result;
    }

    @Value
    public static final class Entry<V> implements Directed {
        private final Direction direction;
        private final V value;
    }
}
