package quoridor.gui.event;

public final class DumpEvent {

    private static DumpEvent instance = new DumpEvent();

    private DumpEvent() {
    }

    public static DumpEvent makeDumpEvent() {
        return instance;
    }
}
