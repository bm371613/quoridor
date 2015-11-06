package quoridor.gui.event;

import lombok.Value;

import quoridor.core.Move;

@Value
public class MoveChoiceEvent {
    private final Move move;
}
