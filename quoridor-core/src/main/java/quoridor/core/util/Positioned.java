package quoridor.core.util;

public interface Positioned {

    int getX();

    int getY();

    default boolean isAt(int x, int y) {
        return getX() == x && getY() == y;
    }

    default boolean isAt(Positioned p) {
        return isAt(p.getX(), p.getY());
    }

    default boolean isBy(int x, int y) {
        return Math.abs(getX() - x) + Math.abs(getY() - y) == 1;
    }

    default boolean isBy(Positioned p) {
        return isBy(p.getX(), p.getY());
    }
}
