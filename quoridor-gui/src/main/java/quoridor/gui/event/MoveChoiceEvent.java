package quoridor.gui.event;

import quoridor.core.Move;

public class MoveChoiceEvent {

    private Move move;

    public MoveChoiceEvent(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }
}
