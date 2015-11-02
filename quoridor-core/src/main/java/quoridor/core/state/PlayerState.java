package quoridor.core.state;

import quoridor.core.util.Positioned;

public class PlayerState implements Positioned {
    private Goal goal;
    private int x;
    private int y;
    private int wallsLeft;

    public PlayerState(Goal goal, int x, int y, int wallsLeft) {
        if (x < 0 || y < 0 || x >= GameState.PLACES || y >= GameState.PLACES) {
            throw new RuntimeException("Move coordinates out of bounds");
        }
        this.goal = goal;
        this.x = x;
        this.y = y;
        this.wallsLeft = wallsLeft;
    }

    public Goal getGoal() {
        return goal;
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

    public PlayerState movedTo(int x, int y) {
        return new PlayerState(goal, x, y, wallsLeft);
    }

    public PlayerState withWallsLeft(int wallsLeft) {
        return new PlayerState(goal, x, y, wallsLeft);
    }
}
