package quoridor.ai.value_function;

import quoridor.core.DistanceCalculator;
import quoridor.core.GameRules;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;

import static quoridor.ai.Utils.distance;
import static quoridor.ai.Utils.minOtherPlayerDistance;

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
        PlayerState playerState = gameState.getPlayerStates().get(playerIx);
        if (GameRules.isWon(playerState)) {
            return max();
        } else if (GameRules.isFinal(gameState)) {
            return min();
        }
        return (minOtherPlayerDistance(gameState, playerIx)
                - distance(gameState, playerIx)) * GameRules.WALL_COUNT
                + playerState.getWallsLeft();
    }

    @Override
    public int min() {
        return -DistanceCalculator.DISTANCE_UPPER_BOUND * GameRules.WALL_COUNT;
    }

    @Override
    public int max() {
        return DistanceCalculator.DISTANCE_UPPER_BOUND * GameRules.WALL_COUNT
                + GameRules.WALL_COUNT;
    }

    @Override
    public int maxTotal(int playersCount) {
        return GameRules.WALL_COUNT;
    }
}
