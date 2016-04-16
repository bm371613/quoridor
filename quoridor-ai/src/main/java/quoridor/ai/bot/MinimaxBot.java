package quoridor.ai.bot;

import java.util.Arrays;

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
        if (depth < 1) {
            int[] result = new int[playersCount];
            for (int i = 0; i < playersCount; ++i) {
                result[i] = valueFunction.apply(gameState, i);
            }
            return result;
        }

        int[] bestValue = new int[playersCount];
        int[] currentValue;
        int playerIx = gameState.currentPlayerIx();
        Arrays.fill(bestValue, Integer.MIN_VALUE);
        for (Move move : GameRules.getLegalMoves(gameState)) {
            currentValue = estimate(move.apply(gameState), depth - 1);
            if (bestValue[playerIx] < currentValue[playerIx]) {
                bestValue = currentValue;
            }
        }
        return bestValue;
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
        int depth = 1;
        while (true) {
            setResult(choose(depth));
            depth *= 2;
        }
    }
}
