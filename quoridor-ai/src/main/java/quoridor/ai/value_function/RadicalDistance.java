package quoridor.ai.value_function;

import quoridor.core.DistanceCalculator;
import quoridor.core.state.GameState;

import static quoridor.ai.value_function.Utils.distance;
import static quoridor.ai.value_function.Utils.minOtherPlayerDistance;

public class RadicalDistance implements ValueFunction {
    @Override
    public int apply(GameState gameState, int playerIx) {
        int myDistance = distance(gameState, playerIx);
        int otherDistance = minOtherPlayerDistance(gameState, playerIx);
        if (myDistance < otherDistance) {
            return Integer.MAX_VALUE
                    - myDistance * DistanceCalculator.DISTANCE_UPPER_BOUND
                    + (myDistance > 0 ? otherDistance : 0);
        } else if (myDistance == otherDistance) {
            return 0;
        } else {
            return Integer.MIN_VALUE
                + otherDistance * DistanceCalculator.DISTANCE_UPPER_BOUND
                - (otherDistance > 0 ? myDistance : 0);
        }
    }
}
