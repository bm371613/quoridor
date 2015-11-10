package quoridor.core.direction;

public enum Direction implements Directed {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT;

    @Override
    public Direction getDirection() {
        return this;
    }
}
