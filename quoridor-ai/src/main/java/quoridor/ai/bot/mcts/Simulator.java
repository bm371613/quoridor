package quoridor.ai.bot.mcts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

import static quoridor.ai.Utils.closestToGoal;

public class Simulator {

    private final Random random = new Random(System.currentTimeMillis());
    private final List<Move> moves = new ArrayList<>(132);
    private final int maxMoves;

    public Simulator(int maxMoves) {
        this.maxMoves = maxMoves;
    }

    protected Move chooseMove(GameState gameState) {
        Move move;
        Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
        moves.clear();
        while (moveIterator.hasNext()) {
            move = moveIterator.next();
            if (GameRules.isFinal(move.apply(gameState))) {
                return move;
            }
            moves.add(move);
        }
        return moves.get(random.nextInt(moves.size()));
    }

    public int simulate(Node node) {
        GameState gameState = node.getGameState();
        Move move;

        for (int i = 0; true; ++i) {
            if (GameRules.isFinal(gameState)) {
                return GameRules.getWinner(gameState);
            }
            if (this.maxMoves == i) {
                return closestToGoal(gameState);
            }
            move = chooseMove(gameState);
            gameState = move.apply(gameState);
        }
    }
}
