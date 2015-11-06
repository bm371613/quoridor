package quoridor.gui.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import lombok.Value;

import quoridor.core.state.Goal;
import quoridor.core.state.PlayerState;

public class PerPlayer<V> {
    private final List<V> values = Lists.newArrayList(null, null, null, null);

    public V get(Goal goal) {
        return values.get(goal.ordinal());
    }

    public PerPlayer<V> set(Goal goal, V value) {
        values.set(goal.ordinal(), value);
        return this;
    }

    public static <V> PerPlayer<V> of(Function <Goal, V> function) {
        PerPlayer<V> result = new PerPlayer<>();
        for (Goal goal : Goal.values()) {
            result.set(goal, function.apply(goal));
        }
        return result;
    }

    public V get(PlayerState playerState) {
        return values.get(playerState.getGoal().ordinal());
    }

    public void forEachValue(Consumer<V> consumer) {
        values.forEach(consumer::accept);
    }

    public void forEachEntry(Consumer<Entry<V>> consumer) {
        for (Goal goal : Goal.values()) {
            consumer.accept(new Entry<>(goal, get(goal)));
        }
    }

    @Value
    public static final class Entry<V> {
        private final Goal goal;
        private final V value;
    }
}
