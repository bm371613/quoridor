package quoridor.core.state;

public class WallsState {
    private WallOrientation[] walls;

    private WallsState() {
        walls = new WallOrientation[
                GameState.WALL_PLACES * GameState.WALL_PLACES];
    }

    private static int ix(int x, int y) {
        if (x < 0 || x >= GameState.WALL_PLACES || y < 0
                || y >=GameState. WALL_PLACES) {
            throw new RuntimeException("Coordinates out of bounds");
        }
        return GameState.WALL_PLACES * x + y;
    }

    public WallOrientation get(int x, int y) {
        return walls[ix(x, y)];
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
            result.walls[ix(x, y)] = wall;
            return this;
        }

        public WallsState build() {
            return result;
        }

    }
}
