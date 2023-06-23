package logic;

import java.awt.*;
/** Класс служит для хранения текущего состояния цели
 @author ColdStorm, Sabdn
 @version 1.0
 */
public class Target {
    private volatile int positionX;
    private volatile int positionY;

    private double weight = 10;

    private Color color = new Color(0, 255, 0);

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }
}
