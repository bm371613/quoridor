package quoridor.ai.value_function;

import com.google.common.collect.ImmutableList;

import quoridor.core.DistanceCalculator;
import quoridor.core.GameRules;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallsState;

public class DistanceBehindBestOpponent implements ValueFunction {

    @Override
    public int apply(GameState gameState, int playerIx) {
        WallsState walls = gameState.getWallsState();
        ImmutableList<PlayerState> playerStates = gameState.getPlayerStates();
        DistanceCalculator calc = DistanceCalculator.getInstance();
        PlayerState playerState = playerStates.get(playerIx);
        int myDistance = calc.calculateDistance(walls, playerState,
                GameRules.getGoalPredicate(playerState));
        int bestOpponentDistance = Integer.MAX_VALUE;
        for (int ix = 0; ix < playerStates.size(); ++ix) {
            if (ix != playerIx) {
                PlayerState opponentState = playerStates.get(ix);
                int opponentDistance = calc.calculateDistance(walls,
                        opponentState,
                        GameRules.getGoalPredicate(opponentState));
                bestOpponentDistance = Math.min(bestOpponentDistance,
                        opponentDistance);
            }
        }
        return bestOpponentDistance - myDistance;
    };
}
