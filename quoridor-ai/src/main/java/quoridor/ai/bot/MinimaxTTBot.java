package quoridor.ai.bot;

import lombok.Value;

import quoridor.ai.hash.IncrementalHash;
import quoridor.ai.thinking_process.IterativeDeepeningThinkingProcess;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public class MinimaxTTBot implements Bot {

    private final ValueFunction valueFunction;
    private final IncrementalHash<GameState, Move> hash;
    private final TranspositionTable<MinimaxTTThinkingProcess.TTEntry> table;

    public MinimaxTTBot(ValueFunction valueFunction,
                        IncrementalHash<GameState, Move> hash,
                        int tableSize) {
        this.valueFunction = valueFunction;
        this.hash = hash;
        this.table = new TranspositionTable<>(tableSize);
    }

    @Override
    public final ThinkingProcess thinkAbout(GameState gameState) {
        return new MinimaxTTThinkingProcess(valueFunction, gameState,
                this.hash, this.table);
    }
}


class MinimaxTTThinkingProcess extends IterativeDeepeningThinkingProcess {

    private final ValueFunction valueFunction;
    private final GameState gameState;
    private final int playersCount;
    private final IncrementalHash<GameState, Move> hash;
    private final TranspositionTable<TTEntry> table;

    @Value(staticConstructor = "of")
    static final class TTEntry {
        private int depth;
        private int[] value;
    }

    MinimaxTTThinkingProcess(ValueFunction valueFunction,
                             GameState gameState,
                             IncrementalHash<GameState, Move> hash,
                             TranspositionTable<TTEntry> table) {
        this.valueFunction = valueFunction;
        this.gameState = gameState;
        this.playersCount = gameState.getPlayerStates().size();
        this.hash = hash;
        this.table = table;
    }

    private int[] estimate(GameState gameState, long hash,
                           Move move, int depth) {
        long hashAfterMove = this.hash.after(gameState, hash, move);
        TTEntry entry = table.get(hashAfterMove);
        if (entry != null && entry.getDepth() >= depth) {
            return table.get(hashAfterMove).value;
        }
        int[] result = estimate(move.apply(gameState), hashAfterMove, depth);
        table.set(hashAfterMove, TTEntry.of(depth, result));
        return result;
    }

    private int[] estimate(GameState gameState, long hash, int depth) {
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
                currentValue = estimate(gameState, hash, move, depth - 1);
                if (result == null
                        || result[playerIx] < currentValue[playerIx]) {
                    result = currentValue;
                }
            }
        }
        return result;
    }

    @Override
    protected Move choose(int depth) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int currentValue;
        long gameStateHash = this.hash.of(gameState);
        for (Move move : GameRules.getLegalMoves(gameState)) {
            currentValue = estimate(gameState, gameStateHash, move, depth)
                    [gameState.currentPlayerIx()];
            if (bestValue < currentValue) {
                bestMove = move;
                bestValue = currentValue;
            }
        }
        if (bestValue == valueFunction.min()
                || bestValue == valueFunction.max()) {
            stopDeepening();
        }
        return bestMove;
    }

}
