package quoridor.ai.value_function;

import quoridor.core.state.GameState;


public interface ValueFunction {
    int apply(GameState gameState, int playerIx);
}
