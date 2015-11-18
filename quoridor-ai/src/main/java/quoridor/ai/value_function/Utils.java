package quoridor.ai.value_function;

import com.google.common.collect.ImmutableList;

import quoridor.core.DistanceCalculator;
import quoridor.core.GameRules;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;

public final class Utils {

    private static DistanceCalculator calc = DistanceCalculator.getInstance();

    private Utils() {
    }

    public static int distance(GameState gameState, int playerIx) {
        PlayerState playerState = gameState.getPlayerStates().get(playerIx);
        return calc.calculateDistance(gameState.getWallsState(),
                playerState,
                GameRules.getGoalPredicate(playerState));
    }

    public static int minOtherPlayerDistance(GameState gameState,
                int playerIx) {
        ImmutableList<PlayerState> playerStates = gameState.getPlayerStates();
        int result = Integer.MAX_VALUE;
        for (int ix = 0; ix < playerStates.size(); ++ix) {
            if (ix != playerIx) {
                result = Math.min(result, distance(gameState, ix));
            }
        }
        return result;
    }
}
