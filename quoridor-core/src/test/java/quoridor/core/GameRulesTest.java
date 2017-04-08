package quoridor.core;

import java.util.Set;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import quoridor.core.direction.Direction;
import quoridor.core.move.Move;
import quoridor.core.move.PawnMove;
import quoridor.core.move.WallMove;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.WallsState;

public class GameRulesTest {

    @Test
    public void shouldAllowLegalSimplePawnMove() {
        GameState gs = GameState.builder()
                .copyFrom(GameRules.makeInitialStateForTwo())
                .setPlayerState(0, PlayerState.of(Direction.UP, 4, 6, 0))
                .build();

        Assert.assertTrue(GameRules.isLegalMove(gs, PawnMove.of(4, 7)));
        Assert.assertTrue(GameRules.isLegalMove(gs, PawnMove.of(4, 5)));
        Assert.assertTrue(GameRules.isLegalMove(gs, PawnMove.of(3, 6)));
        Assert.assertTrue(GameRules.isLegalMove(gs, PawnMove.of(5, 6)));
    }

    @Test
    public void shouldNotAllowIllegalSimplePawnMove() {
        GameState gs = GameState.builder()
                .copyFrom(GameRules.makeInitialStateForTwo())
                .setPlayerState(0, PlayerState.of(Direction.UP, 4, 6, 0))
                .build();

        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(3, 5)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(3, 5)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(5, 7)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(3, 7)));

        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(4, 8)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(4, 4)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(2, 6)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(6, 6)));

        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(1, 3)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(3, 2)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(5, 4)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(3, 7)));
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
                .setPlayerState(0, PlayerState.of(Direction.UP, 4, 6, 0))
                .setWallsState(wallsState)
                .build();

        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(4, 7)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(4, 5)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(3, 6)));
        Assert.assertFalse(GameRules.isLegalMove(gs, PawnMove.of(5, 6)));
    }

    @Test
    public void shouldAllowLegalWallMove() {
        // given
        GameState gameState = GameRules.makeInitialStateForTwo();

        // when
        Set<Move> wallMoves = Sets.newHashSet(Iterators.filter(
            GameRules.getLegalMoves(gameState),
            (move) -> move instanceof WallMove));

        // then
        Assert.assertEquals(wallMoves.size(), 128);

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                Assert.assertTrue(wallMoves.contains(WallMove.of(
                        x, y, WallOrientation.HORIZONTAL
                )));
                Assert.assertTrue(wallMoves.contains(WallMove.of(
                        x, y, WallOrientation.VERTICAL
                )));
            }
        }
    }

}
