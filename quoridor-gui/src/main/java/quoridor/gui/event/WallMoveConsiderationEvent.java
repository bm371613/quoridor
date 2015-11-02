package quoridor.gui.event;

import quoridor.core.state.WallOrientation;
import quoridor.gui.component.board.Wall;

public class WallMoveConsiderationEvent {
    int x;
    int y;
    WallOrientation wallOrientation;

    public WallMoveConsiderationEvent(int x, int y,
                WallOrientation wallOrientation) {
        this.x = x;
        this.y = y;
        this.wallOrientation = wallOrientation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public WallOrientation getWallOrientation() {
        return wallOrientation;
    }
}
