package quoridor.core.state;

import lombok.Getter;

import quoridor.core.util.Positioned;

public class PlayerState implements Positioned {
    @Getter(onMethod = @__({@Override})) private final int x;
    @Getter(onMethod = @__({@Override})) private final int y;

    @Getter private final Goal goal;
    @Getter private final int wallsLeft;

    public PlayerState(Goal goal, int x, int y, int wallsLeft) {
        this.goal = goal;
        this.x = x;
        this.y = y;
        this.wallsLeft = wallsLeft;
    }

    public PlayerState movedTo(int x, int y) {
        return new PlayerState(goal, x, y, wallsLeft);
    }

    public PlayerState withWallsLeft(int wallsLeft) {
        return new PlayerState(goal, x, y, wallsLeft);
    }
}
