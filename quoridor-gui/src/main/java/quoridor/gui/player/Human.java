package quoridor.gui.player;

import quoridor.gui.event.EventListener;

import java.awt.Color;

public final class Human extends Player {

    private EventListener moveEventListener;

    public Human(String name, Color color) {
        super(name, color);
    }

    @Override
    public void makeTurn(EventListener moveEventListener) {
        if (isMakingTurn()) {
            throw new RuntimeException("Player already making a turn!");
        }
        this.moveEventListener = moveEventListener;
    }

    @Override
    public void moveAccepted() {
        this.moveEventListener = null;
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
        if (isMakingTurn()) {
            this.moveEventListener.notifyAboutEvent(this, event);
        }
    }

    private boolean isMakingTurn() {
        return this.moveEventListener != null;
    }
}
