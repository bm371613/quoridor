package quoridor.core.position;

import lombok.Value;

import quoridor.core.direction.Directed;

@Value
public final class Position implements Positioned {
    private final int x;
    private final int y;

    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y) {
        return new Position(x, y);
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
