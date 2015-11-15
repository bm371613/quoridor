package quoridor.gui.event;

public final class RedoEvent {

    private static RedoEvent instance = new RedoEvent();

    private RedoEvent() {
    }

    public static RedoEvent makeRedoEvent() {
        return instance;
    }

}

