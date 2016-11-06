package quoridor.ai.bot;

import java.util.ArrayList;

final class TranspositionTable<E> {
    private int size;
    private final long[] hash;
    private final ArrayList<E> value;

    TranspositionTable(int size) {
        this.size = size;
        this.hash = new long[size];
        this.value = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            this.value.add(null);
        }
    }

    E get(long hash) {
        int ix = this.ix(hash);
        return this.hash[ix] == hash ? this.value.get(ix) : null;
    }

    void set(long hash, E value) {
        int ix = this.ix(hash);
        this.hash[ix] = hash;
        this.value.set(ix, value);
    }

    private int ix(long hash) {
        int result = (int) (hash % size);
        return result < 0 ? result + size : result;
    }
}

