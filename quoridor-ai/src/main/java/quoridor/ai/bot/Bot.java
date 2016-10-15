package quoridor.ai.bot;

import quoridor.ai.thinking_process.ThinkingProcess;
import quoridor.core.state.GameState;

public interface Bot {
    ThinkingProcess thinkAbout(GameState gameState);
}
