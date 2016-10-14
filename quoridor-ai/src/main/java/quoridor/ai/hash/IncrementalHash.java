package quoridor.ai.hash;

public interface IncrementalHash<S, C> {
    long of(S state);
    long after(S state, long hash, C change);
}
