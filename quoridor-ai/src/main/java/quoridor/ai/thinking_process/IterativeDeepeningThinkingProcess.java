package quoridor.ai.thinking_process;

import java.util.Iterator;

import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public abstract class IterativeDeepeningThinkingProcess
        extends ThinkingProcess {

    private final ValueFunction valueFunction;
    private final GameState gameState;

    protected IterativeDeepeningThinkingProcess(ValueFunction valueFunction,
                                                GameState gameState) {
        this.valueFunction = valueFunction;
        this.gameState = gameState;
    }

    protected ValueFunction getValueFunction() {
        return valueFunction;
    }

    protected GameState getGameState() {
        return gameState;
    }

    protected abstract int estimate(Move move, int depth);

    @Override
    public void run() {
        int depth = 0;
        while (true) {
            Move bestMove = null;
            int bestValue = Integer.MIN_VALUE;
            int currentValue;
            Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
            Move move;
            while (moveIterator.hasNext()) {
                move = moveIterator.next();
                currentValue = estimate(move, depth);
                if (bestValue < currentValue) {
                    bestMove = move;
                    bestValue = currentValue;
                }
            }
            setResult(bestMove);
            if (bestValue == valueFunction.min()
                    || bestValue == valueFunction.max()) {
                break;
            }
            depth += 1;
        }
    }

}
