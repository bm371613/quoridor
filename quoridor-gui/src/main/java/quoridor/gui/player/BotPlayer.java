package quoridor.gui.player;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import quoridor.ai.Bot;
import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveChoiceEvent;

public class BotPlayer extends Player {

    private Bot bot;
    private Random random = new Random(System.currentTimeMillis());

    public BotPlayer(String name, Color color, Bot bot) {
        super(name, color);
        this.bot = bot;
    }

    @Override
    public void makeTurn(GameState gameState, EventListener moveEventListener) {
        // TODO
        List<Move> moves = GameRules.getLegalMoves(gameState);
        Move move = moves.get(random.nextInt(moves.size()));
        moveEventListener.notifyAboutEvent(this, new MoveChoiceEvent(move));
    }

    @Override
    public void moveAccepted() {
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
    }
}
