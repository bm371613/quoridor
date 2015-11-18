package quoridor.gui.event;

import lombok.Value;

import quoridor.core.move.Move;

@Value
public class MoveChoiceEvent {
    private final Move move;
}
