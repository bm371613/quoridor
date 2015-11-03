package quoridor.gui.event;

import com.google.common.collect.ImmutableList;
import quoridor.gui.management.PlayerType;

public class NewGameEvent {

    private ImmutableList<PlayerType> playerTypes;

    private NewGameEvent(PlayerType... playerTypes) {
        this.playerTypes = ImmutableList.copyOf(playerTypes);
    }

    public static NewGameEvent gameForTwo() {
        return new NewGameEvent(PlayerType.HUMAN, PlayerType.HUMAN);
    }

    public static NewGameEvent gameForFour() {
        return new NewGameEvent(PlayerType.HUMAN, PlayerType.HUMAN,
                PlayerType.HUMAN, PlayerType.HUMAN);
    }

    public ImmutableList<PlayerType> getPlayerTypes() {
        return playerTypes;
    }
}
