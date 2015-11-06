package quoridor.core;

import com.google.common.collect.ImmutableList;
import quoridor.core.state.GameState;
import quoridor.core.state.Goal;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.WallsState;

import java.util.ArrayList;
import java.util.List;

public final class GameRules {

    private GameRules() {
    }

    public static GameState makeInitialStateForTwo() {
        int top = GameState.PLACES - 1;
        int bottom = 0;
        int middle = GameState.PLACES / 2;
        int initWalls = 10;
        List<PlayerState> playerStates = ImmutableList.of(
                    new PlayerState(Goal.TOP, middle, bottom, initWalls),
                    new PlayerState(Goal.BOTTOM, middle, top, initWalls));
        return GameState.builder()
                .setWallsState(WallsState.builder().build())
                .setPlayersStates(playerStates)
                .setTurn(0)
                .build();
    }

    public static GameState makeInitialStateForFour() {
        int top = GameState.PLACES - 1;
        int bottom = 0;
        int left = 0;
        int right = GameState.PLACES - 1;
        int middle = GameState.PLACES / 2;
        int initWalls = 5;
        List<PlayerState> playerStates = ImmutableList.of(
                new PlayerState(Goal.TOP, middle, bottom, initWalls),
                new PlayerState(Goal.RIGHT, left, middle, initWalls),
                new PlayerState(Goal.BOTTOM, middle, top, initWalls),
                new PlayerState(Goal.LEFT, right, middle, initWalls));
        return GameState.builder()
                .setWallsState(WallsState.builder().build())
                .setPlayersStates(playerStates)
                .setTurn(0)
                .build();
    }

    public static List<Move> getLegalMoves(GameState gs) {
        List<Move> result = new ArrayList<>();
        Move move;

        // pawn moves
        PlayerState player = gs.getCurrentPlayersState();
        int minX = Math.max(0, player.getX() - 2);
        int maxX = Math.max(GameState.PLACES - 1, player.getX() + 2);
        int minY = Math.max(0, player.getY() - 2);
        int maxY = Math.max(GameState.PLACES - 1, player.getY() + 2);
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                move = Move.makePawnMove(x, y);
                if (isLegalMove(gs, move)) {
                    result.add(move);
                }
            }
        }

        // wall moves
        for (int x = 0; x < GameState.WALL_PLACES; ++x) {
            for (int y = 0; y < GameState.WALL_PLACES; ++y) {
                move = Move.makeWallMove(x, y, WallOrientation.HORIZONTAL);
                if (isLegalMove(gs, move)) {
                    result.add(move);
                }
                move = Move.makeWallMove(x, y, WallOrientation.VERTICAL);
                if (isLegalMove(gs, move)) {
                    result.add(move);
                }
            }
        }

        return result;
    }

    public static boolean isLegalMove(GameState gs, Move move) {
        if (move.isWallMove()) {
            return 0 <= move.getX() && move.getX() < GameState.WALL_PLACES
                    && 0 <= move.getY() && move.getY() < GameState.WALL_PLACES
                    && gs.getCurrentPlayersState().getWallsLeft() > 0
                    && !wallMoveCausesCollision(gs, move)
                    && !wallMoveCausesBlocking(gs, move);
        } else {
            if (!(0 <= move.getX() && move.getX() < GameState.PLACES
                    && 0 <= move.getY() && move.getY() < GameState.PLACES)) {
                return false;
            }
            PlayerState player = gs.getCurrentPlayersState();
            if (player.isBy(move)) {
                return !gs.isWallBetween(player, move) && !gs.isOccupied(move);
            } else {
                return gs.getPlayerStates().stream().anyMatch((opponent) ->
                        opponent != player && isLegalJump(gs, move, opponent));
            }
        }
    }

    public static boolean isFinal(GameState gameState) {
        return gameState.getPlayerStates().stream().anyMatch(GameRules::isWon);
    }

    public static boolean isWon(PlayerState playerState) {
        switch (playerState.getGoal()) {
            case TOP:
                return playerState.getY() == GameState.PLACES - 1;
            case BOTTOM:
                return playerState.getY() == 0;
            case LEFT:
                return playerState.getX() == 0;
            case RIGHT:
                return playerState.getX() == GameState.PLACES - 1;
            default:
                throw new RuntimeException("Incorrect player state.");
        }
    }

    private static boolean wallMoveCausesCollision(GameState gs, Move move) {
        int x = move.getX();
        int y = move.getY();
        WallOrientation orientation = move.getWallOrientation();
        if (gs.getWallsState().get(x, y) != null) {
            return true;
        }
        if (orientation == WallOrientation.HORIZONTAL) {
            return gs.getWallsState().get(x - 1, y) == orientation
                    || gs.getWallsState().get(x + 1, y) == orientation;
        } else {
            return gs.getWallsState().get(x, y - 1) == orientation
                    || gs.getWallsState().get(x, y + 1) == orientation;
        }
    }

    private static boolean wallMoveCausesBlocking(GameState gs, Move move) {
        return false; // TODO
    }

    private static boolean isLegalJump(GameState gs, Move move,
        PlayerState opponent) {
        PlayerState player = gs.getCurrentPlayersState();
        boolean isJump = player.isBy(opponent) && opponent.isBy(move)
                && !move.isAt(player);
        if (!isJump) {
            return false;
        }
        if (gs.isWallBetween(player, opponent)
                || gs.isWallBetween(opponent, move)) {
            return false;
        }
        return player.getX() == move.getX() || player.getY() == move.getY()
                || gs.isWallBehind(player, opponent);
    }
}
