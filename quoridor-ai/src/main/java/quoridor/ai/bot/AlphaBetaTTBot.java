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
    private TranspositionTable<TwoPlayersAlphaBetaTTThinkingProcess.TTEntry>
            twoPlayerTable;
    private TranspositionTable<MultiPlayerAlphaBetaTTThinkingProcess.TTEntry>
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
                    valueFunction, gameState, hash, getTwoPlayerTable());
        } else {
            return new MultiPlayerAlphaBetaTTThinkingProcess(
                    valueFunction, gameState, hash, getMultiPlayerTable());
        }
    }

    private TranspositionTable<TwoPlayersAlphaBetaTTThinkingProcess.TTEntry>
            getTwoPlayerTable() {
        if (twoPlayerTable == null) {
            twoPlayerTable = new TranspositionTable<>(tableSize);
        }
        return twoPlayerTable;
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

    private final long gameStateHash;
    private IncrementalHash<GameState, Move> hash;
    private TranspositionTable<TTEntry> table;
    private final int me;
    private final int opponent;

    @Value(staticConstructor = "of")
    static final class TTEntry {
        private int depth;
        private int alpha;
        private int beta;
    }

    TwoPlayersAlphaBetaTTThinkingProcess(ValueFunction valueFunction,
                                         GameState gameState,
                                         IncrementalHash<GameState, Move> hash,
                                         TranspositionTable<TTEntry> table) {
        super(valueFunction, gameState);
        this.gameStateHash = hash.of(gameState);
        this.hash = hash;
        this.table = table;
        this.me = gameState.currentPlayerIx();
        this.opponent = 1 - me;
    }

    @Override
    protected int estimate(Move move, int depth) {
        ValueFunction valueFunction = getValueFunction();
        return estimate(getGameState(), gameStateHash, move, depth,
                valueFunction.min(), valueFunction.max());
    }

    private int estimate(GameState gameState, long hash,
                         Move move, int depth, int alpha, int beta) {
        long hashAfterMove = this.hash.after(gameState, hash, move);
        TTEntry entry = table.get(hashAfterMove);
        if (entry != null && entry.getDepth() >= depth) {
            alpha = Math.max(alpha, entry.getAlpha());
            beta = Math.min(beta, entry.getBeta());
            if (alpha >= beta) {
                return alpha;
            }
        }

        int result = estimate(move.apply(gameState), hashAfterMove, depth,
                alpha, beta);
        if (entry == null || entry.getDepth() <= depth) {
            ValueFunction valueFunction = getValueFunction();
            int ta = valueFunction.min();
            int tb = valueFunction.max();
            if (entry != null && entry.getDepth() >= depth) {
                ta = Math.max(ta, entry.getAlpha());
                tb = Math.min(tb, entry.getBeta());
            }
            if (result <= alpha) {
                tb = alpha;
            } else if (beta <= result) {
                ta = beta;
            } else {
                ta = result;
                tb = result;
            }
            table.set(hashAfterMove, TTEntry.of(depth, ta, tb));
        }
        return result;
    }

    private int estimate(GameState gameState, long hash, int depth,
                         int alpha, int beta) {
        if (depth < 1 || GameRules.isFinal(gameState)) {
            return getValueFunction().apply(gameState, me);
        }
        Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
        if (gameState.currentPlayerIx() == opponent) {
            while (moveIterator.hasNext()) {
                beta = Math.min(
                        beta,
                        estimate(gameState, hash, moveIterator.next(),
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
                        estimate(gameState, hash, moveIterator.next(),
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
        this.initialBound = valueFunction.max();
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
