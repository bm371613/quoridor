package quoridor.gui.util;

import com.google.common.collect.Lists;
import quoridor.core.state.Goal;
import quoridor.core.state.PlayerState;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PerPlayer<V> {
    List<V> values = Lists.newArrayList(null, null, null, null);

    public V get(Goal goal) {
        return values.get(goal.ordinal());
    }

    public PerPlayer<V> set(Goal goal, V value) {
        values.set(goal.ordinal(), value);
        return this;
    }

    public V get(PlayerState playerState) {
        return values.get(playerState.getGoal().ordinal());
    }

    public PerPlayer<V> set(PlayerState playerState, V value) {
        values.set(playerState.getGoal().ordinal(), value);
        return this;
    }

    public void forEach(Consumer<V> consumer) {
        values.forEach(consumer::accept);
    }

    public <R> PerPlayer<R> map(Function <V, R> function) {
        PerPlayer<R> result = new PerPlayer<>();
        for (Goal goal : Goal.values()) {
            result.set(goal, function.apply(get(goal)));
        }
        return result;
    }
}
