package quoridor.ai.bot.mcts;

public interface ChildSelector {
    float evaluateChild(Node child, int playerIx);

    default Node selectChild(Node node) {
        int playerIx = node.getGameState().currentPlayerIx();
        float bestValue = Float.MIN_VALUE;
        float currentValue;
        Node bestChild = null;

        for (Node child : node.getChildren()) {
            currentValue = evaluateChild(child, playerIx);
            if (currentValue > bestValue) {
                bestChild = child;
                bestValue = currentValue;
            }
        }

        return bestChild;
    }
}
