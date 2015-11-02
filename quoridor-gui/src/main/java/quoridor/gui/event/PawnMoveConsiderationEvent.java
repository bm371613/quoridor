package quoridor.gui.event;

public class PawnMoveConsiderationEvent {
    int x;
    int y;

    public PawnMoveConsiderationEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
