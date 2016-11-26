package quoridor.ai.bot.mcts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;

import quoridor.core.GameRules;
import quoridor.core.move.Move;
import quoridor.core.state.GameState;

public class Node {
    @Getter private final GameState gameState;
    @Getter private final Move lastMove;
    @Getter private final int[] winCount;

    @Getter private List<Node> children;
    @Getter private int childrenSimulationCount;
    @Getter private int simulationCount;

    Node(GameState gameState) {
        this(gameState, null);
    }

    Node(GameState gameState, Move lastMove) {
        this.gameState = gameState;
        this.winCount = new int[gameState.getPlayerStates().size()];
        this.lastMove = lastMove;
    }

    void expand() {
        children = new ArrayList<>();
        Iterator<Move> movesIterator = GameRules.getLegalMoves(gameState);
        Move move;
        while (movesIterator.hasNext()) {
            move = movesIterator.next();
            children.add(new Node(move.apply(gameState), move));
        }
    }

    void incrementCounters(int winner) {
        this.simulationCount += 1;
        this.winCount[winner] += 1;
        if (!isLeaf()) {
            this.childrenSimulationCount += 1;
        }
    }

    boolean isLeaf() {
        return children == null;
    }
}
