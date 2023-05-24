package gui;

public class Robot {
    private volatile double robotPositionX;
    private volatile double robotPositionY;
    private volatile double robotDirection = 0;

    private volatile double angularVelocity = 0.01;

    private volatile double angle = 0;
    public static final double MAX_VELOCITY = 0.1;
    public static final double MAX_ANGULAR_VELOCITY = 0.010105;

    private int maximumViewingRange = 100;

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

    public void setAngle(double angle_) {
        angle = angle_;
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
}
