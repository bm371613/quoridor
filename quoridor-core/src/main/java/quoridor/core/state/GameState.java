package quoridor.core.state;

import quoridor.core.Move;
import quoridor.core.util.Positioned;

public class GameState {
    public static int PLACES = 9; // number of places along each side
    public static int WALL_PLACES = PLACES - 1 ;
    public static int WALLS_NUMBER = 20;

    private WallsState wallsState;
    private PlayerState topPlayersState; // player starting at y = PLACES - 1
    private PlayerState bottomPlayersState; // player starting at y = 0
    private boolean topPlayersTurn;

    private GameState() {
    }

    public WallsState getWallsState() {
        return wallsState;
    }

    public PlayerState getTopPlayersState() {
        return topPlayersState;
    }

    public PlayerState getBottomPlayersState() {
        return bottomPlayersState;
    }

    public boolean isTopPlayersTurn() {
        return topPlayersTurn;
    }

    public GameState apply(Move move) {
        Builder builder = builder().copyFrom(this);
        if (move.isPawnMove()) {
            if (isTopPlayersTurn()) {
                builder.setTopPlayersState(new PlayerState(move.getX(),
                        move.getY(), getTopPlayersState().getWallsLeft()));
                builder.setTopPlayersTurn(false);
            } else {
                builder.setBottomPlayersState(new PlayerState(move.getX(),
                        move.getY(), getBottomPlayersState().getWallsLeft()));
                builder.setTopPlayersTurn(true);
            }
        } else {
            builder.setWallsState(WallsState.builder()
                    .copyFrom(getWallsState())
                    .set(move.getX(), move.getY(), move.getWallOrientation())
                    .build());
            PlayerState playerState = getCurrentPlayersState();
            PlayerState newPlayerState = new PlayerState(playerState.getX(),
                    playerState.getY(), playerState.getWallsLeft() - 1);
            if (isTopPlayersTurn()) {
                builder.setTopPlayersState(newPlayerState);
                builder.setTopPlayersTurn(false);
            } else {
                builder.setBottomPlayersState(newPlayerState);
                builder.setTopPlayersTurn(true);
            }
        }
        return builder.build();
    }

    // helpers

    public PlayerState getCurrentPlayersState() {
        return topPlayersTurn ? topPlayersState : bottomPlayersState;
    }

    public PlayerState getCurrentPlayersOpponentsState() {
        return topPlayersTurn ? bottomPlayersState : topPlayersState;
    }

    private boolean isWallAbove(int x, int y) {
        return (wallsState.get(x, y) == WallOrientation.HORIZONTAL
            || wallsState.get(x - 1, y) == WallOrientation.HORIZONTAL);
    }

    private boolean isWallBelow(int x, int y) {
        return isWallAbove(x, y - 1);
    }

    private boolean isWallAtRight(int x, int y) {
        return (wallsState.get(x, y) == WallOrientation.VERTICAL
                || wallsState.get(x, y - 1) == WallOrientation.VERTICAL);
    }

    private boolean isWallAtLeft(int x, int y) {
        return isWallAtRight(x - 1, y);
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private GameState result;

        private Builder () {
            result = new GameState();
        }

        public Builder copyFrom(GameState gs) {
            return this
                    .setWallsState(gs.getWallsState())
                    .setTopPlayersState(gs.getTopPlayersState())
                    .setBottomPlayersState(gs.getBottomPlayersState())
                    .setTopPlayersTurn(gs.isTopPlayersTurn());
        }

        public Builder setWallsState(WallsState wallsState) {
            result.wallsState = wallsState;
            return this;
        }

        public Builder setTopPlayersState(PlayerState topPlayersState) {
            result.topPlayersState = topPlayersState;
            return this;
        }

        public Builder setBottomPlayersState(PlayerState bottomPlayersState) {
            result.bottomPlayersState = bottomPlayersState;
            return this;
        }

        public Builder setTopPlayersTurn(boolean topPlayersTurn) {
            result.topPlayersTurn = topPlayersTurn;
            return this;
        }

        public GameState build() {
            return result;
        }

    }
}
