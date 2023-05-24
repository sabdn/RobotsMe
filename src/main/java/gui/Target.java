package gui;

public class Target {
    private volatile int positionX;
    private volatile int positionY;

    public Target(int x, int y){
        positionX = x;
        positionY = y;
    }

    public void setPositionX(int positionX){
        this.positionX = positionX;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionY() {
        return positionY;
    }
}
