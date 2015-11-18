package quoridor.ai.value_function;

import quoridor.core.state.GameState;

import static quoridor.ai.value_function.Utils.distance;
import static quoridor.ai.value_function.Utils.minOtherPlayerDistance;

public class SimpleDistance implements ValueFunction {
    @Override
    public int apply(GameState gameState, int playerIx) {
        return minOtherPlayerDistance(gameState, playerIx)
                - distance(gameState, playerIx);
    };
}
