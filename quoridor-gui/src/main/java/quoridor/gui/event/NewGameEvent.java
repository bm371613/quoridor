package quoridor.gui.event;

import quoridor.gui.management.PlayerType;

public class NewGameEvent {

    private PlayerType topPlayerType;
    private PlayerType bottomPlayerType;

    public NewGameEvent(PlayerType topPlayerType, PlayerType bottomPlayerType) {
        this.topPlayerType = topPlayerType;
        this.bottomPlayerType = bottomPlayerType;
    }
}
