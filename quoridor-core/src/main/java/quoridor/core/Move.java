package quoridor.core;

import lombok.Getter;

import quoridor.core.position.Position;
import quoridor.core.position.Positioned;
import quoridor.core.state.WallOrientation;

public final class Move implements Positioned {

    @Getter private final Position position;
    @Getter private final boolean isPawnMove;
    @Getter private final WallOrientation wallOrientation;

    private Move(boolean isPawnMove, Position position,
                 WallOrientation wallOrientation) {
        this.position = position;
        this.isPawnMove = isPawnMove;
        this.wallOrientation = wallOrientation;
    }

    public static Move makePawnMove(int x, int y) {
        return new Move(true, Position.of(x, y), null);
    }

    public static Move makeWallMove(int x, int y,
            WallOrientation wallOrientation) {
        if (wallOrientation == null) {
            throw new RuntimeException("Wall move must define orientation");
        }
        return new Move(false, Position.of(x, y), wallOrientation);
    }

    public boolean isWallMove() {
        return !isPawnMove();
    }
}
