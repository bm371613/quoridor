package quoridor.core.state;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;

import quoridor.core.Move;
import quoridor.core.position.Positioned;

public final class GameState {
    public static final int PLACES = 9; // number of places along each side
    public static final int WALL_PLACES = PLACES - 1;

    @Getter private final WallsState wallsState;
    @Getter private final ImmutableList<PlayerState> playerStates;
    private final int turn;

    public GameState(WallsState wallsState,
                     ImmutableList<PlayerState> playerStates, int turn) {
        this.wallsState = wallsState;
        this.playerStates = playerStates;
        this.turn = turn;
    }

    public int currentPlayerIx() {
        return turn % playerStates.size();
    }

    public GameState apply(Move move) {
        Builder builder = builder().copyFrom(this);

        if (move.isPawnMove()) {
            PlayerState playerState = getCurrentPlayersState()
                    .movedTo(move.getX(), move.getY());
            builder.setPlayerState(currentPlayerIx(), playerState);
        } else {
            WallsState newWallState = WallsState.builder()
                    .copyFrom(getWallsState())
                    .set(move.getX(), move.getY(), move.getWallOrientation())
                    .build();
            PlayerState newCurrentPlayersState = getCurrentPlayersState()
                    .withWallsLeft(getCurrentPlayersState().getWallsLeft() - 1);
            builder.setWallsState(newWallState)
                    .setPlayerState(currentPlayerIx(), newCurrentPlayersState);
        }

        builder.setTurn(turn + 1);
        return builder.build();
    }

    // helpers

    public PlayerState getCurrentPlayersState() {
        return playerStates.get(currentPlayerIx());
    }

    public boolean isOccupied(Positioned p) {
        return playerStates.stream().anyMatch((player) ->
                p.getX() == player.getX() && p.getY() == player.getY());
    }

    // builder

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private List<PlayerState> playerStates;
        private WallsState wallsState;
        private int turn;

        private Builder() {
        }

        public Builder copyFrom(GameState gs) {
            return this
                    .setWallsState(gs.wallsState)
                    .setPlayersStates(gs.playerStates)
                    .setTurn(gs.turn);
        }

        public Builder setWallsState(WallsState wallsState) {
            this.wallsState = wallsState;
            return this;
        }

        public Builder setPlayersStates(List<PlayerState> playersStates) {
            this.playerStates = Lists.newArrayList(playersStates);
            return this;
        }

        public Builder setPlayerState(int i, PlayerState playerState) {
            this.playerStates.set(i, playerState);
            return this;
        }

        public Builder setTurn(int turn) {
            this.turn = turn;
            return this;
        }

        public GameState build() {
            return new GameState(wallsState,
                    ImmutableList.copyOf(this.playerStates), turn);
        }

    }
}
