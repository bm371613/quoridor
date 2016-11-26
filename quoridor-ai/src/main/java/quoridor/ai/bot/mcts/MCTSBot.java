package quoridor.ai.bot.mcts;

import java.util.List;

import quoridor.ai.bot.Bot;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.core.state.GameState;

public class MCTSBot implements Bot {

    private final int expansionThreshold;
    private final ChildSelector childSelector;
    private final Simulator simulator;

    public MCTSBot(int expansionThreshold, ChildSelector childSelector,
                   Simulator simulator) {
        this.expansionThreshold = expansionThreshold;
        this.childSelector = childSelector;
        this.simulator = simulator;
    }

    @Override
    public ThinkingProcess thinkAbout(GameState gameState) {
        return new MCTSThinkingProcess(gameState, expansionThreshold,
                childSelector, simulator);
    }
}


class MCTSThinkingProcess extends ThinkingProcess {

    private final Node root;
    private final int expansionThreshold;
    private final ChildSelector childSelector;
    private final Simulator simulator;

    MCTSThinkingProcess(GameState gameState, int expansionThreshold,
                        ChildSelector childSelector, Simulator simulator) {
        super();
        this.root = new Node(gameState);
        this.expansionThreshold = expansionThreshold;
        this.childSelector = childSelector;
        this.simulator = simulator;
    }

    private int search(Node node) {
        int winner;
        if (node.isLeaf()) {
            if (node.getSimulationCount() >= expansionThreshold) {
                node.expand();
                winner = search(childSelector.selectChild(node));
            } else {
                winner = simulator.simulate(node);
            }
        } else {
            winner = search(childSelector.selectChild(node));
        }
        node.incrementCounters(winner);
        return winner;
    }

    @Override
    public void run() {
        root.expand();

        int currentPlayerIx = root.getGameState().currentPlayerIx();
        int bestSimulationCount = 0;
        int bestWinCount = 0;
        int simulationCount;
        int winCount;
        List<Node> children = root.getChildren();

        while (true) {
            search(root);
            for (Node child : children) {
                simulationCount = child.getSimulationCount();
                winCount = child.getWinCount()[currentPlayerIx];
                if (bestWinCount * simulationCount
                        < winCount * bestSimulationCount
                        || bestWinCount == 0 && winCount > 0) {
                    setResult(child.getLastMove());
                    bestSimulationCount = simulationCount;
                    bestWinCount = winCount;
                }
            }
        }
    }
}

