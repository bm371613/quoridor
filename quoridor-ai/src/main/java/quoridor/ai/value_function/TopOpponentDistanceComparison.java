package quoridor.ai.value_function;

import quoridor.core.DistanceCalculator;
import quoridor.core.GameRules;
import quoridor.core.state.GameState;

import static quoridor.ai.value_function.Utils.distance;
import static quoridor.ai.value_function.Utils.minOtherPlayerDistance;

public final class TopOpponentDistanceComparison implements ValueFunction {


    private static final TopOpponentDistanceComparison INSTANCE =
            new TopOpponentDistanceComparison();

    private TopOpponentDistanceComparison() {
    }

    public static TopOpponentDistanceComparison getInstance() {
        return INSTANCE;
    }

    @Override
    public int apply(GameState gameState, int playerIx) {
        if (GameRules.isWon(gameState.getPlayerStates().get(playerIx))) {
            return max();
        } else if (GameRules.isFinal(gameState)) {
            return min();
        }
        return minOtherPlayerDistance(gameState, playerIx)
                - distance(gameState, playerIx);
    }

    @Override
    public int min() {
        return -DistanceCalculator.DISTANCE_UPPER_BOUND;
    }

    @Override
    public int max() {
        return DistanceCalculator.DISTANCE_UPPER_BOUND;
    }
}
