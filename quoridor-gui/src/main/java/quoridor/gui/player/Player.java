package quoridor.gui.player;

import java.awt.Color;

import lombok.Getter;

import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;

public abstract class Player implements EventListener {

    @Getter private final String name;
    @Getter private final Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public abstract void makeTurn(GameState gameState,
                                  EventListener moveEventListener);
    public abstract void moveAccepted();
}
