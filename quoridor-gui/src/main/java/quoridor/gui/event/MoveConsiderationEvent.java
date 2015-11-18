package quoridor.gui.event;

import lombok.Value;

import quoridor.core.move.Move;
import quoridor.gui.util.Highlightable;

@Value
public class MoveConsiderationEvent {
    private final Move move;
    private final Highlightable moveComponent;
}
