package quoridor.ai.hash;

import com.google.common.collect.Lists;
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

public class ZobristaTest {

    private IncrementalHash<GameState, Move> zobrista = Zobrista.getInstance();

    @Test
    public void afterPawnMoveShouldWork() {
        // given
        WallsState wallsState = WallsState.builder()
                .set(4, 7, WallOrientation.HORIZONTAL)
                .set(7, 7, WallOrientation.VERTICAL)
                .build();
        GameState gameState1 = GameState.builder()
                .setWallsState(wallsState)
                .setPlayersStates(Lists.newArrayList(null, null))
                .setPlayerState(0, PlayerState.of(Direction.UP, 4, 1, 9))
                .setPlayerState(1, PlayerState.of(Direction.DOWN, 4, 8, 9))
                .setTurn(1)
                .build();
        Move move = PawnMove.of(3, 8);
        GameState gameState2 = move.apply(gameState1);

        // when
        long hash1 = zobrista.of(gameState1);
        long hash2 = zobrista.of(gameState2);
        long hashAfter = zobrista.after(gameState1, hash1, move);

        // then
        Assert.assertNotEquals(hash1, hash2);
        Assert.assertEquals(hash2, hashAfter);
    }

    @Test
    public void afterWallMoveShouldWork() {
        // given
        WallsState wallsState = WallsState.builder()
                .set(4, 7, WallOrientation.HORIZONTAL)
                .set(7, 7, WallOrientation.VERTICAL)
                .build();
        GameState gameState1 = GameState.builder()
                .setWallsState(wallsState)
                .setPlayersStates(Lists.newArrayList(null, null))
                .setPlayerState(0, PlayerState.of(Direction.UP, 4, 1, 9))
                .setPlayerState(1, PlayerState.of(Direction.DOWN, 4, 8, 9))
                .setTurn(1)
                .build();
        Move move = WallMove.of(3, 4, WallOrientation.HORIZONTAL);
        GameState gameState2 = move.apply(gameState1);

        // when
        long hash1 = zobrista.of(gameState1);
        long hash2 = zobrista.of(gameState2);
        long hashAfter = zobrista.after(gameState1, hash1, move);

        // then
        Assert.assertNotEquals(hash1, hash2);
        Assert.assertEquals(hash2, hashAfter);
    }
}
