package quoridor.ai.bot.mcts;

public interface ChildSelector {

    double hopeFactor(double childrenSimulationCount);

    default Node selectChild(Node parent) {
        int playerIx = parent.getGameState().currentPlayerIx();
        double bestValue = Float.MIN_VALUE;
        double currentValue;
        Node bestChild = null;
        double hopeFactor = hopeFactor(parent.getChildrenSimulationCount());
        int childSimulationCount;

        for (Node child : parent.getChildren()) {
            childSimulationCount = child.getSimulationCount();
            if (childSimulationCount == 0) {
                return child;
            }
            currentValue = ((double) child.getWinCount()[playerIx])
                    / childSimulationCount
                    + Math.sqrt(hopeFactor / child.getSimulationCount());
            if (currentValue > bestValue) {
                bestChild = child;
                bestValue = currentValue;
            }
        }

        return bestChild;
    }

    ChildSelector WITH_LOG_HOPE = Math::log;
    ChildSelector WITH_SQRT_HOPE = Math::sqrt;
}
