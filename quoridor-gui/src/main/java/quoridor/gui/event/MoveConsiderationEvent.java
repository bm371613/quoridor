package quoridor.gui.event;

import quoridor.core.Move;
import quoridor.gui.util.Highlightable;

public class MoveConsiderationEvent {

    private Move move;
    private Highlightable moveComponent;

    public MoveConsiderationEvent(Move move, Highlightable moveComponent) {
        this.move = move;
        this.moveComponent = moveComponent;
    }

    public Move getMove() {
        return move;
    }

    public Highlightable getMoveComponent() {
        return moveComponent;
    }
}
