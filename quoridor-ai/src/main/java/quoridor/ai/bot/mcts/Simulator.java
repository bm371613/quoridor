package quoridor.ai.bot.mcts;

import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;

import static quoridor.ai.Utils.closestToGoal;

public interface Simulator {

    Move chooseMove(GameState gameState);

    default int simulate(Node node) {
        GameState gameState = node.getGameState();
        Move move;
        int wallsLeft = 0;
        for (PlayerState ps : gameState.getPlayerStates()) {
            wallsLeft += ps.getWallsLeft();
        }

        while (true) {
            if (GameRules.isFinal(gameState)) {
                return GameRules.getWinner(gameState);
            }
            if (wallsLeft < 1) {
                return closestToGoal(gameState);
            }
            move = chooseMove(gameState);
            if (move.getType() == Move.Type.WALL) {
                --wallsLeft;
            }
            gameState = move.apply(gameState);
        }
    }
}
