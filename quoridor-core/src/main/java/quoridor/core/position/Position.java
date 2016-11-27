package quoridor.core.position;

import java.io.Serializable;

import lombok.Value;

import quoridor.core.direction.Directed;
import quoridor.core.state.GameState;

@Value
public final class Position implements Serializable, Positioned {

    private static Position[][] positions =
            new Position[GameState.PLACES + 2][GameState.PLACES + 2];

    static {
        for (int x = -1; x <= GameState.PLACES; ++x) {
            for (int y = -1; y <= GameState.PLACES; ++y) {
                positions[x + 1][y + 1] = new Position(x, y);
            }
        }
    }

    private final int x;
    private final int y;

    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y) {
        return positions[x + 1][y + 1];
    }

    @Override
    public Position getPosition() {
        return this;
    }

    public static Position next(Positioned p, Directed d) {
        switch (d.getDirection()) {
            case UP:
                return of(p.getX(), p.getY() + 1);
            case DOWN:
                return of(p.getX(), p.getY() - 1);
            case LEFT:
                return of(p.getX() - 1, p.getY());
            case RIGHT:
                return of(p.getX() + 1, p.getY());
            default:
                return null;
        }
    }
}
