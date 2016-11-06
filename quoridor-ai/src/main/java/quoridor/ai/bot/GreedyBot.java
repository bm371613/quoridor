package quoridor.ai.bot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public class GreedyBot implements Bot {

    private final ValueFunction valueFunction;

    private final Random random = new Random(System.currentTimeMillis());

    public GreedyBot(ValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    @Override
    public final ThinkingProcess thinkAbout(GameState gameState) {
        return new ThinkingProcess() {
            @Override
            public void run() {
                int bestValue = Integer.MIN_VALUE;
                int currentValue;
                int playerIx = gameState.currentPlayerIx();
                final List<Move> bestMoves = new ArrayList<>();
                Iterator<Move> moveIterator =
                        GameRules.getLegalMoves(gameState);
                Move move;
                while (moveIterator.hasNext()) {
                    move = moveIterator.next();
                    currentValue = valueFunction.apply(move.apply(gameState),
                            playerIx);
                    if (bestValue < currentValue) {
                        setResult(move);
                        bestMoves.clear();
                        bestValue = currentValue;
                    }
                    if (bestValue == currentValue) {
                        bestMoves.add(move);
                    }
                }
                setResult(bestMoves.get(random.nextInt(bestMoves.size())));
            }
        };
    }
}
