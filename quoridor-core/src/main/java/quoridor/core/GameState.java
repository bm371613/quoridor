package quoridor.core;

public class GameState {
    public static int BOARD_SIZE = 9; // number of places along each side
    public static int WALLS_NUMBER = 20;

    private GrooveCrossState[][] grooveCrossStates;
    private PlayerState bottomPlayerState; // player starting at y = 0
    private PlayerState topPlayerState; // player starting at y = BOARD_SIZE - 1

    public GameState() {
        grooveCrossStates =
                new GrooveCrossState[BOARD_SIZE - 1][BOARD_SIZE - 1];
        int midX = BOARD_SIZE / 2;
        int playersWalls = WALLS_NUMBER / 2;
        bottomPlayerState = new PlayerState(midX, 0, playersWalls);
        topPlayerState = new PlayerState(midX, BOARD_SIZE - 1, playersWalls);
    }

    public GrooveCrossState[][] getGrooveCrossStates() {
        return grooveCrossStates;
    }

    public PlayerState getBottomPlayerState() {
        return bottomPlayerState;
    }

    public PlayerState getTopPlayerState() {
        return topPlayerState;
    }
}
