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
//        spawnRobot(50, 50);
//        spawnRobot(50, 100);
//        spawnRobot(50, 150);
        makeFastRobot(50, 50);
        makeVigilantRobot(50, 100);
        makeHardyRobot(50, 150);
        var robot = new Robot(50, 200);
        robot.setSightCircle(60);
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

    private void makeFastRobot(double startRobotPositionX, double startRobotPositionY){
        var robot = new Robot(startRobotPositionX, startRobotPositionY);
        robot.setSpeed(LocalConst.MAXIMUM_SPEED);
        robot.setSightCircle(LocalConst.DEFAULT_VIEWING_RANGE);
        robot.setHealth(LocalConst.DEFAULT_HEALTH);
        robot.setOriginHealth(LocalConst.DEFAULT_HEALTH);
        robot.setColor(LocalConst.DEFAULT_COLOR_FAST_ROBOTS);
        robots.add(robot);
    }

    private void makeVigilantRobot(double startRobotPositionX, double startRobotPositionY){
        var robot = new Robot(startRobotPositionX, startRobotPositionY);
        robot.setSpeed(LocalConst.DEFAULT_SPEED);
        robot.setSightCircle(LocalConst.MAXIMUM_VIEWING_RANGE);
        robot.setHealth(LocalConst.DEFAULT_HEALTH);
        robot.setOriginHealth(LocalConst.DEFAULT_HEALTH);
        robot.setColor(LocalConst.DEFAULT_COLOR_VIGILANT_ROBOTS);
        robots.add(robot);
    }

    private void makeHardyRobot(double startRobotPositionX, double startRobotPositionY){
        var robot = new Robot(startRobotPositionX, startRobotPositionY);
        robot.setSpeed(LocalConst.DEFAULT_SPEED);
        robot.setSightCircle(LocalConst.DEFAULT_VIEWING_RANGE);
        robot.setHealth(LocalConst.MAXIMUM_HEALTH);
        robot.setOriginHealth(LocalConst.MAXIMUM_HEALTH);
        robot.setColor(LocalConst.DEFAULT_COLOR_HARDY_ROBOTS);
        robots.add(robot);
    }

    private void targetSpawner(int x, int y) {
        var target = new Target(x, y);
        targets.add(target);
    }

    private void returnToOriginalLovingSettings(Robot robot){
        robot.setLookingForCouple(false);
        robot.setAlreadyTaken(false);
        robot.setActOfLove(false);
        robot.resetNumberSecondsActOfLove();
        robot.setPartner(null);
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
                if (robot.getAge() >= LocalConst.MATURE_AGE_IN_SECONDS && robot.getHealth() > LocalConst.ENOUGH_HEALTH_FOR_BREEDING){
                    robot.setLookingForCouple(true);
                }
                if (robot.isLookingForCouple() && robot.getHealth() < LocalConst.MINIMUM_HEALTH_FOR_BREEDING){
                    if (!robot.isAlreadyTaken()) {
                        returnToOriginalLovingSettings(robot);
                    } else {
                        var partner = robot.getPartner();
                        partner.setPartner(null);
                        returnToOriginalLovingSettings(robot);
                    }
                }
                if (robot.isActOfLove()){
                    robot.increaseSecondsActOfLoveByOne();
                    if (robot.getNumberSecondsActOfLove() >= LocalConst.DURATION_ACT_OF_LOVE_IN_SECONDS){
                        makeNewRobot(robot, robot.getPartner());
                        returnToOriginalLovingSettings(robot.getPartner());
                        returnToOriginalLovingSettings(robot);
                    }
                }
            }
        }
        var x = (int) (Math.random() * 601);
        var y = (int) (Math.random() * 601);
        targetSpawner(x, y);
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

    private Target makeRandomGhostTarget(){
        var x = (int) (Math.random() * 601);
        var y = (int) (Math.random() * 601);
        return new Target(x, y);
    }

    private void checkingRobotsAround(Robot robot){
        for (var tempRobot : robots){
            if (robot != tempRobot) {
                var distanceBetweenRobots = RobotMath.distance(robot.getRobotPositionX(), robot.getRobotPositionY(), tempRobot.getRobotPositionX(), tempRobot.getRobotPositionY());
                if (distanceBetweenRobots > robot.getSightCircle()) {
                    continue;
                }
                if (tempRobot.isLookingForCouple() && !tempRobot.isAlreadyTaken()) {
                    tempRobot.setAlreadyTaken(true);
                    tempRobot.setPartner(robot);
                    robot.setAlreadyTaken(true);
                    robot.setPartner(tempRobot);
                }
            }
        }
        throw new RuntimeException();
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
    private Target makeGhostTargetBetweenRobots(Robot firstRobot, Robot secondRobot){
        var targetX = (int) ((firstRobot.getRobotPositionX() + secondRobot.getRobotPositionX()) / 2);
        var targetY = (int) ((firstRobot.getRobotPositionY() + secondRobot.getRobotPositionY()) / 2);
        return new Target(targetX, targetY);
    }
    public void moveRobot(double duration) {
        for (var robot : robots) {
            if (robot.getHealth() <= 0) {
                continue;
            }
            var needNewFood = false;
            var minTarget = findMinimumDistanceTarget(robot, robot.getSightCircle());
            if (!robot.isLookingForCouple()) {
                var distance = RobotMath.distance(minTarget.getPositionX(), minTarget.getPositionY(),
                        robot.getRobotPositionX(), robot.getRobotPositionY());
                if (distance < 0.5) {
                    if (!robot.isEating()) {
                        robot.setEating(true);
                    }
                    if (robot.getNumberSecondsEating() >= LocalConst.NUMBER_SECONDS_EATING) {
                        robot.setEating(false);
                        robot.setNumberSecondsEating(0);
                        robot.increaseHealth(10);
                        needNewFood = true;
                    } else {
                        continue;
                    }
                } else {
                    if (robot.isEating()) {
                        robot.setEating(false);
                        robot.setNumberSecondsEating(0);
                        robot.increaseHealth(10);
                    }
                }
                minTarget = findMinimumDistanceTarget(robot, robot.getSightCircle());
            } else {
                try {
                    if (!robot.isAlreadyTaken()) {
                        checkingRobotsAround(robot);
                    }
                    var distanceBetweenRobots = RobotMath.distance(robot.getRobotPositionX(), robot.getRobotPositionY(),
                            robot.getPartner().getRobotPositionX(), robot.getPartner().getRobotPositionY());
                    if (distanceBetweenRobots < 0.5){
                        robot.setActOfLove(true);
                        robot.getPartner().setActOfLove(true);
                        continue;
                    }
                    minTarget = makeGhostTargetBetweenRobots(robot, robot.getPartner());
                }catch (RuntimeException e){
                    if (!robot.isAlreadyTaken()){
                        minTarget = makeRandomGhostTarget();
                    }
                }

            }
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
    private void makeNewRobot(Robot firstRobot, Robot secondRobot){
        SplittableRandom random = new SplittableRandom();
        var robot = new Robot(firstRobot.getRobotPositionX(), firstRobot.getRobotPositionY());

        var lucky = random.nextInt(4) == 0; //вероятность выпадения 0 равна 25%
        if (lucky){
            robot.setSpeed(Math.max(firstRobot.getSpeed(), secondRobot.getSpeed()));
        } else {
            var speed = Math.max((firstRobot.getSpeed() + secondRobot.getSpeed()) / 2, LocalConst.MINIMUM_SPEED);
            robot.setSpeed(speed);
        }

        lucky = random.nextInt(4) == 0;
        if (lucky){
            robot.setHealth(Math.max(firstRobot.getOriginHealth(), secondRobot.getOriginHealth()));
        } else {
            var health = Math.min(firstRobot.getOriginHealth(), secondRobot.getOriginHealth());
            robot.setHealth(health);
            if (robot.getHealth() == 0){
                System.out.println("Первый робот");
                System.out.println(firstRobot.getOriginHealth());
                System.out.println("Второй робот");
                System.out.println(secondRobot.getOriginHealth());
            }
        }

        lucky = random.nextInt(4) == 0;
        if (lucky){
            robot.setSightCircle(Math.max(firstRobot.getSightCircle(), secondRobot.getSightCircle()));
        } else {
            var sightCircle = Math.max((firstRobot.getSightCircle() + secondRobot.getSightCircle()) / 2,
                    LocalConst.MINIMUM_VIEWING_RANGE);
            robot.setSightCircle(sightCircle);
        }

        robot.setColor(colorMixing(firstRobot, secondRobot));
        robots.add(robot);
    }

}
