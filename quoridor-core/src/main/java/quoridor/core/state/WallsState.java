package quoridor.core.state;

import quoridor.core.position.Positioned;

public final class WallsState {
    private final WallOrientation[] walls;

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

    // helpers

    private boolean isWallAbove(int x, int y) {
        return y == GameState.WALL_PLACES
                || get(x, y) == WallOrientation.HORIZONTAL
                || get(x - 1, y) == WallOrientation.HORIZONTAL;
    }

    private boolean isWallBelow(int x, int y) {
        return y == 0 || isWallAbove(x, y - 1);
    }

    private boolean isWallAtRight(int x, int y) {
        return x == GameState.WALL_PLACES
                || get(x, y) == WallOrientation.VERTICAL
                || get(x, y - 1) == WallOrientation.VERTICAL;
    }

    private boolean isWallAtLeft(int x, int y) {
        return x == 0 || isWallAtRight(x - 1, y);
    }

    public boolean isWallBetween(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && Math.abs(y1 - y2) == 1) {
            return isWallAbove(x1, Math.min(y1, y2));
        } else if (y1 == y2 && Math.abs(x1 - x2) == 1) {
            return isWallAtRight(Math.min(x1, x2), y1);
        } else {
            throw new RuntimeException("Bad arguments");
        }
    }

    public boolean isWallBetween(Positioned p1, Positioned p2) {
        return isWallBetween(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public boolean isWallBehind(Positioned p1, Positioned p2) {
        assert p1.isBy(p2);
        if (p1.getX() == p2.getX()) {
            if (p1.getY() < p2.getY()) {
                return isWallAbove(p2.getX(), p2.getY());
            } else {
                return isWallBelow(p2.getX(), p2.getY());
            }
        } else {
            if (p1.getX() < p2.getX()) {
                return isWallAtRight(p2.getX(), p2.getY());
            } else {
                return isWallAtLeft(p2.getX(), p2.getY());
            }
        }
    }

    // builder

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
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
