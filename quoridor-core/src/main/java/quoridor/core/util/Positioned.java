package quoridor.core.util;

public interface Positioned {

    public int getX();

    public int getY();

    default public boolean isAt(int x, int y) {
        return getX() == x && getY() == y;
    }

    default public boolean isAt(Positioned p) {
        return isAt(p.getX(), p.getY());
    }

    default public boolean isBy(int x, int y) {
        return Math.abs(getX() - x) + Math.abs(getY() - y) == 1;
    }

    default public boolean isBy(Positioned p) {
        return isBy(p.getX(), p.getY());
    }
}
