package quoridor.ai.bot.mcts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import quoridor.ai.value_function.ValueFunction;
import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public final class GreedySimulator implements Simulator {

    private final ValueFunction valueFunction;
    private final double greediness;
    private final Random random;
    private final List<Move> moves = new ArrayList<>();

    public GreedySimulator(ValueFunction valueFunction, double greediness) {
        this.valueFunction = valueFunction;
        this.greediness = greediness;
        random = new Random();
    }

    @Override
    public Move chooseMove(GameState gameState) {
        return random.nextDouble() < greediness
                ? chooseGreedy(gameState)
                : chooseRandom(gameState);
    }

    private Move chooseRandom(GameState gameState) {
        Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
        moves.clear();
        while (moveIterator.hasNext()) {
            moves.add(moveIterator.next());
        }
        return moves.get(random.nextInt(moves.size()));
    }

    private Move chooseGreedy(GameState gameState) {
        Move best = null;
        Move current;
        int bestValue = Integer.MIN_VALUE;
        int currentValue;

        Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
        while (moveIterator.hasNext()) {
            current = moveIterator.next();
            currentValue = valueFunction.apply(current.apply(gameState),
                    gameState.currentPlayerIx());
            if (currentValue > bestValue) {
                best = current;
                bestValue = currentValue;
            }
        }
        return best;
    }
}
