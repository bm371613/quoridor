package quoridor.gui.player;

import quoridor.gui.event.EventListener;

public interface Player extends EventListener {
    void makeTurn(EventListener moveEventListener);
    void moveAccepted();
}
