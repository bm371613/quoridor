package quoridor.ai.bot;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import quoridor.ai.value_function.TopOpponentDistanceComparison;
import quoridor.core.direction.Direction;
import quoridor.core.move.Move;
import quoridor.core.move.PawnMove;
import quoridor.core.state.GameState;
import quoridor.core.state.PlayerState;
import quoridor.core.state.WallOrientation;
import quoridor.core.state.WallsState;

public class MinmaxBotTest {

    @Test
    public void shouldMinimizeLoss() {
        // given
        WallsState wallsState = WallsState.builder()
                .set(0, 1, WallOrientation.VERTICAL)
                .set(0, 2, WallOrientation.HORIZONTAL)
                .set(1, 0, WallOrientation.HORIZONTAL)
                .set(1, 6, WallOrientation.VERTICAL)
                .set(1, 7, WallOrientation.HORIZONTAL)
                .set(2, 5, WallOrientation.HORIZONTAL)
                .set(3, 0, WallOrientation.HORIZONTAL)
                .set(3, 1, WallOrientation.VERTICAL)
                .set(3, 3, WallOrientation.VERTICAL)
                .set(3, 5, WallOrientation.VERTICAL)
                .set(3, 7, WallOrientation.HORIZONTAL)
                .set(4, 3, WallOrientation.VERTICAL)
                .set(4, 5, WallOrientation.VERTICAL)
                .set(5, 0, WallOrientation.HORIZONTAL)
                .set(5, 2, WallOrientation.VERTICAL)
                .set(5, 4, WallOrientation.VERTICAL)
                .set(5, 6, WallOrientation.VERTICAL)
                .set(5, 7, WallOrientation.HORIZONTAL)
                .build();
        GameState gameState = GameState.builder()
                .setWallsState(wallsState)
                .setPlayersStates(Lists.newArrayList(null, null))
                .setPlayerState(0, PlayerState.of(Direction.UP, 4, 5, 0))
                .setPlayerState(1, PlayerState.of(Direction.DOWN, 7, 1, 2))
                .setTurn(0)
                .build();
        MinimaxThinkingProcess thinkingProcess = new MinimaxThinkingProcess(
                TopOpponentDistanceComparison.getInstance(),
                gameState
        );

        // when
        Move[] moves = {
            thinkingProcess.choose(0),
            thinkingProcess.choose(1),
            thinkingProcess.choose(2),
            thinkingProcess.choose(3)
        };

        // then
        for (Move move : moves) {
            Assert.assertEquals(move, PawnMove.of(4, 4));
        }
    }
}
