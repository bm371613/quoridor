package quoridor.ai;

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

    public static int closestToGoal(GameState gameState) {
        int playersCount = gameState.getPlayerStates().size();
        int currentPlayerIx = gameState.currentPlayerIx();

        int best = -1;
        int bestDistance = Integer.MAX_VALUE;

        int current;
        int distance;

        for (int i = currentPlayerIx; i < currentPlayerIx + playersCount; ++i) {
            current = i % playersCount;
            distance = distance(gameState, current);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = current;
            }
        }
        return best;
    }
}
