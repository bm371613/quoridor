package quoridor.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

import quoridor.core.direction.Directed;
import quoridor.core.direction.Direction;
import quoridor.core.direction.PerDirection;
import quoridor.core.move.Move;
import quoridor.core.move.PawnMove;
import quoridor.core.move.WallMove;
import quoridor.core.position.Position;
import quoridor.core.position.Positioned;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.WallsState;

public final class GameRules {

    public static final int WALL_COUNT = 20;

    private GameRules() {
    }

    private static PerDirection<Predicate<Positioned>> goalPredicates =
            (new PerDirection<Predicate<Positioned>>())
                    .set(Direction.DOWN, (p) -> p.getY() == 0)
                    .set(Direction.LEFT, (p) -> p.getX() == 0)
                    .set(Direction.UP, (p) -> p.getY() == GameState.PLACES - 1)
                    .set(Direction.RIGHT,
                            (p) -> p.getX() == GameState.PLACES - 1);

    public static GameState makeInitialStateForTwo() {
        int top = GameState.PLACES - 1;
        int bottom = 0;
        int middle = GameState.PLACES / 2;
        int initWalls = WALL_COUNT / 2;
        List<PlayerState> playerStates = ImmutableList.of(
                    PlayerState.of(Direction.UP, middle, bottom, initWalls),
                    PlayerState.of(Direction.DOWN, middle, top, initWalls));
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
        int initWalls = WALL_COUNT / 4;
        List<PlayerState> playerStates = ImmutableList.of(
                PlayerState.of(Direction.UP, middle, bottom, initWalls),
                PlayerState.of(Direction.RIGHT, left, middle, initWalls),
                PlayerState.of(Direction.DOWN, middle, top, initWalls),
                PlayerState.of(Direction.LEFT, right, middle, initWalls));
        return GameState.builder()
                .setWallsState(WallsState.builder().build())
                .setPlayersStates(playerStates)
                .setTurn(0)
                .build();
    }

    public static Iterator<Move> getLegalMoves(GameState gs) {
        if (isFinal(gs)) {
            return Collections.emptyIterator();
        }

        if (gs.getCurrentPlayersState().getWallsLeft() < 1) {
            return new LegalPawnMovesIterator(gs);
        } else {
            return Iterators.concat(
                    new LegalPawnMovesIterator(gs),
                    new LegalWallMovesIterator(gs)
            );
        }
    }

    private static boolean isLegalMove(GameState gs, WallMove move) {
        return WallsState.inBounds(move)
                && gs.getCurrentPlayersState().getWallsLeft() > 0
                && !wallMoveCausesCollision(gs, move)
                && !wallMoveCausesBlocking(gs, move);
    }

    private static boolean isLegalMove(GameState gs, PawnMove move) {
        return Iterators.any(new LegalPawnMovesIterator(gs), move::equals);
    }

    public static boolean isLegalMove(GameState gs, Move move) {
        if (isFinal(gs)) {
            return false;
        }
        switch (move.getType()) {
            case WALL:
                return isLegalMove(gs, (WallMove) move);
            case PAWN:
                return isLegalMove(gs, (PawnMove) move);
            default:
                return false;
        }
    }

    public static Predicate<Positioned> getGoalPredicate(Directed directed) {
        return goalPredicates.get(directed);
    }

    public static boolean isFinal(GameState gameState) {
        for (PlayerState ps : gameState.getPlayerStates()) {
            if (GameRules.isWon(ps)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWon(PlayerState ps) {
        return getGoalPredicate(ps).test(ps);
    }

    public static int getWinner(GameState gs) {
        List<PlayerState> playerStates = gs.getPlayerStates();
        for (int playerIx = 0; playerIx < playerStates.size(); ++playerIx) {
            if (isWon(playerStates.get(playerIx))) {
                return playerIx;
            }
        }
        throw new RuntimeException(
                "Cannot determine winner for a non-final game state");
    }

    static boolean wallMoveCausesCollision(GameState gs,
                WallMove move) {
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

    static boolean wallMoveCausesBlocking(GameState gs,
                WallMove move) {
        WallsState walls = move.apply(gs).getWallsState();
        if ((walls.getHorizontalCount() == 0 || walls.getVerticalCount() == 0)
                && GameState.ODD_PLACES) {
            return false;
        }
        DistanceCalculator dc = DistanceCalculator.getInstance();
        for (PlayerState ps : gs.getPlayerStates()) {
            if (dc.calculateDistance(walls, ps, getGoalPredicate(ps))
                    >= DistanceCalculator.INFINITY) {
                return true;
            }
        }
        return false;
    }
}

final class LegalPawnMovesIterator implements Iterator<Move> {

    private GameState gameState;
    private boolean advanced;
    private PawnMove next;

    private List<Positioned> sources = new ArrayList<>(4);
    private List<Positioned> targets = new ArrayList<>(10);
    private HashSet<Integer> considered = new HashSet<>();

    LegalPawnMovesIterator(GameState gameState) {
        this.gameState = gameState;
        sources.add(gameState.getCurrentPlayersState());
    }

    @Override
    public boolean hasNext() {
        if (!advanced) {
            advance();
        }
        return next != null;
    }

    @Override
    public PawnMove next() {
        if (hasNext()) {
            advanced = false;
            return next;
        } else {
            throw new NoSuchElementException();
        }
    }

    private void advance() {
        advanced = true;
        next = null;

        while (targets.isEmpty() && !sources.isEmpty()) {
            Positioned source = sources.remove(sources.size() - 1);
            for (Direction direction : Direction.values()) {
                Positioned p = Position.next(source, direction);

                // skip already considered
                if (considered.contains(positionedHash(p))) {
                    continue;
                }
                considered.add(positionedHash(p));

                // skip out of bounds
                if (!GameState.placeInBoardBounds(p)) {
                    continue;
                }

                // skip unreachable
                if (gameState.getWallsState().isWallBetween(source, p)) {
                    continue;
                }

                if (gameState.isOccupied(p)) {
                    sources.add(p);
                } else {
                    targets.add(p);
                }
            }
        }

        if (!targets.isEmpty()) {
            next = PawnMove.of(
                    targets.remove(targets.size() - 1).getPosition());
        }
    }

    private int positionedHash(Positioned p) {
        return p.getX() * GameState.PLACES + p.getY();
    }
}

final class LegalWallMovesIterator implements Iterator<Move> {

    private static final int IX_LIMIT =
            2 * GameState.WALL_PLACES * GameState.WALL_PLACES;

    private GameState gameState;
    private boolean advanced;
    private WallMove next;
    private int ix;

    LegalWallMovesIterator(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean hasNext() {
        if (!advanced) {
            advance();
        }
        return next != null;
    }

    @Override
    public WallMove next() {
        if (hasNext()) {
            advanced = false;
            return next;
        } else {
            throw new NoSuchElementException();
        }
    }

    private void advance() {
        advanced = true;
        next = null;

        int tmp;
        WallMove move;
        while (next == null && ix < IX_LIMIT) {
            tmp = ix / 2;
            move = WallMove.of(
                    tmp / GameState.WALL_PLACES,
                    tmp % GameState.WALL_PLACES,
                    ix % 2 == 0
                            ? WallOrientation.HORIZONTAL
                            : WallOrientation.VERTICAL
            );
            if (!GameRules.wallMoveCausesCollision(gameState, move)
                    && !GameRules.wallMoveCausesBlocking(gameState, move)) {
                next = move;
            }
            ++ix;
        }
    }
}
