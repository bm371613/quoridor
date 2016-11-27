package quoridor.ai.bot.mcts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public final class RandomSimulator implements Simulator {

    private final Random random;
    private final List<Move> moves = new ArrayList<>();

    public RandomSimulator() {
        random = new Random();
    }

    @Override
    public Move chooseMove(GameState gameState) {
        Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
        moves.clear();
        while (moveIterator.hasNext()) {
            moves.add(moveIterator.next());
        }
        return moves.get(random.nextInt(moves.size()));
    }
}
