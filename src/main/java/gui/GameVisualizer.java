package gui;

import logic.RobotConst;
import logic.Robot;
import logic.RobotControl;
import logic.Target;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final RobotControl controller;
    public GameVisualizer(RobotControl robotController) {
        controller = robotController;
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
//        var robots = new ArrayList<>();
//        robots.addAll(controller.getRobots());
        var robots = controller.getRobots();
        synchronized(robots) {
            for (var robot : robots) {
                int robotCenterX = round(robot.getRobotPositionX());
                int robotCenterY = round(robot.getRobotPositionY());
                drawRobot(g2d, robot.getRobotDirection(), robotCenterX, robotCenterY, robot);
            }
        }
        var targets = controller.getTargets();
        synchronized (targets) {
            for (var target : targets) {
                drawTarget(g2d, target.getPositionX(), target.getPositionY(), target);
            }
        }
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, double direction, int robotCenterX, int robotCenterY, Robot robot) {
        AffineTransform t = AffineTransform.getRotateInstance(
                direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(robot.getColor());
        fillOval(g, robotCenterX, robotCenterY, RobotConst.ROBOT_WIDTH, RobotConst.ROBOT_HEIGHT); // сам робот
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, RobotConst.ROBOT_WIDTH, RobotConst.ROBOT_HEIGHT); // оконтовка робота
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + RobotConst.DISTANCE_FROM_BODY_TO_EYE, robotCenterY, RobotConst.EYE_RADIUS, RobotConst.EYE_RADIUS); // глаз робота
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + RobotConst.DISTANCE_FROM_BODY_TO_EYE, robotCenterY, RobotConst.EYE_RADIUS, RobotConst.EYE_RADIUS); // окантовка глаза
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawTarget(Graphics2D g, int x, int y, Target target)
    {
        AffineTransform t = AffineTransform.getRotateInstance(
                0, 0, 0);
        g.setTransform(t);
        g.setColor(target.getColor());
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
