package logic;

import java.util.Objects;
import java.util.SplittableRandom;

public class Peculiarity {
    private double value;
    private String namePeculiarity;


    public Peculiarity(double value) {
        this.value = value;
    }

    public Peculiarity(double value, String name){
        this.value = value;
        this.namePeculiarity = name;
    }

    public static Peculiarity crossing(Peculiarity firstPeculiarity, Peculiarity secondPeculiarity) {
        SplittableRandom random = new SplittableRandom();
        var lucky = random.nextInt(4) == 0; //вероятность выпадения 0 равна 25%
        if (Objects.equals(firstPeculiarity.getNamePeculiarity(), RobotConst.NAME_HEALTH)){
            if (lucky){
                return new Peculiarity(RobotConst.MAXIMUM_HEALTH);
            } else {
                return new Peculiarity(RobotConst.MINIMUM_HEALTH);
            }
        }
        double value;
        if (lucky) {
            value = Math.max(firstPeculiarity.getValue(), secondPeculiarity.getValue());
        } else {
            value = Math.max((firstPeculiarity.getValue() + secondPeculiarity.getValue()) / 2,
                    RobotConst.MINIMUM_PECULIARITIES_VALUES.get(firstPeculiarity.getNamePeculiarity()));
        }
        return new Peculiarity(value, firstPeculiarity.getNamePeculiarity());
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getNamePeculiarity() {
        return namePeculiarity;
    }

    public void setNamePeculiarity(String namePeculiarity) {
        this.namePeculiarity = namePeculiarity;
    }
}
