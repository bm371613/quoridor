package quoridor.core;

import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.WallsState;

import java.util.ArrayList;
import java.util.Collection;

public class GameRules {

    private GameRules() {
    }

    public static GameState makeInitialState(boolean topPlayersTurn) {
        int top = GameState.PLACES - 1;
        int bottom = 0;
        int middle = GameState.PLACES / 2;
        int initWalls = GameState.WALLS_NUMBER / 2;
        return GameState.builder()
                .setWallsState(WallsState.builder().build())
                .setTopPlayersState(new PlayerState(middle, top, initWalls))
                .setBottomPlayersState(new PlayerState(middle, bottom, initWalls))
                .setTopPlayersTurn(topPlayersTurn)
                .build();
    }

    public static Collection<Move> getLegalMoves(GameState gs) {
        Collection<Move> result = new ArrayList<>();
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
        int x = move.getX();
        int y = move.getY();
        if (move.isWallMove()) {
            return gs.getCurrentPlayersState().getWallsLeft() > 0
                    && !wallMoveCausesCollision(gs, move)
                    && !wallMoveCausesBlocking(gs, move);
        } else {
            PlayerState player = gs.getCurrentPlayersState();
            PlayerState opponent = gs.getCurrentPlayersOpponentsState();
            if (player.isBy(move)) {
                // regular pawn move (no jump)
                return !gs.isWallBetween(player, move) && !opponent.isAt(move);
            } else if (player.isBy(opponent) && opponent.isBy(move)
                    && !move.isAt(player)) {
                // jump
                return !(gs.isWallBetween(player, opponent)
                            || gs.isWallBetween(opponent, move))
                        && (player.getX() == move.getX() || player.getY() == y
                            || gs.isWallBehind(player, opponent));
            }
        }
        return false;
    }

    public static boolean isFinal(GameState gameState) {
        return isWonByTopPlayer(gameState) || isWonByBottomPlayer(gameState);
    }

    public static boolean isWonByTopPlayer(GameState gameState) {
        return gameState.getTopPlayersState().getY() == 0;
    }

    public static boolean isWonByBottomPlayer(GameState gameState) {
        return gameState.getBottomPlayersState().getY()
                == GameState.PLACES - 1;
    }

    private static boolean wallMoveCausesCollision(GameState gs, Move move) {
        int x = move.getX();
        int y = move.getY();
        if (gs.getWallsState().get(x, y) != null) {
            return true;
        }
        if (move.getWallOrientation() == WallOrientation.HORIZONTAL) {
            return gs.getWallsState().get(x - 1, y) == null
                    && gs.getWallsState().get(x + 1, y) == null;
        } else {
            return gs.getWallsState().get(x, y - 1) == null
                    && gs.getWallsState().get(x, y + 1) == null;
        }
    }

    private static boolean wallMoveCausesBlocking(GameState gs, Move move) {
        return false; // TODO
    }
}
