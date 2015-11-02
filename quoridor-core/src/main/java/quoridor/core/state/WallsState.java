package quoridor.core.state;

public class WallsState {
    private WallOrientation[] walls;

    private WallsState() {
        walls = new WallOrientation[
                GameState.WALL_PLACES * GameState.WALL_PLACES];
    }

    private static boolean inBounds(int x, int y) {
        return 0 <= x && x < GameState.WALL_PLACES && 0 <= y
                && y < GameState.WALL_PLACES;
    }

    private static int ix(int x, int y) {
        return GameState.WALL_PLACES * x + y;
    }

    public WallOrientation get(int x, int y) {
        return inBounds(x, y) ? walls[ix(x, y)] : null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private WallsState result;

        private Builder() {
            result = new WallsState();
        }

        public Builder copyFrom(WallsState wallsState) {
            System.arraycopy(wallsState.walls, 0, result.walls, 0,
                    wallsState.walls.length);
            return this;
        }

        public Builder set(int x, int y, WallOrientation wall) {
            if (!inBounds(x, y)) {
                throw new RuntimeException("Coordinates out of bounds");
            }
            result.walls[ix(x, y)] = wall;
            return this;
        }

        public WallsState build() {
            return result;
        }

    }
}
