package quoridor.gui.player;

import quoridor.core.GameRules;
import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveConsiderationEvent;

import java.awt.Color;

public final class Human extends Player {

    private GameState gameState;
    private EventListener moveEventListener;

    public Human(String name, Color color) {
        super(name, color);
    }

    @Override
    public void makeTurn(GameState gameState, EventListener moveEventListener) {
        if (isMakingTurn()) {
            throw new RuntimeException("Player already making a turn!");
        }
        this.gameState = gameState;
        this.moveEventListener = moveEventListener;
    }

    @Override
    public void moveAccepted() {
        this.gameState = null;
        this.moveEventListener = null;
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
        if (isMakingTurn()) {
            if (event instanceof MoveConsiderationEvent) {
                MoveConsiderationEvent mce = (MoveConsiderationEvent) event;
                mce.getMoveComponent().setHighlighted(
                        GameRules.isLegalMove(gameState, mce.getMove()));
            } else {
                this.moveEventListener.notifyAboutEvent(this, event);
            }
        }
    }

    private boolean isMakingTurn() {
        return this.moveEventListener != null;
    }
}
