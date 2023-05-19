package utils;

public enum CarType {
    FAMILY_CAR(1000), SPORTIVE_CAR(300), TRUCK(1500), HEAVY_TRUCK(2500);
    public final int speed;
    CarType(int speed){
        this.speed = speed;
    }
}
