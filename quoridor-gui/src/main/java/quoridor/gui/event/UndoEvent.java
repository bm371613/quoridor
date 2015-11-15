package quoridor.gui.event;

public final class UndoEvent {

    private static UndoEvent instance = new UndoEvent();

    private UndoEvent() {
    }

    public static UndoEvent makeUndoEvent() {
        return instance;
    }

}
