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

    abstract public void makeTurn(EventListener moveEventListener);
    abstract public void moveAccepted();
}
