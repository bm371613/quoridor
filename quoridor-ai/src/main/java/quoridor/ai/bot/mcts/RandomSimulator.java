package quoridor.ai.bot.mcts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

import static quoridor.ai.Utils.distance;

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
        for (int depth = 0; depth < maxDepth; ++depth) {
            if (GameRules.isFinal(gameState)) {
                return GameRules.getWinner(gameState);
            }
            moveIterator = GameRules.getLegalMoves(gameState);
            moves.clear();
            while (moveIterator.hasNext()) {
                moves.add(moveIterator.next());
            }
            gameState = moves.get(random.nextInt(moves.size()))
                    .apply(gameState);
        }

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
