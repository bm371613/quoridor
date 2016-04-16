package quoridor.core.state;

import java.io.Serializable;

import lombok.Value;

import quoridor.core.direction.Directed;
import quoridor.core.direction.Direction;
import quoridor.core.position.Position;
import quoridor.core.position.Positioned;

@Value
public final class PlayerState implements Serializable, Directed, Positioned {
    private final Direction goal;
    private final Position position;
    private final int wallsLeft;

    private PlayerState(Direction goal, Position position, int wallsLeft) {
        this.goal = goal;
        this.position = position;
        this.wallsLeft = wallsLeft;
    }

    public static PlayerState of(Direction goal, int x, int y, int wallsLeft) {
        return new PlayerState(goal, Position.of(x, y), wallsLeft);
    }

    public PlayerState movedTo(int x, int y) {
        return new PlayerState(goal, Position.of(x, y), wallsLeft);
    }

    public PlayerState withWallsLeft(int wallsLeft) {
        return new PlayerState(goal, position, wallsLeft);
    }

    @Override
    public Direction getDirection() {
        return goal;
    }
}
