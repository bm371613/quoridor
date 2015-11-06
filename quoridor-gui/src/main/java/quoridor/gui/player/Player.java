package quoridor.gui.player;

import java.awt.Color;

import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;

public abstract class Player implements EventListener {

    private String name;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public abstract void makeTurn(GameState gameState,
                                  EventListener moveEventListener);
    public abstract void moveAccepted();
}
