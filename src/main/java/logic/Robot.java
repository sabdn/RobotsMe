package logic;

import java.awt.*;

/** Класс служит для хранения текущего состояния робота
 @author ColdStorm, Sabdn
 @version 1.0
 */
public class Robot {
    public static final double MAX_VELOCITY = 0.1;
    public static final double MAX_ANGULAR_VELOCITY = 0.010105;

    private volatile double robotPositionX;
    private volatile double robotPositionY;
    private volatile double robotDirection = 0;
    private volatile double angularVelocity = 0.01;
    private volatile double angle = 0;
    private volatile int sightCircle = 1000000;
    private volatile double power = 0.5;
    private volatile double speed = 0.1;
    private volatile Color color = new Color(139, 0, 255);
    private volatile int health = 10;
    private volatile int age = 0;
    private volatile int numberSecondsAifeAfterDeath = 0;
    private volatile boolean eating = false;
    private volatile int numberSecondsEating = 0;

    private volatile boolean lookingForCouple = false;
    private volatile boolean alreadyTaken = false;
    private volatile boolean actOfLove = false;
    private volatile int numberSecondsActOfLove;
    private volatile int OriginHealth;

    private volatile Robot partner;
    private volatile int coolDownAfterActOfLove = 0;
    private volatile boolean needCoolDownAfterActOfLove = false;


    public Robot(double startRobotPositionX, double startRobotPositionY){
        robotPositionX = startRobotPositionX;
        robotPositionY = startRobotPositionY;
    }

    public void setRobotPositionX(double positionX) {
        robotPositionX = positionX;
    }
    public double getRobotPositionX() {
        return robotPositionX;
    }
    public void setRobotPositionY(double positionY_) {
        robotPositionY = positionY_;
    }
    public double getRobotPositionY() {
        return robotPositionY;
    }
    public double getRobotDirection() {
        return robotDirection;
    }
    public void setRobotDirection(double direction) {
        robotDirection = direction;
    }
    public void setAngle(double newAngle) {
        angle = newAngle;
    }
    public double getAngle() {
        return angle;
    }
    public double getAngularVelocity() {
        return angularVelocity;
    }
    public void setAngularVelocity(double angularVelocity_) {
        angularVelocity = angularVelocity_;
    }
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }
    public double getPower() {
        return power;
    }
    public void setPower(double newPower) {
        power = newPower;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color newColor) {
        color = newColor;
    }
    public int getHealth() {
        return health;
    }
    public synchronized void increaseHealth(int value){
        health = health + value;
    }
    public synchronized void decreaseHealth(int value){
        health = health - value;
    }
    public synchronized void reduceHealthByOne(){
        health--;
        if (health == 0){
            setColor(new Color(190, 190, 190));
        }
    }
    public synchronized void increaseNumberSecondsAifeAfterDeathByOne(){
        numberSecondsAifeAfterDeath++;
    }
    public int getNumberSecondsAifeAfterDeath() {
        return numberSecondsAifeAfterDeath;
    }
    public int getAge() {
        return age;
    }
    public synchronized void increaseAgeByOne(){
        age++;
    }
    public void setEating(boolean eating) {
        this.eating = eating;
    }
    public boolean isEating(){
        return eating;
    }
    public void increaseNumberSecondsEatingByOne(){
        numberSecondsEating++;
    }
    public int getNumberSecondsEating() {
        return numberSecondsEating;
    }
    public void setNumberSecondsEating(int numberSecondsEating) {
        this.numberSecondsEating = numberSecondsEating;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public boolean isLookingForCouple() {
        return lookingForCouple;
    }
    public void setLookingForCouple(boolean lookingForCouple) {
        this.lookingForCouple = lookingForCouple;
    }
    public boolean isAlreadyTaken() {
        return alreadyTaken;
    }
    public void setAlreadyTaken(boolean alreadyTaken) {
        this.alreadyTaken = alreadyTaken;
    }
    public boolean isActOfLove() {
        return actOfLove;
    }
    public void setActOfLove(boolean actOfLove) {
        this.actOfLove = actOfLove;
    }
    public int getNumberSecondsActOfLove() {
        return numberSecondsActOfLove;
    }
    public synchronized void increaseSecondsActOfLoveByOne(){
        numberSecondsActOfLove++;
    }
    public void resetNumberSecondsActOfLove(){
        numberSecondsActOfLove = 0;
    }
    public Robot getPartner() {
        return partner;
    }
    public void setPartner(Robot partner) {
        this.partner = partner;
    }
    public void setOriginHealth(int originHealth) {
        OriginHealth = originHealth;
    }
    public int getOriginHealth() {
        return OriginHealth;
    }
    public int getSightCircle() {
        return sightCircle;
    }
    public void setSightCircle(int newMaximumViewingRange) {
        sightCircle = newMaximumViewingRange;
    }
    public int getCoolDownAfterActOfLove() {
        return coolDownAfterActOfLove;
    }
    public void resetCoolDownAfterActOfLove() {
        coolDownAfterActOfLove = 0;
    }
    public synchronized void increaseCoolDownAfterActOfLoveByOne(){
        coolDownAfterActOfLove++;
    }
    public boolean isNeedCoolDownAfterActOfLove() {
        return needCoolDownAfterActOfLove;
    }
    public void setNeedCoolDownAfterActOfLove(boolean needCoolDownAfterActOfLove) {
        this.needCoolDownAfterActOfLove = needCoolDownAfterActOfLove;
    }

}
