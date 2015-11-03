package quoridor.gui.event;

import quoridor.core.state.GameState;
import quoridor.gui.management.Player;
import quoridor.gui.util.PerPlayer;

public class NewGameEvent {

    private GameState gameState;
    private PerPlayer<Player> players;

    public NewGameEvent(GameState gameState, PerPlayer<Player> players) {
        this.gameState = gameState;
        this.players = players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public PerPlayer<Player> getPlayers() {
        return players;
    }
}
