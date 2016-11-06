package quoridor.ai.bot;

import java.util.List;

import lombok.Value;

import quoridor.ai.hash.IncrementalHash;
import quoridor.ai.thinking_process.IterativeDeepeningThinkingProcess;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public class AlphaBetaTTBot implements Bot {

    private final ValueFunction valueFunction;
    private final IncrementalHash<GameState, Move> hash;
    private final int tableSize;
    private TranspositionTable <MultiPlayerAlphaBetaTTThinkingProcess.TTEntry>
            multiPlayerTable;

    public AlphaBetaTTBot(ValueFunction valueFunction,
                          IncrementalHash<GameState, Move> hash,
                          int tableSize) {
        this.valueFunction = valueFunction;
        this.hash = hash;
        this.tableSize = tableSize;
    }

    @Override
    public final ThinkingProcess thinkAbout(GameState gameState) {
        if (gameState.getPlayerStates().size() == 2) {
            return new TwoPlayersAlphaBetaTTThinkingProcess(
                    valueFunction, gameState);
        } else {
            return new MultiPlayerAlphaBetaTTThinkingProcess(
                    valueFunction, gameState, hash, getMultiPlayerTable());
        }
    }

    private TranspositionTable<MultiPlayerAlphaBetaTTThinkingProcess.TTEntry>
            getMultiPlayerTable() {
        if (multiPlayerTable == null) {
            multiPlayerTable = new TranspositionTable<>(tableSize);
        }
        return multiPlayerTable;
    }
}

class TwoPlayersAlphaBetaTTThinkingProcess
        extends IterativeDeepeningThinkingProcess {

    private final ValueFunction valueFunction;
    private final GameState gameState;
    private final int me;
    private final int opponent;

    TwoPlayersAlphaBetaTTThinkingProcess(ValueFunction valueFunction,
                                       GameState gameState) {
        this.valueFunction = valueFunction;
        this.gameState = gameState;
        this.me = gameState.currentPlayerIx();
        this.opponent = 1 - me;
    }

    private int estimate(GameState gameState, int depth, int alpha, int beta) {
        if (depth < 1 || GameRules.isFinal(gameState)) {
            return valueFunction.apply(gameState, me);
        }
        if (gameState.currentPlayerIx() == opponent) {
            for (Move move : GameRules.getLegalMoves(gameState)) {
                beta = Math.min(
                        beta,
                        estimate(move.apply(gameState), depth - 1, alpha, beta)
                );
                if (alpha >= beta) {
                    break;
                }
            }
            return beta;
        } else {
            for (Move move : GameRules.getLegalMoves(gameState)) {
                alpha = Math.max(
                        alpha,
                        estimate(move.apply(gameState), depth - 1, alpha, beta)
                );
                if (alpha >= beta) {
                    break;
                }
            }
            return alpha;
        }
    }

    @Override
    protected Move choose(int depth) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int currentValue;
        for (Move move : GameRules.getLegalMoves(gameState)) {
            currentValue = estimate(
                    move.apply(gameState), depth,
                    valueFunction.min(), valueFunction.max());
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

class MultiPlayerAlphaBetaTTThinkingProcess
        extends IterativeDeepeningThinkingProcess {

    private final ValueFunction valueFunction;
    private final GameState gameState;
    private final int playersCount;
    private final IncrementalHash<GameState, Move> hash;
    private final TranspositionTable<TTEntry> table;
    private final int maxTotal;
    private final int initialBound;

    @Value(staticConstructor = "of")
    static final class TTEntry {
        private int depth;
        private final boolean exact;
        private final int[] value;
    }

    MultiPlayerAlphaBetaTTThinkingProcess(ValueFunction valueFunction,
                                          GameState gameState,
                                          IncrementalHash<GameState, Move> hash,
                                          TranspositionTable<TTEntry> table) {
        this.valueFunction = valueFunction;
        this.gameState = gameState;
        this.playersCount = gameState.getPlayerStates().size();
        this.hash = hash;
        this.table = table;
        this.maxTotal = valueFunction.maxTotal(playersCount);
        this.initialBound = maxTotal - valueFunction.min();
    }

    private int[] estimate(GameState gameState, long hash,
                           Move move, int depth, int bound) {
        int playerIx = gameState.currentPlayerIx();
        long hashAfterMove = this.hash.after(gameState, hash, move);
        TTEntry entry = table.get(hashAfterMove);
        if (entry != null && entry.getDepth() >= depth
                && (entry.isExact() || bound <= entry.getValue()[playerIx])) {
            return entry.getValue();
        }
        int[] result = estimate(move.apply(gameState), hashAfterMove,
                depth, bound);
        if (entry == null || entry.getDepth() <= depth) {
            table.set(hashAfterMove, TTEntry.of(
                    depth, result[playerIx] < bound, result
            ));
        }
        return result;
    }

    private int[] estimate(GameState gameState, long hash,
                           int depth, int bound) {
        if (depth < 1 || GameRules.isFinal(gameState)) {
            int[] result = new int[playersCount];
            for (int i = 0; i < playersCount; ++i) {
                result[i] = valueFunction.apply(gameState, i);
            }
            return result;
        } else {
            List<Move> moves = GameRules.getLegalMoves(gameState);
            int[] best = estimate(gameState, hash, moves.get(0), depth - 1,
                    initialBound);
            int[] currentValue;
            int playerIx = gameState.currentPlayerIx();
            for (int i = 1; i < moves.size(); ++i) {
                if (best[playerIx] >= bound) {
                    break;
                }
                currentValue = estimate(gameState, hash, moves.get(i),
                        depth - 1, maxTotal - best[playerIx]);
                if (best[playerIx] < currentValue[playerIx]) {
                    best = currentValue;
                }
            }
            return best;
        }
    }


    @Override
    protected Move choose(int depth) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int currentValue;
        long gameStateHash = hash.of(gameState);
        for (Move move : GameRules.getLegalMoves(gameState)) {
            currentValue = estimate(gameState, gameStateHash, move, depth,
                    initialBound)[gameState.currentPlayerIx()];
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
