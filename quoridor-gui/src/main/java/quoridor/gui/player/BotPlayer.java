package quoridor.gui.player;

import quoridor.ai.Bot;
import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveChoiceEvent;

import java.awt.Color;

public class BotPlayer extends Player {

    Bot bot;

    public BotPlayer(String name, Color color, Bot bot) {
        super(name, color);
        this.bot = bot;
    }

    @Override
    public void makeTurn(GameState gameState, EventListener moveEventListener) {
        // TODO
        Move move = GameRules.getLegalMoves(gameState).iterator().next();
        moveEventListener.notifyAboutEvent(this, new MoveChoiceEvent(move));
    }

    @Override
    public void moveAccepted() {
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
    }
}
