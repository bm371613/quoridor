package quoridor.core;

import org.junit.Assert;
import org.junit.Test;

import quoridor.core.direction.Direction;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.WallsState;

public class GameRulesTest {

    @Test
    public void shouldAllowLegalSimplePawnMove() {
        GameState gs = GameState.builder()
                .copyFrom(GameRules.makeInitialStateForTwo())
                .setPlayerState(0, PlayerState.of(Direction.TOP, 4, 6, 0))
                .build();

        Assert.assertTrue(GameRules.isLegalMove(gs, Move.makePawnMove(4, 7)));
        Assert.assertTrue(GameRules.isLegalMove(gs, Move.makePawnMove(4, 5)));
        Assert.assertTrue(GameRules.isLegalMove(gs, Move.makePawnMove(3, 6)));
        Assert.assertTrue(GameRules.isLegalMove(gs, Move.makePawnMove(5, 6)));
    }

    @Test
    public void shouldNotAllowIllegalSimplePawnMove() {
        GameState gs = GameState.builder()
                .copyFrom(GameRules.makeInitialStateForTwo())
                .setPlayerState(0, PlayerState.of(Direction.TOP, 4, 6, 0))
                .build();

        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(3, 5)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(3, 5)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(5, 7)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(3, 7)));

        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(4, 8)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(4, 4)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(2, 6)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(6, 6)));

        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(1, 3)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(3, 2)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(5, 4)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(3, 7)));
    }

    @Test
    public void shouldNotAllowCrossingWalls() {
        WallsState wallsState = WallsState.builder()
                .set(4, 6, WallOrientation.HORIZONTAL)
                .set(3, 5, WallOrientation.HORIZONTAL)
                .set(3, 6, WallOrientation.VERTICAL)
                .set(4, 5, WallOrientation.VERTICAL)
                .build();
        GameState gs = GameState.builder()
                .copyFrom(GameRules.makeInitialStateForTwo())
                .setPlayerState(0, PlayerState.of(Direction.TOP, 4, 6, 0))
                .setWallsState(wallsState)
                .build();

        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(4, 7)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(4, 5)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(3, 6)));
        Assert.assertFalse(GameRules.isLegalMove(gs, Move.makePawnMove(5, 6)));
    }

    @Test
    public void shouldNotAllowMovingOutOfBoard() {
        // TODO
    }

    @Test
    public void shouldAllowLegalJumpForward() {
        // TODO
    }

    @Test
    public void shouldNotAllowJumpingOutOfBoard() {
        // TODO
    }

    @Test
    public void shouldAllowLegalJumpWithTurn() {
        // TODO
    }

    @Test
    public void shouldNotAllowJumpingWithTurnOutOfBoard() {
        // TODO
    }

    @Test
    public void shouldNotAllowJumpWithTurnWithoutWallBehindOpponent() {
        // TODO
    }

    @Test
    public void shouldNotAllowQuasiJumpBackToTheSamePlace() {
        // TODO
    }

    @Test
    public void shouldAllowLegalWallMove() {
        // TODO
    }

    @Test
    public void shouldNotAllowWallMoveIfNoWallsAreLeft() {
        // TODO
    }

    @Test
    public void shouldNotAllowWallMoveOutOfBoard() {
        // TODO
    }

    @Test
    public void shouldNotAllowParallelWallCollision() {
        // TODO
    }

    @Test
    public void shouldNotAllowOrthogonalWallCollision() {
        // TODO
    }

    @Test
    public void shouldNotAllowBlocking() {
        // TODO
    }
}
