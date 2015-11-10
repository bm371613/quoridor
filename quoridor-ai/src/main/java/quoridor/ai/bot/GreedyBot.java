package quoridor.ai.bot;

import quoridor.ai.ThinkingProcess;
import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;

public class GreedyBot implements Bot {

    private final ValueFunction valueFunction;

    public GreedyBot(ValueFunction valueFunction) {
        this.valueFunction = valueFunction;
    }

    @Override
    public final ThinkingProcess thinkAbout(GameState gameState) {
        return new ThinkingProcess() {
            @Override
            public void run() {
                int bestValue = Integer.MIN_VALUE;
                int currentValue;
                int playerIx = gameState.currentPlayerIx();
                for (Move move : GameRules.getLegalMoves(gameState)) {
                    currentValue = valueFunction.apply(gameState.apply(move),
                            playerIx);
                    if (getResult() == null || bestValue < currentValue) {
                        setResult(move);
                        bestValue = currentValue;
                    }
                }
            }
        };
    }
}
