package quoridor.core.state;

import quoridor.core.util.Positioned;

public class PlayerState implements Positioned {
    private int x;
    private int y;
    private int wallsLeft;

    public PlayerState(int x, int y, int wallsLeft) {
        if (x < 0 || y < 0 || x >= GameState.PLACES || y >= GameState.PLACES) {
            throw new RuntimeException("Move coordinates out of bounds");
        }
        this.x = x;
        this.y = y;
        this.wallsLeft = wallsLeft;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getWallsLeft() {
        return wallsLeft;
    }
}
