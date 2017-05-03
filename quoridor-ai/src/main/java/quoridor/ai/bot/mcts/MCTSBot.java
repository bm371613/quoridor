package quoridor.ai.bot.mcts;

import java.util.List;

import quoridor.ai.Utils;
import quoridor.ai.bot.Bot;
import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.core.GameRules;
import quoridor.core.state.GameState;

public final class MCTSBot implements Bot {

    private final int twoPLayerExpansionThreshold;
    private final ChildSelector twoPlayerChildSelector;
    private final Simulator twoPlayerSimulator;

    private final int fourPLayerExpansionThreshold;
    private final ChildSelector fourPlayerChildSelector;
    private final Simulator fourPlayerSimulator;

    public MCTSBot(int expansionThreshold, ChildSelector childSelector,
                   Simulator simulator) {
        this(expansionThreshold, childSelector, simulator,
                expansionThreshold, childSelector, simulator);
    }

    public MCTSBot(int twoPLayerExpansionThreshold,
                   ChildSelector twoPlayerChildSelector,
                   Simulator twoPlayerSimulator,
                   int fourPLayerExpansionThreshold,
                   ChildSelector fourPlayerChildSelector,
                   Simulator fourPlayerSimulator) {
        this.twoPLayerExpansionThreshold = twoPLayerExpansionThreshold;
        this.twoPlayerChildSelector = twoPlayerChildSelector;
        this.twoPlayerSimulator = twoPlayerSimulator;

        this.fourPLayerExpansionThreshold = fourPLayerExpansionThreshold;
        this.fourPlayerChildSelector = fourPlayerChildSelector;
        this.fourPlayerSimulator = fourPlayerSimulator;
    }

    @Override
    public ThinkingProcess thinkAbout(GameState gameState) {
        if (gameState.getPlayerStates().size() == 2) {
            return new MCTSThinkingProcess(gameState,
                    twoPLayerExpansionThreshold, twoPlayerChildSelector,
                    twoPlayerSimulator);
        } else {
            return new MCTSThinkingProcess(gameState,
                    fourPLayerExpansionThreshold, fourPlayerChildSelector,
                    fourPlayerSimulator);
        }
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

    private Node straightToGoal(Node source) {
        int distance;
        int minDistance = Integer.MAX_VALUE;
        int playerIx = source.getGameState().currentPlayerIx();
        Node result = null;
        for (Node child : source.getChildren()) {
            distance = Utils.distance(child.getGameState(), playerIx);
            if (distance < minDistance) {
                minDistance = distance;
                result = child;
            }
        }
        return result;
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
        Node straightToGoal = straightToGoal(root);
        setResult(straightToGoal.getLastMove());

        while (true) {
            search(root);
            if (straightToGoal.getWinCount()[currentPlayerIx]
                    == straightToGoal.getSimulationCount()) {
                continue;
            }
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

