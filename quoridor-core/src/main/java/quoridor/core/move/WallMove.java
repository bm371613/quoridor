package quoridor.core.move;

import lombok.Value;

import quoridor.core.position.Position;
import quoridor.core.position.Positioned;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.WallsState;

@Value(staticConstructor = "of")
public class WallMove implements Move, Positioned {
    private final Position position;
    private final WallOrientation wallOrientation;

    public static WallMove of(int x, int y, WallOrientation wallOrientation) {
        return of(Position.of(x, y), wallOrientation);
    }

    @Override
    public Type getType() {
        return Type.WALL;
    }

    @Override
    public GameState apply(GameState gameState) {
        GameState.Builder builder = gameState.buildNext();
        WallsState newWallState = WallsState.builder()
                .copyFrom(gameState.getWallsState())
                .set(getX(), getY(), wallOrientation)
                .build();
        PlayerState currentPlayersState = gameState.getCurrentPlayersState();
        PlayerState newCurrentPlayersState = currentPlayersState
                .withWallsLeft(currentPlayersState.getWallsLeft() - 1);
        builder.setWallsState(newWallState).setPlayerState(
                gameState.currentPlayerIx(), newCurrentPlayersState);
        return builder.build();
    }
}
