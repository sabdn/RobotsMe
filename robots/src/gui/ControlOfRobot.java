package gui;

import jdk.jshell.execution.Util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class ControlOfRobot {
    private final RobotState robot;
    private final GameVisualizer visualizer;
    private final Timer timer = initTimer();

    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;
    private final double duration = 10;

    public ControlOfRobot() {
        robot = new RobotState();
        visualizer = new GameVisualizer(this);

        timer.schedule(new TimerTask() { // эта штука
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);

        visualizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPositionX(e.getPoint().x);
                setTargetPositionY(e.getPoint().y);
                visualizer.repaint();
            }
        });
        visualizer.setDoubleBuffered(true);
    }

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    public GameVisualizer getVisualizer() {
        return visualizer;
    }

    public double getRobotPositionX() {
        return robot.getRobotPositionX();
    }

    public double getRobotPositionY() {
        return robot.getRobotPositionY();
    }

    public double getRobotDirection() {
        return robot.getRobotDirection();
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }

    public int getTargetPositionY() {
        return targetPositionY;
    }

    public void setTargetPositionX(int newTargetX) {
        this.targetPositionX = newTargetX;
    }

    public void setTargetPositionY(int newTargetY) {
        this.targetPositionY = newTargetY;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(visualizer::repaint);
    }

    protected void onModelUpdateEvent() {
        moveRobot(targetPositionX, targetPositionY, duration);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle) { //приводим угол в пределы 360-ти градусов
        var twoPi = 2 * Math.PI;
        return ((angle % twoPi) + twoPi) % twoPi;
    }

    private static double applyLimits(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }


//    public void moveRobot(int targetPositionX, int targetPositionY, double duration) {
//        double distance = distance(targetPositionX, targetPositionY,
//                robot.getRobotPositionX(), robot.getRobotPositionY());
//        if (distance < 0.5) {
//            return;
//        }
//        double velocity = RobotState.MAX_VELOCITY;
//        double angleToTarget = angleTo(robot.getRobotPositionX(),
//                robot.getRobotPositionY(), targetPositionX, targetPositionY);
//        double angularVelocity = 0;
//
//        double angle = asNormalizedRadians(angleToTarget - robot.getRobotDirection());
//
//        if (angle < Math.PI / 2) {
//            angularVelocity = RobotState.MAX_ANGULAR_VELOCITY;
//        } else if (angle > Math.PI / 2) {
//            angularVelocity = -RobotState.MAX_ANGULAR_VELOCITY;
//        }
//
//        velocity = applyLimits(velocity, 0, RobotState.MAX_VELOCITY);
//        angularVelocity = applyLimits(angularVelocity, -RobotState.MAX_ANGULAR_VELOCITY,
//                RobotState.MAX_ANGULAR_VELOCITY);
//        double newX = robot.getRobotPositionX() + velocity / angularVelocity *
//                (Math.sin(robot.getRobotDirection() + angularVelocity * duration) -
//                        Math.sin(robot.getRobotDirection()));
//        if (!Double.isFinite(newX)) {
//            newX = robot.getRobotPositionX() +
//                    velocity * duration * Math.cos(robot.getRobotDirection());
//        }
//        double newY = robot.getRobotPositionY() - velocity / angularVelocity *
//                (Math.cos(robot.getRobotDirection() + angularVelocity * duration) -
//                        Math.cos(robot.getRobotDirection()));
//        if (!Double.isFinite(newY)) {
//            newY = robot.getRobotPositionY() +
//                    velocity * duration * Math.sin(robot.getRobotDirection());
//        }
//        robot.setRobotPositionX(newX);
//        robot.setRobotPositionY(newY);
//        double newDirection =
//                asNormalizedRadians(robot.getRobotDirection() +
//                        angularVelocity * duration);
//        robot.setRobotDirection(newDirection);
//    }

    static double angleDifference(double angle1, double angle2){
        double diff = asNormalizedRadians(angle2) - asNormalizedRadians(angle1);
        if(Math.abs(diff) < Math.PI) return diff;
        if(diff >= 0) return diff - 2 *Math.PI;
        return 2 * Math.PI + diff;
    }

    public void move() {
        double distance = distance(targetPositionX, targetPositionY,
                robot.getRobotPositionX(), robot.getRobotPositionY());
        double velocity = RobotState.MAX_VELOCITY;
        if(distance < velocity){
            return;
        }
        robot.setRobotPositionX(robot.getRobotPositionX() + velocity * Math.cos(robot.getAngle()));
        robot.setRobotPositionY(robot.getRobotPositionY() + velocity * Math.sin(robot.getAngle()));
        double newDirection =
                asNormalizedRadians(robot.getRobotDirection() +
                        robot.getAngularVelocity() * duration);
        robot.setRobotDirection(newDirection);
    }

    public void moveRobot(int targetPositionX, int targetPositionY, double duration){
        var x = robot.getRobotPositionX();
        var y = robot.getRobotPositionY();

        robot.setAngularVelocity(applyLimits(robot.getAngularVelocity(), -RobotState.MAX_ANGULAR_VELOCITY,
                RobotState.MAX_ANGULAR_VELOCITY));
        double angDiff = angleDifference(robot.getAngle(), angleTo(x, y, targetPositionX, targetPositionY));

        var angularVelocity = robot.getAngularVelocity();
        var angle = robot.getAngle();
        if(Math.abs(angDiff) > angularVelocity) {
            robot.setAngle(angle + Math.signum(angDiff) * angularVelocity);
        }
        move();
    }

}
