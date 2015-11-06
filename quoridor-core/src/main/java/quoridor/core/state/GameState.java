package quoridor.core.state;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import quoridor.core.Move;
import quoridor.core.util.Positioned;

public final class GameState {
    public static final int PLACES = 9; // number of places along each side
    public static final int WALL_PLACES = PLACES - 1;

    private WallsState wallsState;
    private ImmutableList<PlayerState> playerStates;
    private int turn;

    private GameState() {
    }

    public WallsState getWallsState() {
        return wallsState;
    }

    public ImmutableList<PlayerState> getPlayerStates() {
        return playerStates;
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

    private boolean isWallAbove(int x, int y) {
        return y == PLACES - 1
                || wallsState.get(x, y) == WallOrientation.HORIZONTAL
                || wallsState.get(x - 1, y) == WallOrientation.HORIZONTAL;
    }

    private boolean isWallBelow(int x, int y) {
        return y == 0 || isWallAbove(x, y - 1);
    }

    private boolean isWallAtRight(int x, int y) {
        return x == PLACES - 1
                || wallsState.get(x, y) == WallOrientation.VERTICAL
                || wallsState.get(x, y - 1) == WallOrientation.VERTICAL;
    }

    private boolean isWallAtLeft(int x, int y) {
        return x == 0 || isWallAtRight(x - 1, y);
    }

    public boolean isWallBetween(Positioned p1, Positioned p2) {
        assert p1.isBy(p2);
        if (p1.getX() == p2.getX()) {
            return isWallAbove(p1.getX(), Math.min(p1.getY(), p2.getY()));
        } else {
            return isWallAtRight(Math.min(p1.getX(), p2.getX()), p1.getY());
        }
    }

    public boolean isWallBehind(Positioned p1, Positioned p2) {
        assert p1.isBy(p2);
        if (p1.getX() == p2.getX()) {
            if (p1.getY() < p2.getY()) {
                return isWallAbove(p2.getX(), p2.getY());
            } else {
                return isWallBelow(p2.getX(), p2.getY());
            }
        } else {
            if (p1.getX() < p2.getX()) {
                return isWallAtRight(p2.getX(), p2.getY());
            } else {
                return isWallAtLeft(p2.getX(), p2.getY());
            }
        }
    }

    // builder

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private List<PlayerState> playerStates;
        private GameState result;

        private Builder() {
            result = new GameState();
        }

        public Builder copyFrom(GameState gs) {
            return this
                    .setWallsState(gs.wallsState)
                    .setPlayersStates(gs.playerStates)
                    .setTurn(gs.turn);
        }

        public Builder setWallsState(WallsState wallsState) {
            result.wallsState = wallsState;
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
            result.turn = turn;
            return this;
        }

        public GameState build() {
            result.playerStates = ImmutableList.copyOf(this.playerStates);
            return result;
        }

    }
}
