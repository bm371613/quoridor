package quoridor.core;

public class PlayerState {
    private int posX;
    private int posY;
    private int wallsLeft;

    public PlayerState(int posX, int posY, int wallsLeft) {
        this.posX = posX;
        this.posY = posY;
        this.wallsLeft = wallsLeft;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWallsLeft() {
        return wallsLeft;
    }

    public void setWallsLeft(int wallsLeft) {
        this.wallsLeft = wallsLeft;
    }
}
