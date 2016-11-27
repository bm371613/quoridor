package quoridor.ai.bot.mcts;


import quoridor.core.state.GameState;

public final class ReducedWallsSimulator extends Simulator {

    private final int wallLimit;

    public ReducedWallsSimulator(int wallLimit) {
        super();
        this.wallLimit = wallLimit;
    }

    @Override
    protected int maxWallMoves(GameState gameState) {
        return Math.min(wallLimit, super.maxWallMoves(gameState));
    }
}
