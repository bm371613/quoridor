package quoridor.core.move;

import quoridor.core.state.GameState;

public interface Move {

    enum Type {
        PAWN, WALL
    }

    Type getType();

    GameState apply(GameState gameState);
}
