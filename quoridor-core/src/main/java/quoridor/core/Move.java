package quoridor.core;

import lombok.Getter;

import quoridor.core.state.WallOrientation;
import quoridor.core.util.Positioned;

public final class Move implements Positioned {

    @Getter private final int x;
    @Getter private final int y;
    @Getter private final boolean isPawnMove;
    @Getter private final WallOrientation wallOrientation;

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

    public boolean isWallMove() {
        return !isPawnMove();
    }
}
