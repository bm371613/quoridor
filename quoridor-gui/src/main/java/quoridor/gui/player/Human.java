package quoridor.gui.player;

import quoridor.gui.event.EventListener;

public final class Human implements Player {

    private EventListener moveEventListener;

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
