package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class ControlOfRobot {

    private Vector<Robot> robots;

    private Vector<Target> targets;
    private final GameVisualizer visualizer;
    private final Timer timer = initTimer();

    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;
    private final double duration = 10;

    private int countRobots;

    public ControlOfRobot() {

        robots = new Vector<>();
        robotSpawner(50, 50);
        robotSpawner(50,  100);
        robotSpawner(50, 150);
        var robot = new Robot(50, 200);
        robot.setMaximumViewingRange(30);
        robots.add(robot);
        targets = new Vector<>();
        targetSpawner(150, 50);
        targetSpawner(150, 100);
        targetSpawner(150, 150);
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

    private void robotSpawner(double startRobotPositionX, double startRobotPositionY){
        var robot = new Robot(startRobotPositionX, startRobotPositionY);
        robots.add(robot);
        countRobots++;
    }

    private void targetSpawner(int x, int y){
        var target = new Target(x, y);
        targets.add(target);
    }

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    public Component getVisualizer() {
        return visualizer;
    }

    public Vector<Robot> getRobots(){
        return robots;
    }

    public Vector<Target> getTargets() {
        return targets;
    }

    public void setTargetPositionX(int newTargetX) {
        targetPositionX = newTargetX;
    }

    public void setTargetPositionY(int newTargetY) {
        targetPositionY = newTargetY;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(visualizer::repaint);
    }

    protected void onModelUpdateEvent() {
        moveRobot(targetPositionX, targetPositionY, duration);
    }

    private Target findMinimumDistanceTarget(Robot robot, int maximumViewingRange){
        var minimumDistance = 12312312321.0; //просто какое-то большое число для сравнения
        Target minTarget = new Target(-1 ,-1);
        for (var target : targets){
            var distance = RobotMath.distance(target.getPositionX(), target.getPositionY(),
                    robot.getRobotPositionX(), robot.getRobotPositionY());
            if (distance > maximumViewingRange){
                continue;
            }
            if (distance < minimumDistance){
                minimumDistance = distance;
                minTarget = target;
            }
        }
        if (Double.compare(minimumDistance, 12312312321.0) == 0){
            var x = (int) (Math.random()*601);
            var y = (int) (Math.random()*601);
            return new Target(x ,y);
        }
        return minTarget;
    }

    public void moveRobot(int targetPositionX, int targetPositionY, double duration) {

        for (var robot : robots) {
            var minTarget = findMinimumDistanceTarget(robot, robot.getMaximumViewingRange());
            var distance = RobotMath.distance(minTarget.getPositionX(), minTarget.getPositionY(),
                    robot.getRobotPositionX(), robot.getRobotPositionY());
            if (distance < 0.5) {
                return;
            }
            targetPositionX = minTarget.getPositionX();
            targetPositionY = minTarget.getPositionY();
            double velocity = Robot.MAX_VELOCITY;
            double angleToTarget = RobotMath.angleTo(robot.getRobotPositionX(), robot.getRobotPositionY(), targetPositionX, targetPositionY);
            double angularVelocity = 0;

            double angle = RobotMath.angleDifference(robot.getRobotDirection(), angleToTarget);

            if (angle < 0) {
                angularVelocity = -Robot.MAX_ANGULAR_VELOCITY;
            } else if (angle > 0) {
                angularVelocity = Robot.MAX_ANGULAR_VELOCITY;
            }

            velocity = RobotMath.applyLimits(velocity, 0, Robot.MAX_VELOCITY);
            angularVelocity = RobotMath.applyLimits(angularVelocity, -Robot.MAX_ANGULAR_VELOCITY, Robot.MAX_ANGULAR_VELOCITY);

            double newX = robot.getRobotPositionX() + velocity * Math.cos(robot.getRobotDirection()) * duration;
            double newY = robot.getRobotPositionY() + velocity * Math.sin(robot.getRobotDirection()) * duration;
            double newDirection = RobotMath.asNormalizedRadians(robot.getRobotDirection() + angularVelocity * duration);

            robot.setRobotPositionX(newX);
            robot.setRobotPositionY(newY);
            robot.setRobotDirection(newDirection);
        }
    }

}
