package quoridor.ai.bot;

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
    private final TranspositionTable table;

    public MinimaxTTBot(ValueFunction valueFunction,
                        IncrementalHash<GameState, Move> hash,
                        int tableSize) {
        this.valueFunction = valueFunction;
        this.hash = hash;
        this.table = new TranspositionTable(tableSize);
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
    private final TranspositionTable table;

    MinimaxTTThinkingProcess(ValueFunction valueFunction,
                             GameState gameState,
                             IncrementalHash<GameState, Move> hash,
                             TranspositionTable table) {
        this.valueFunction = valueFunction;
        this.gameState = gameState;
        this.playersCount = gameState.getPlayerStates().size();
        this.hash = hash;
        this.table = table;
    }

    private int[] estimate(GameState gameState, long hash,
                           Move move, int depth) {
        long hashAfterMove = this.hash.after(gameState, hash, move);
        if (this.table.has(hashAfterMove, depth)) {
            return this.table.get(hashAfterMove);
        }
        int[] result = estimate(move.apply(gameState), hashAfterMove, depth);
        this.table.set(hashAfterMove, depth, result);
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
        return bestMove;
    }

}

class TranspositionTable {
    private int size;
    private final long[] hash;
    private final int[][] value;
    private final int[] depth;

    TranspositionTable(int size) {
        this.size = size;
        this.hash = new long[size];
        this.value = new int[size][4];
        this.depth = new int[size];
        for (int i = 0; i < size; ++i) {
            this.depth[i] = -1;
        }
    }

    boolean has(long hash, int depth) {
        int ix = this.ix(hash);
        return this.hash[ix] == hash && this.depth[ix] >= depth;
    }

    int[] get(long hash) {
        return this.value[ix(hash)];
    }

    void set(long hash, int depth, int[] value) {
        int ix = this.ix(hash);
        this.hash[ix] = hash;
        this.value[ix] = value;
        this.depth[ix] = depth;
    }

    private int ix(long hash) {
        int result = (int) (hash % size);
        return result < 0 ? result + size : result;
    }
}
