package quoridor.gui.event;

import lombok.Value;

import quoridor.core.direction.PerDirection;
import quoridor.core.state.GameState;
import quoridor.gui.player.Player;

@Value
public class LoadGameEvent {
    private final GameState gameState;
    private final PerDirection<Player> players;
}
