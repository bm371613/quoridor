package quoridor.ai;

import quoridor.core.Move;

public abstract class ThinkingProcess implements Runnable {

    private Move result;

    protected final void setResult(Move result) {
        synchronized (this) {
            this.result = result;
        }
    }

    public final Move getResult() {
        synchronized (this) {
            return result;
        }
    }
}
