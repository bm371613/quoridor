package quoridor.ai.thinking_process;

import quoridor.core.move.Move;

public abstract class IterativeDeepeningThinkingProcess
        extends ThinkingProcess {

    private boolean keepDeepening = true;

    protected abstract Move choose(int depth);

    protected final void stopDeepening() {
        keepDeepening = false;
    }

    @Override
    public void run() {
        int depth = 0;
        while (keepDeepening) {
            setResult(choose(depth));
            depth += 1;
        }
    }

}
