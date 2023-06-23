package logic;

import java.awt.*;
import java.util.HashMap;

/** Класс служит для хранения размеров робота и в будущем ещё чёнибудь наркутим
 @author ColdStorm, Sabdn
 @version 1.0
 */
public class RobotConst {
    public final static int ROBOT_WIDTH = 30;
    public final static int ROBOT_HEIGHT = 10;
    public final static int EYE_RADIUS = 5;
    public final static int DISTANCE_FROM_BODY_TO_EYE = 10;

    public final static Color DEFAULT_COLOR_FAST_ROBOTS = new Color(255, 255, 0);
    public final static Color DEFAULT_COLOR_HARDY_ROBOTS = new Color(0, 255, 255);
    public final static Color DEFAULT_COLOR_VIGILANT_ROBOTS = new Color(255, 0, 255);
    public final static Color DEFAULT_COLOR_SOME_MORE_ROBOTS = new Color(255, 255, 0);
    public final static double MAXIMUM_VIEWING_RANGE = 100;
    public final static double DEFAULT_VIEWING_RANGE = 50;
    public final static double MINIMUM_VIEWING_RANGE = 10;
    public final static double MAXIMUM_SPEED = 0.1;
    public final static double DEFAULT_SPEED = 0.05;
    public final static double MINIMUM_SPEED = 0.01;
    public final static double MAXIMUM_HEALTH = 300;
    public final static double MINIMUM_HEALTH = 150;
    public final static int NUMBER_SECONDS_AIFE_AFTER_DEATH = 5;
    public final static int NUMBER_SECONDS_EATING = 4;

    public final static int MATURE_AGE_IN_SECONDS = 50;
    public final static int ENOUGH_HEALTH_FOR_BREEDING = 70;
    public final static int MINIMUM_HEALTH_FOR_BREEDING = 30;
    public final static int DURATION_ACT_OF_LOVE_IN_SECONDS = 10;

    public final static String NAME_HEALTH = "health";
    public final static String NAME_VIEWING_RANGE = "viewing range";
    public final static String NAME_SPEED = "speed";
    public final static HashMap<String, Double> MINIMUM_PECULIARITIES_VALUES = new HashMap<>();
    static {
        MINIMUM_PECULIARITIES_VALUES.put(NAME_HEALTH, MINIMUM_HEALTH);
        MINIMUM_PECULIARITIES_VALUES.put(NAME_VIEWING_RANGE, MINIMUM_VIEWING_RANGE);
        MINIMUM_PECULIARITIES_VALUES.put(NAME_SPEED, MINIMUM_SPEED);
    }

    public final static String[] NAMES = new String[] {"Боб", "Мартин", "Лютер", "Санечка", "Сын маминой подруги",
            "С именем твой мамы", "Саня лох"};
}
