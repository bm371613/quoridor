package quoridor.gui.player;

import java.awt.Color;
import javax.swing.SwingUtilities;

import quoridor.ai.Bot;
import quoridor.ai.ThinkingProcess;
import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveChoiceEvent;

public class BotPlayer extends Player {

    private final Bot bot;

    public BotPlayer(String name, Color color, Bot bot) {
        super(name, color);
        this.bot = bot;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void makeTurn(GameState gameState, EventListener moveEventListener) {
        Player player = this;
        ThinkingProcess thinkingProcess = bot.thinkAbout(gameState);
        new Thread(() -> {
            Thread t = new Thread(thinkingProcess, getName() + " thinking");
            t.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            t.stop();
            Move move = thinkingProcess.getResult();
            if (move == null) {
                move = GameRules.getLegalMoves(gameState).get(0);
            }
            final MoveChoiceEvent event = new MoveChoiceEvent(move);
            SwingUtilities.invokeLater(() -> moveEventListener.notifyAboutEvent(
                    player, event));
        }, getName()).start();
    }

    @Override
    public void moveAccepted() {
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
    }
}
