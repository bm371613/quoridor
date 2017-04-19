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

public class Simulator {

    private final Random random = new Random();
    private final List<Move> moves = new ArrayList<>(132);
    private final Integer maxMoves;

    public Simulator() {
        this.maxMoves = null;
    }

    public Simulator(int maxMoves) {
        this.maxMoves = maxMoves;
    }

    protected int maxWallMoves(GameState gameState) {
        int wallsLeft = 0;
        for (PlayerState ps : gameState.getPlayerStates()) {
            wallsLeft += ps.getWallsLeft();
        }
        return wallsLeft;
    }

    protected Move chooseMove(GameState gameState) {
        Iterator<Move> moveIterator = GameRules.getLegalMoves(gameState);
        moves.clear();
        while (moveIterator.hasNext()) {
            moves.add(moveIterator.next());
        }
        return moves.get(random.nextInt(moves.size()));
    }

    public int simulate(Node node) {
        GameState gameState = node.getGameState();
        Move move;
        int wallsLeft = maxWallMoves(gameState);

        for (int i = 0; true; ++i) {
            if (GameRules.isFinal(gameState)) {
                return GameRules.getWinner(gameState);
            }
            if (wallsLeft < 1 || this.maxMoves != null && this.maxMoves == i) {
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
