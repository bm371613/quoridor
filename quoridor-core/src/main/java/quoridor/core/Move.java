package quoridor.core;

import quoridor.core.state.WallOrientation;
import quoridor.core.util.Positioned;

public final class Move implements Positioned {

    private int x;
    private int y;
    private final boolean isPawnMove;
    private WallOrientation wallOrientation;

    private Move(boolean isPawnMove, int x, int y,
                 WallOrientation wallOrientation) {
        this.x = x;
        this.y = y;
        this.isPawnMove = isPawnMove;
        this.wallOrientation = wallOrientation;
    }

    public static Move makePawnMove(int x, int y) {
        return new Move(true, x, y, null);
    }

    public static Move makeWallMove(int x, int y,
            WallOrientation wallOrientation) {
        if (wallOrientation == null) {
            throw new RuntimeException("Wall move must define orientation");
        }
        return new Move(false, x, y, wallOrientation);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public boolean isPawnMove() {
        return isPawnMove;
    }

    public boolean isWallMove() {
        return !isPawnMove();
    }

    public WallOrientation getWallOrientation() {
        return wallOrientation;
    }
}
