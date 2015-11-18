package quoridor.core.state;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;

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

    // helpers

    public PlayerState getCurrentPlayersState() {
        return playerStates.get(currentPlayerIx());
    }

    public boolean isOccupied(Positioned p) {
        return playerStates.stream().anyMatch((player) ->
                p.getX() == player.getX() && p.getY() == player.getY());
    }

    public static boolean placeInBoardBounds(Positioned p) {
        return 0 <= p.getX() && p.getX() < PLACES
                && 0 <= p.getY() && p.getY() < PLACES;
    }

    // builder

    public static Builder builder() {
        return new Builder();
    }

    public Builder buildNext() {
        Builder result = new Builder();
        result.copyFrom(this);
        result.setTurn(turn + 1);
        return result;
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
