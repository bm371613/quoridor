package quoridor.core.position;

import lombok.Value;

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
}
