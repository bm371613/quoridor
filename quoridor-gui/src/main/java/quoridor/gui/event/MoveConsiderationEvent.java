package quoridor.gui.event;

import quoridor.core.Move;
import quoridor.gui.component.board.MoveComponent;

public class MoveConsiderationEvent {

    private Move move;
    private MoveComponent moveComponent;

    public MoveConsiderationEvent(Move move, MoveComponent moveComponent) {
        this.move = move;
        this.moveComponent = moveComponent;
    }

    public Move getMove() {
        return move;
    }

    public MoveComponent getMoveComponent() {
        return moveComponent;
    }
}
