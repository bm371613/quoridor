package quoridor.core.state;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;

import quoridor.core.position.Positioned;

public final class GameState implements Serializable {
    public static final int PLACES = 9; // number of places along each side
    public static final int WALL_PLACES = PLACES - 1;
    public static final boolean ODD_PLACES = PLACES % 2 == 1;

    @Getter private final WallsState wallsState;
    @Getter private final ImmutableList<PlayerState> playerStates;
    @Getter private final int turn;

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

    public String toPrettyString() {
        int size = PLACES + WALL_PLACES;
        StringBuilder builder = new StringBuilder();
        builder.append("Turn: ").append(currentPlayerIx()).append("\n");
        char[][] board = new char[size][size];
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                board[y][x] = ' ';
            }
        }
        for (int i = 0; i < playerStates.size(); ++i) {
            PlayerState playerState = playerStates.get(i);
            builder.append("Player ").append(i).append(": ")
                    .append(playerState.getGoal()).append(" ")
                    .append(playerState.getWallsLeft()).append("\n");
            board[2 * playerState.getY()][2 * playerState.getX()]
                    = Character.forDigit(i, 10);
        }
        for (int wx = 0; wx < WALL_PLACES; ++wx) {
            for (int wy = 0; wy < WALL_PLACES; ++wy) {
                WallOrientation wallOrientation = wallsState.get(wx, wy);
                int x = 2 * wx + 1;
                int y = 2 * wy + 1;
                if (wallOrientation == WallOrientation.HORIZONTAL) {
                    board[y][x - 1] = '-';
                    board[y][x] = '-';
                    board[y][x + 1] = '-';
                } else if (wallOrientation == WallOrientation.VERTICAL) {
                    board[y - 1][x] = '|';
                    board[y][x] = '|';
                    board[y + 1][x] = '|';
                } else {
                    board[y][x] = '+';
                }
            }
        }
        builder.append('+');
        for (int x = 0; x < size; ++x) {
            builder.append('-');
        }
        builder.append("+\n");
        for (int y = size - 1; y >= 0; --y) {
            builder.append('|');
            for (int x = 0; x < size; ++x) {
                builder.append(board[y][x]);
            }
            builder.append("|\n");
        }
        builder.append('+');
        for (int x = 0; x < size; ++x) {
            builder.append('-');
        }
        builder.append("+\n");
        return builder.toString();
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
