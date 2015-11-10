package quoridor.ai.simple;

import com.google.common.collect.ImmutableList;

import quoridor.ai.Bot;
import quoridor.ai.ThinkingProcess;
import quoridor.ai.ValueFunction;
import quoridor.core.DistanceCalculator;
import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallsState;

public class GreedyBot implements Bot {

    private ValueFunction valueFunction = (gameState, playerIx) -> {
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

    @Override
    public final ThinkingProcess thinkAbout(GameState gameState) {
        return new ThinkingProcess() {
            @Override
            public void run() {
                int bestValue = Integer.MIN_VALUE;
                int currentValue;
                int playerIx = gameState.currentPlayerIx();
                for (Move move : GameRules.getLegalMoves(gameState)) {
                    currentValue = valueFunction.apply(gameState.apply(move),
                            playerIx);
                    if (getResult() == null || bestValue < currentValue) {
                        setResult(move);
                        bestValue = currentValue;
                    }
                }
            }
        };
    }
}
