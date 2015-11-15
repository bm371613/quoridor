package quoridor.gui.event;

import lombok.Value;

import quoridor.gui.Game;

@Value
public class LoadGameEvent {
    private final Game game;
}
