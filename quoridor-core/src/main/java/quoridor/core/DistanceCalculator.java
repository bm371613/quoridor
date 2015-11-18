package quoridor.core;

import java.util.function.Predicate;

import quoridor.core.position.Position;
import quoridor.core.position.Positioned;
import quoridor.core.state.GameState;
import quoridor.core.state.WallsState;

public final class DistanceCalculator {

    public static final int INFINITY = Integer.MAX_VALUE;
    public static final int DISTANCE_UPPER_BOUND =
            GameState.PLACES * GameState.PLACES;

    private static final DistanceCalculator INSTANCE = new DistanceCalculator();

    private Queue queue = new Queue();
    private int[][] distances = new int[GameState.PLACES][GameState.PLACES];

    private DistanceCalculator() {
    }

    public static DistanceCalculator getInstance() {
        return INSTANCE;
    }

    public synchronized int calculateDistance(WallsState walls, Positioned from,
                Predicate<Positioned> to) {
        queue.clear();
        for (int i = 0; i < GameState.PLACES; ++i) {
            for (int j = 0; j < GameState.PLACES; ++j) {
                distances[i][j] = INFINITY;
            }
        }

        queue.push(from.getX(), from.getY());
        distances[from.getX()][from.getY()] = 0;
        int x, y;
        while (!queue.isEmpty()) {
            x = queue.getFrontX();
            y = queue.getFrontY();
            queue.pop();

            if (to.test(Position.of(x, y))) {
                return distances[x][y];
            }

            visitNeighbor(walls, queue, distances, x, y, x - 1, y);
            visitNeighbor(walls, queue, distances, x, y, x + 1, y);
            visitNeighbor(walls, queue, distances, x, y, x, y - 1);
            visitNeighbor(walls, queue, distances, x, y, x, y + 1);
        }

        return INFINITY;
    }

    private static void visitNeighbor(WallsState walls, Queue queue,
            int[][] distances, int x, int y, int nx, int ny) {
        int d = distances[x][y] + 1;
        if (0 <= nx && nx < GameState.PLACES
                && 0 <= ny && ny < GameState.PLACES
                && distances[nx][ny] > d
                && !walls.isWallBetween(x, y, nx, ny)) {
            distances[nx][ny] = d;
            queue.push(nx, ny);
        }
    }

    private static class Queue {
        private final int[] xs = new int[GameState.PLACES * GameState.PLACES];
        private final int[] ys = new int[GameState.PLACES * GameState.PLACES];
        private int front;
        private int end;

        public void clear() {
            front = 0;
            end = 0;
        }

        public void push(int x, int y) {
            xs[end] = x;
            ys[end] = y;
            ++end;
        }

        public void pop() {
            ++front;
        }

        public int getFrontX() {
            return xs[front];
        }

        public int getFrontY() {
            return ys[front];
        }

        public boolean isEmpty() {
            return front == end;
        }
    }
}
