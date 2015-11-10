package quoridor.ai.bot;

import java.util.List;
import java.util.Random;

import quoridor.ai.ThinkingProcess;
import quoridor.core.GameRules;
import quoridor.core.Move;
import quoridor.core.state.GameState;

public class RandomBot implements Bot {

    private Random random = new Random(System.currentTimeMillis());

    @Override
    public ThinkingProcess thinkAbout(GameState gameState) {
        return new ThinkingProcess() {
            @Override
            public void run() {
                List<Move> moves = GameRules.getLegalMoves(gameState);
                Move move = moves.get(random.nextInt(moves.size()));
                setResult(move);
            }
        };
    }
}
