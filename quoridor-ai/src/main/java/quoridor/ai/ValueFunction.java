package quoridor.ai;

import quoridor.core.state.GameState;

public interface ValueFunction {
    int apply(GameState gameState, int playerIx);
}
