package quoridor.ai.bot;

import java.util.Iterator;

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

    private final int me;
    private final int opponent;

    TwoPlayersAlphaBetaTTThinkingProcess(ValueFunction valueFunction,
                                       GameState gameState) {
        super(valueFunction, gameState);
        this.me = gameState.currentPlayerIx();
        this.opponent = 1 - me;
    }

    @Override
    protected int estimate(Move move, int depth) {
        ValueFunction valueFunction = getValueFunction();
        return estimate(move.apply(getGameState()), depth,
                valueFunction.min(), valueFunction.max());
    }

    private int estimate(GameState gameState, int depth, int alpha, int beta) {
        if (depth < 1 || GameRules.isFinal(gameState)) {
            return getValueFunction().apply(gameState, me);
        }
        Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
        if (gameState.currentPlayerIx() == opponent) {
            while (moveIterator.hasNext()) {
                beta = Math.min(
                        beta,
                        estimate(moveIterator.next().apply(gameState),
                                depth - 1, alpha, beta)
                );
                if (alpha >= beta) {
                    break;
                }
            }
            return beta;
        } else {
            while (moveIterator.hasNext()) {
                alpha = Math.max(
                        alpha,
                        estimate(moveIterator.next().apply(gameState),
                                depth - 1, alpha, beta)
                );
                if (alpha >= beta) {
                    break;
                }
            }
            return alpha;
        }
    }

}

class MultiPlayerAlphaBetaTTThinkingProcess
        extends IterativeDeepeningThinkingProcess {

    private final long gameStateHash;
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
        super(valueFunction, gameState);
        this.gameStateHash = hash.of(gameState);
        this.playersCount = gameState.getPlayerStates().size();
        this.hash = hash;
        this.table = table;
        this.maxTotal = valueFunction.maxTotal(playersCount);
        this.initialBound = maxTotal - valueFunction.min();
    }

    @Override
    protected int estimate(Move move, int depth) {
        GameState gameState = getGameState();
        return estimate(gameState, gameStateHash, move, depth, initialBound)
                [gameState.currentPlayerIx()];
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
            ValueFunction valueFunction = getValueFunction();
            int[] result = new int[playersCount];
            for (int i = 0; i < playersCount; ++i) {
                result[i] = valueFunction.apply(gameState, i);
            }
            return result;
        } else {
            Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
            int[] best = estimate(gameState, hash, moveIterator.next(),
                    depth - 1, initialBound);
            int[] currentValue;
            int playerIx = gameState.currentPlayerIx();
            while (moveIterator.hasNext()) {
                if (best[playerIx] >= bound) {
                    break;
                }
                currentValue = estimate(gameState, hash, moveIterator.next(),
                        depth - 1, maxTotal - best[playerIx]);
                if (best[playerIx] < currentValue[playerIx]) {
                    best = currentValue;
                }
            }
            return best;
        }
    }

}
