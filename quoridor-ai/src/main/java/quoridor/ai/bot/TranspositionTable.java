package quoridor.ai.bot;

import java.util.ArrayList;

final class TranspositionTable<E> {
    private int size;
    private final long[] hash;
    private final ArrayList<E> value;
    private final int[] depth;

    TranspositionTable(int size) {
        this.size = size;
        this.hash = new long[size];
        this.value = new ArrayList<>(size);
        this.depth = new int[size];
        for (int i = 0; i < size; ++i) {
            this.value.add(null);
            this.depth[i] = -1;
        }
    }

    boolean has(long hash, int depth) {
        int ix = this.ix(hash);
        return this.hash[ix] == hash && this.depth[ix] >= depth;
    }

    E get(long hash) {
        return this.value.get(ix(hash));
    }

    void set(long hash, int depth, E value) {
        int ix = this.ix(hash);
        this.hash[ix] = hash;
        this.value.set(ix, value);
        this.depth[ix] = depth;
    }

    private int ix(long hash) {
        int result = (int) (hash % size);
        return result < 0 ? result + size : result;
    }
}

