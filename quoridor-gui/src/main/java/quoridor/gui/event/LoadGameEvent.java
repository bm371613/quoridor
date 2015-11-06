package quoridor.gui.event;

import lombok.Value;

import quoridor.core.state.GameState;
import quoridor.gui.player.Player;
import quoridor.gui.util.PerPlayer;

@Value
public class LoadGameEvent {
    private final GameState gameState;
    private final PerPlayer<Player> players;
}
