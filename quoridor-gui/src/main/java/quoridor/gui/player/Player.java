package quoridor.gui.player;

import quoridor.gui.event.EventListener;

import java.awt.Color;

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

    public abstract void makeTurn(EventListener moveEventListener);
    public abstract void moveAccepted();
}
