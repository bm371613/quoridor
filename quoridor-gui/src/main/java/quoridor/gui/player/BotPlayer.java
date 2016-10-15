package quoridor.gui.player;

import java.awt.Color;
import javax.swing.SwingUtilities;

import quoridor.ai.bot.Bot;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;
import quoridor.gui.event.EventListener;
import quoridor.gui.event.MoveChoiceEvent;

public class BotPlayer extends Player {

    private final Bot bot;
    private Thread outerThread;
    private Thread innerThread;

    public BotPlayer(String name, Color color, Bot bot) {
        super(name, color);
        this.bot = bot;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void makeTurn(GameState gameState, EventListener moveEventListener) {
        Player player = this;
        ThinkingProcess thinkingProcess = bot.thinkAbout(gameState);
        outerThread = new Thread(() -> {
            innerThread = new Thread(thinkingProcess, getName() + " thinking");
            System.gc();
            innerThread.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            innerThread.stop();
            Move move = thinkingProcess.getResult();
            if (move == null) {
                move = GameRules.getLegalMoves(gameState).get(0);
            }
            final MoveChoiceEvent event = new MoveChoiceEvent(move);
            SwingUtilities.invokeLater(() -> moveEventListener.notifyAboutEvent(
                    player, event));
        }, getName());
        outerThread.start();
    }

    @Override
    public void moveAccepted() {
    }

    @Override
    @SuppressWarnings("deprecation")
    public void moveCancelled() {
        if (outerThread.isAlive()) {
            outerThread.stop();
        }
        if (innerThread.isAlive()) {
            innerThread.stop();
        }
    }

    @Override
    public void notifyAboutEvent(Object source, Object event) {
    }
}
