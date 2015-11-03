package quoridor.gui.event;

import quoridor.core.Move;

public class MoveConsiderationEvent {

    private Move move;

    public MoveConsiderationEvent(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }
}
