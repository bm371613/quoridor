package quoridor.gui.util;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import quoridor.core.state.Goal;
import quoridor.core.state.PlayerState;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PerPlayer<V> {
    private  List<V> values = Lists.newArrayList(null, null, null, null);

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

    public static final class Entry<V> {
        private final Goal goal;
        private final V value;

        private Entry(Goal goal, V value) {
            this.goal = goal;
            this.value = value;
        }

        public Goal getGoal() {
            return goal;
        }

        public V getValue() {
            return value;
        }
    }
}
