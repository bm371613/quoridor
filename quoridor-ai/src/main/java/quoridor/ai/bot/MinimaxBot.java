package quoridor.ai.bot;

import java.util.Iterator;

import quoridor.ai.thinking_process.IterativeDeepeningThinkingProcess;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;


public class MinimaxBot implements Bot {

    private final ValueFunction valueFunction;

    public MinimaxBot(ValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    @Override
    public final ThinkingProcess thinkAbout(GameState gameState) {
        return new MinimaxThinkingProcess(valueFunction, gameState);
    }
}

final class MinimaxThinkingProcess extends IterativeDeepeningThinkingProcess {

    private final int playersCount;

    MinimaxThinkingProcess(ValueFunction valueFunction,
                                  GameState gameState) {
        super(valueFunction, gameState);
        this.playersCount = gameState.getPlayerStates().size();
    }

    @Override
    protected int estimate(Move move, int depth) {
        GameState gameState = getGameState();
        return estimate(move.apply(gameState), depth)
                [gameState.currentPlayerIx()];
    }

    private int[] estimate(GameState gameState, int depth) {
        int[] result = null;
        if (depth < 1 || GameRules.isFinal(gameState)) {
            ValueFunction valueFunction = getValueFunction();
            result = new int[playersCount];
            for (int i = 0; i < playersCount; ++i) {
                result[i] = valueFunction.apply(gameState, i);
            }
        } else {
            int[] currentValue;
            int playerIx = gameState.currentPlayerIx();
            Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
            while (moveIterator.hasNext()) {
                currentValue = estimate(moveIterator.next().apply(gameState),
                        depth - 1);
                if (result == null
                        || result[playerIx] < currentValue[playerIx]) {
                    result = currentValue;
                }
            }
        }
        return result;
    }

}
