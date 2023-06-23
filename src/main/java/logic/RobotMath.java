package logic;
/** Класс с набором математических операций для движения робота
 @author ColdStorm, Sabdn
 @version 1.0
 */
public class RobotMath {
    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public static double distance(double x1, double y1, double x2, double y2){
        return calculateDistance(x1, y1, x2, y2);
    }

    public static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public static double asNormalizedRadians(double angle) { //приводим угол в пределы 360-ти градусов
        var twoPi = 2 * Math.PI;
        return ((angle % twoPi) + twoPi) % twoPi;
    }

    public static double applyLimits(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double angleDifference(double angle1, double angle2){
        double diff = asNormalizedRadians(angle2) - asNormalizedRadians(angle1);
        if(Math.abs(diff) < Math.PI) return diff;
        if(diff >= 0) return diff - 2 *Math.PI;
        return 2 * Math.PI + diff;
    }
}
