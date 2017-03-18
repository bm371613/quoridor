package quoridor.ai.bot;

import java.util.Iterator;

import quoridor.ai.thinking_process.IterativeDeepeningThinkingProcess;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public class AlphaBetaBot implements Bot {

    private final ValueFunction valueFunction;

    public AlphaBetaBot(ValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    @Override
    public final ThinkingProcess thinkAbout(GameState gameState) {
        if (gameState.getPlayerStates().size() == 2) {
            return new TwoPlayersAlphaBetaThinkingProcess(
                    valueFunction, gameState);
        } else {
            return new MultiPlayerAlphaBetaThinkingProcess(
                    valueFunction, gameState);
        }
    }
}

class TwoPlayersAlphaBetaThinkingProcess
        extends IterativeDeepeningThinkingProcess {

    private final int me;
    private final int opponent;

    TwoPlayersAlphaBetaThinkingProcess(ValueFunction valueFunction,
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

class MultiPlayerAlphaBetaThinkingProcess
        extends IterativeDeepeningThinkingProcess {

    private final int playersCount;
    private final int maxTotal;
    private final int initialBound;

    MultiPlayerAlphaBetaThinkingProcess(ValueFunction valueFunction,
                           GameState gameState) {
        super(valueFunction, gameState);
        this.playersCount = gameState.getPlayerStates().size();
        this.maxTotal = valueFunction.maxTotal(playersCount);
        this.initialBound = valueFunction.max();
    }

    @Override
    protected int estimate(Move move, int depth) {
        GameState gameState = getGameState();
        return estimate(move.apply(gameState), depth, initialBound)
                [gameState.currentPlayerIx()];
    }

    private int[] estimate(GameState gameState, int depth, int bound) {
        if (depth < 1 || GameRules.isFinal(gameState)) {
            ValueFunction valueFunction = getValueFunction();
            int[] result = new int[playersCount];
            for (int i = 0; i < playersCount; ++i) {
                result[i] = valueFunction.apply(gameState, i);
            }
            return result;
        } else {
            Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
            int[] best = estimate(moveIterator.next().apply(gameState),
                    depth - 1, initialBound);
            int[] currentValue;
            int playerIx = gameState.currentPlayerIx();
            while (moveIterator.hasNext()) {
                if (best[playerIx] >= bound) {
                    break;
                }
                currentValue = estimate(moveIterator.next().apply(gameState),
                        depth - 1, maxTotal - best[playerIx]);
                if (best[playerIx] < currentValue[playerIx]) {
                    best = currentValue;
                }
            }
            return best;
        }
    }

}
