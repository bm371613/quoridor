package quoridor.analysis;

import quoridor.ai.bot.Bot;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public class GameRunner {
    private final int turnTime;
    private final int maxTurnCount;
    private final int verbosity;

    private Thread outerThread;
    private Thread innerThread;

    public GameRunner(int turnTime, int maxTurnCount, int verbosity) {
        this.turnTime = turnTime;
        this.maxTurnCount = maxTurnCount;
        this.verbosity = verbosity;
    }

    public int run(Bot bot0, Bot bot1) {
        GameState gameState = GameRules.makeInitialStateForTwo();
        return run(gameState, bot0, bot1);
    }

    public int run(Bot bot0, Bot bot1, Bot bot2, Bot bot3) {
        GameState gameState = GameRules.makeInitialStateForFour();
        return run(gameState, bot0, bot1, bot2, bot3);
    }

    private int run(GameState gameState, Bot... bots) {
        for (int i = 0; i < maxTurnCount; ++i) {
            if (verbosity >= 1) {
                System.out.print(gameState.toPrettyString());
            }
            if (GameRules.isFinal(gameState)) {
                return GameRules.getWinner(gameState);
            }
            gameState = makeMove(gameState, bots[gameState.currentPlayerIx()]);
        }
        return -1;
    }

    @SuppressWarnings("deprecation")
    private GameState makeMove(GameState gameState, Bot bot) {
        ThinkingProcess thinkingProcess = bot.thinkAbout(gameState);
        outerThread = new Thread(() -> {
            innerThread = new Thread(thinkingProcess);
            System.gc();
            innerThread.start();
            try {
                Thread.sleep(turnTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            innerThread.stop();
        });
        outerThread.start();
        try {
            outerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Move move = thinkingProcess.getResult();
        if (move == null) {
            move = GameRules.getLegalMoves(gameState).next();
        }
        return move.apply(gameState);
    }
}
