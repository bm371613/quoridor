package quoridor.gui.event;

import lombok.Value;

import quoridor.core.Move;
import quoridor.gui.util.Highlightable;

@Value
public class MoveConsiderationEvent {
    private final Move move;
    private final Highlightable moveComponent;
}
