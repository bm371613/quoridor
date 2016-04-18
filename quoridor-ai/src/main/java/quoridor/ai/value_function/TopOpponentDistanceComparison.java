package quoridor.ai.value_function;

import quoridor.core.DistanceCalculator;
import quoridor.core.GameRules;
import quoridor.core.state.GameState;

import static quoridor.ai.value_function.Utils.distance;
import static quoridor.ai.value_function.Utils.minOtherPlayerDistance;

public class TopOpponentDistanceComparison implements ValueFunction {
    @Override
    public int apply(GameState gameState, int playerIx) {
        if (GameRules.isWon(gameState.getPlayerStates().get(playerIx))) {
            return Integer.MAX_VALUE - (DistanceCalculator.DISTANCE_UPPER_BOUND
                    - minOtherPlayerDistance(gameState, playerIx));
        } else if (GameRules.isFinal(gameState)) {
            return Integer.MIN_VALUE + (DistanceCalculator.DISTANCE_UPPER_BOUND
                        - distance(gameState, playerIx));
        }
        return minOtherPlayerDistance(gameState, playerIx)
                - distance(gameState, playerIx);
    }
}
