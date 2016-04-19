package quoridor.ai.bot;

import quoridor.ai.ThinkingProcess;
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

class MinimaxThinkingProcess extends ThinkingProcess {

    private final ValueFunction valueFunction;
    private final GameState gameState;
    private final int playersCount;

    MinimaxThinkingProcess(ValueFunction valueFunction,
                                  GameState gameState) {
        this.valueFunction = valueFunction;
        this.gameState = gameState;
        this.playersCount = gameState.getPlayerStates().size();
    }

    private int[] estimate(GameState gameState, int depth) {
        int[] result = null;
        if (depth < 1 || GameRules.isFinal(gameState)) {
            result = new int[playersCount];
            for (int i = 0; i < playersCount; ++i) {
                result[i] = valueFunction.apply(gameState, i);
            }
        } else {
            int[] currentValue;
            int playerIx = gameState.currentPlayerIx();
            for (Move move : GameRules.getLegalMoves(gameState)) {
                currentValue = estimate(move.apply(gameState), depth - 1);
                if (result == null
                        || result[playerIx] < currentValue[playerIx]) {
                    result = currentValue;
                }
            }
        }
        return result;
    }

    private Move choose(int depth) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int currentValue;
        for (Move move : GameRules.getLegalMoves(gameState)) {
            currentValue = estimate(move.apply(gameState), depth)
                    [gameState.currentPlayerIx()];
            if (bestValue < currentValue) {
                bestMove = move;
                bestValue = currentValue;
            }
        }
        return bestMove;
    }

    @Override
    public void run() {
        int depth = 0;
        while (true) {
            setResult(choose(depth));
            depth += 1;
        }
    }
}
