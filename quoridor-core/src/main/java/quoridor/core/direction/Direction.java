package quoridor.core.direction;

public enum Direction implements Directed {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    @Override
    public Direction getDirection() {
        return this;
    }
}
