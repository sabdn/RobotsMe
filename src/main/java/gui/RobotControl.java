package gui;

import java.awt.*;
import java.util.*;

/**
 * Класс служит для управления роботом
 *
 * @author ColdStorm, Sabdn
 * @version 1.0
 */
public class RobotControl {

    private ArrayList<Robot> robots;

    private ArrayList<Target> targets;

    private final GameVisualizer visualizer;
    private final Timer timer = initTimer();
    private final double duration = 10;

    private int countRobots;

    public RobotControl() {
        robots = new ArrayList<>();
        spawnRobot(50, 50);
        spawnRobot(50, 100);
        spawnRobot(50, 150);
        var robot = new Robot(50, 200);
        robot.setMaximumViewingRange(60);
        robot.setSpeed(0.05);
        robots.add(robot);
        targets = new ArrayList<>();
        targetSpawner(150, 50);
        targetSpawner(150, 100);
        targetSpawner(150, 150);
        targetSpawner(200, 200);
        visualizer = new GameVisualizer(this);

        timer.schedule(new TimerTask() { // эта штука
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 10);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateElements();
            }
        }, 0, 1000);

        visualizer.setDoubleBuffered(true);
    }

    private void spawnRobot(double startRobotPositionX, double startRobotPositionY) {
        var robot = new Robot(startRobotPositionX, startRobotPositionY);
        robots.add(robot);
    }

    private void targetSpawner(int x, int y) {
        var target = new Target(x, y);
        targets.add(target);
    }

    private void updateElements() {
        synchronized (robots){
            for (var i = 0; i < robots.size(); i++){
                var robot = robots.get(i);
                robot.reduceHealthByOne();
                robot.increaseAgeByOne();
                if (robot.getHealth() <= 0){
                    robot.increaseNumberSecondsAifeAfterDeathByOne();
                    if (robot.getNumberSecondsAifeAfterDeath() > 9){
                        robots.remove(robot);
                    }
                }
                if (robot.isEating()){
                    robot.increaseNumberSecondsEatingByOne();
                }
            }
        }
    }

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    public Component getVisualizer() {
        return visualizer;
    }

    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(visualizer::repaint);
    }

    protected void onModelUpdateEvent() {
        moveRobot(duration);
    }

    private int randomNumber(){
        return (int) (Math.random() * 601);
    }

    private Color colorMixing(Robot firstRobot, Robot secondRobot) {
        var firstRed = firstRobot.getColor().getRed();
        var firstGreen = firstRobot.getColor().getGreen();
        var firstBlue = firstRobot.getColor().getBlue();
        var secondRed = secondRobot.getColor().getRed();
        var secondGreen = secondRobot.getColor().getGreen();
        var secondBlue = secondRobot.getColor().getBlue();
        var newRed = (firstRed + secondRed) / 2;
        var newGreen = (firstGreen + secondGreen) / 2;
        var newBlue = (secondBlue + firstBlue) / 2;
        return new Color(newRed, newGreen, newBlue);
    }

    private Target findMinimumDistanceTarget(Robot robot, int maximumViewingRange) {
        var minimumDistance = 12312312321.0; //просто какое-то большое число для сравнения
        Target minTarget = new Target(-1, -1);
        for (var target : targets) {
            var distance = RobotMath.distance(target.getPositionX(), target.getPositionY(),
                    robot.getRobotPositionX(), robot.getRobotPositionY());
            if (distance > maximumViewingRange) {
                continue;
            }
            if (distance < minimumDistance) {
                minimumDistance = distance;
                minTarget = target;
            }
        }
        if (Double.compare(minimumDistance, 12312312321.0) == 0) {
            var x = (int) (Math.random() * 601);
            var y = (int) (Math.random() * 601);
            return new Target(x, y);
        }
        return minTarget;
    }

    public void moveRobot(double duration) {
        for (var robot : robots) {
            var needNewFood = false;
            if (robot.getHealth() <= 0) {
                continue;
            }
            var minTarget = findMinimumDistanceTarget(robot, robot.getMaximumViewingRange());
            var distance = RobotMath.distance(minTarget.getPositionX(), minTarget.getPositionY(),
                    robot.getRobotPositionX(), robot.getRobotPositionY());
            if (distance < 0.5) {
                if (!robot.isEating()) {
                    robot.setEating(true);
                }
                if (robot.getNumberSecondsEating() >= 4){
                    robot.setEating(false);
                    robot.setNumberSecondsEating(0);
                    robot.increaseHealth(10);
                    needNewFood = true;
                } else {
                    continue;
                }
            } else {
                if (robot.isEating()){
                    robot.setEating(false);
                    robot.setNumberSecondsEating(0);
                    robot.increaseHealth(10);
                }
            }
            minTarget = findMinimumDistanceTarget(robot, robot.getMaximumViewingRange());
            if (needNewFood){
                targets.remove(minTarget);
                var x = randomNumber();
                var y = randomNumber();
                targetSpawner(x, y);
            }

            var targetPositionX = minTarget.getPositionX();
            var targetPositionY = minTarget.getPositionY();
            double velocity = robot.getSpeed();
            double angleToTarget = RobotMath.angleTo(robot.getRobotPositionX(), robot.getRobotPositionY(), targetPositionX, targetPositionY);
            double angularVelocity = 0;

            double angle = RobotMath.angleDifference(robot.getRobotDirection(), angleToTarget);

            if (angle < 0) {
                angularVelocity = -Robot.MAX_ANGULAR_VELOCITY;
            } else if (angle > 0) {
                angularVelocity = Robot.MAX_ANGULAR_VELOCITY;
            }

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
