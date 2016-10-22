package quoridor.ai.value_function;

import quoridor.core.state.GameState;


public interface ValueFunction {
    int apply(GameState gameState, int playerIx);
    int min();
    int max();
    default int maxTotal(int playersCount) {
        return playersCount * max();
    };
}
