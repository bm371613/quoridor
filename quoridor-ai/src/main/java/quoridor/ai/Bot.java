package quoridor.ai;

import quoridor.core.state.GameState;

public interface Bot {
    ThinkingProcess thinkAbout(GameState gameState);
}
