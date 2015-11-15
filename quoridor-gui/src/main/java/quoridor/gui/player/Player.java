package quoridor.gui.player;

import java.awt.Color;

import lombok.Getter;

import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;

public abstract class Player implements EventListener {

    @Getter private final String name;
    @Getter private final Color color;
    @Getter private final boolean isHuman;

    public Player(String name, Color color, boolean isHuman) {
        this.name = name;
        this.color = color;
        this.isHuman = isHuman;
    }

    public Player(String name, Color color) {
        this(name, color, false);
    }

    public abstract void makeTurn(GameState gameState,
                                  EventListener moveEventListener);
    public abstract void moveAccepted();
    public abstract void moveCancelled();
}
