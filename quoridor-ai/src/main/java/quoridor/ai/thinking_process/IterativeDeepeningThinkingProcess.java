package quoridor.ai.thinking_process;

import quoridor.core.move.Move;

public abstract class IterativeDeepeningThinkingProcess
        extends ThinkingProcess {

    protected abstract Move choose(int depth);

    @Override
    public void run() {
        int depth = 0;
        while (true) {
            setResult(choose(depth));
            depth += 1;
        }
    }

}
