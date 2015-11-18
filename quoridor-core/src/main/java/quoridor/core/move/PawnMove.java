package quoridor.core.move;

import lombok.Value;

import quoridor.core.position.Position;
import quoridor.core.position.Positioned;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;

@Value(staticConstructor = "of")
public class PawnMove implements Move, Positioned {
    private final Position position;

    public static PawnMove of(int x, int y) {
        return of(Position.of(x, y));
    }

    @Override
    public Type getType() {
        return Type.PAWN;
    }

    @Override
    public GameState apply(GameState gameState) {
        GameState.Builder builder = gameState.buildNext();
        PlayerState playerState = gameState.getCurrentPlayersState()
                .movedTo(getX(), getY());
        builder.setPlayerState(gameState.currentPlayerIx(), playerState);
        return builder.build();
    }
}
