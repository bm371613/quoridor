package quoridor.gui.event;

import quoridor.core.state.WallOrientation;
import quoridor.gui.component.board.Wall;

public class WallMoveConsiderationEvent {

    int x;
    int y;
    WallOrientation wallOrientation;
    Wall wall;

    public WallMoveConsiderationEvent(int x, int y,
                WallOrientation wallOrientation, Wall wall) {
        this.x = x;
        this.y = y;
        this.wallOrientation = wallOrientation;
        this.wall = wall;
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

    public Wall getWall() {
        return wall;
    }
}
