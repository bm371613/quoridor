package quoridor.ai.bot;

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
            // TODO
            throw new RuntimeException("Not supported");
        }
    }
}

class TwoPlayersAlphaBetaThinkingProcess
        extends IterativeDeepeningThinkingProcess {

    private final ValueFunction valueFunction;
    private final GameState gameState;
    private final int me;
    private final int opponent;

    TwoPlayersAlphaBetaThinkingProcess(ValueFunction valueFunction,
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

