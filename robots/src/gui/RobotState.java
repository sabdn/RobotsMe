package gui;

public class RobotState {
    private volatile double robotPositionX = 100;
    private volatile double robotPositionY = 100;
    private volatile double robotDirection = 0;

    private volatile double angularVelocity = 0.01;

    private volatile double angle = 0;
    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ANGULAR_VELOCITY = 0.01;

    public void setRobotPositionX(double robotPositionX_) {
        robotPositionX = robotPositionX_;
    }

    public double getRobotPositionX() {
        return robotPositionX;
    }

    public void setRobotPositionY(double robotPositionY_) {
        robotPositionY = robotPositionY_;
    }

    public double getRobotPositionY() {
        return robotPositionY;
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    public void setRobotDirection(double robotDirection_) {
        robotDirection = robotDirection_;
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
