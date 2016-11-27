package quoridor.ai.bot.mcts;

import java.util.List;

import quoridor.ai.bot.Bot;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.core.GameRules;
import quoridor.core.state.GameState;

public final class MCTSBot implements Bot {

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


final class MCTSThinkingProcess extends ThinkingProcess {

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

    private int searchChild(Node node) {
        GameState gameState = node.getGameState();
        return GameRules.isFinal(gameState)
                ? GameRules.getWinner(gameState)
                : search(childSelector.selectChild(node));
    }

    private int search(Node node) {
        int winner;
        if (node.isLeaf()) {
            if (node.getSimulationCount() >= expansionThreshold) {
                node.expand();
                winner = searchChild(node);
            } else {
                winner = simulator.simulate(node);
            }
        } else {
            winner = searchChild(node);
        }
        node.incrementCounters(winner);
        return winner;
    }

    private void prepareRoot() {
        int winner;

        root.expand();
        for (Node child : root.getChildren()) {
            winner = simulator.simulate(child);
            child.incrementCounters(winner);
            root.incrementCounters(winner);
        }
    }

    @Override
    public void run() {
        prepareRoot();

        int currentPlayerIx = root.getGameState().currentPlayerIx();
        int bestSimulationCount;
        int bestWinCount;
        int simulationCount;
        int winCount;
        List<Node> children = root.getChildren();

        while (true) {
            search(root);
            bestSimulationCount = 0;
            bestWinCount = 0;
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

