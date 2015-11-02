package quoridor.gui.event;

import quoridor.gui.component.board.Place;

public class PawnMoveConsiderationEvent {
    int x;
    int y;
    Place place;

    public PawnMoveConsiderationEvent(int x, int y, Place place) {
        this.x = x;
        this.y = y;
        this.place = place;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Place getPlace() {
        return place;
    }
}
