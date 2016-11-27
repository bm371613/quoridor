package quoridor.ai.bot.mcts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;

import static quoridor.ai.Utils.closestToGoal;

public class RandomSimulator implements Simulator {

    private final Random random;
    private final List<Move> moves = new ArrayList<>();
    private final int maxDepth;

    public RandomSimulator(int maxDepth) {
        random = new Random();
        this.maxDepth = maxDepth;
    }

    @Override
    public int simulate(Node node) {
        GameState gameState = node.getGameState();
        Iterator<Move> moveIterator;
        Move move;
        int wallsLeft = 0;
        for (PlayerState ps : gameState.getPlayerStates()) {
            wallsLeft += ps.getWallsLeft();
        }

        for (int depth = 0; depth < maxDepth && wallsLeft > 0; ++depth) {
            if (GameRules.isFinal(gameState)) {
                return GameRules.getWinner(gameState);
            }
            moveIterator = GameRules.getLegalMoves(gameState);
            moves.clear();
            while (moveIterator.hasNext()) {
                moves.add(moveIterator.next());
            }
            move = moves.get(random.nextInt(moves.size()));
            if (move.getType() == Move.Type.WALL) {
                --wallsLeft;
            }
            gameState = move.apply(gameState);
        }

        return closestToGoal(gameState);
    }
}
