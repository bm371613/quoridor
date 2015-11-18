package quoridor.core.move;

import quoridor.core.state.GameState;

public interface Move {

    public enum Type {
        PAWN, WALL
    }

    Type getType();

    GameState apply(GameState gameState);
}
