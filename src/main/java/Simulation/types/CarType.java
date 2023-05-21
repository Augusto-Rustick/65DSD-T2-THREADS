package Simulation.types;

public enum CarType {
    FAMILY_CAR(1200), SPORTIVE_CAR(700), TRUCK(2000), HEAVY_TRUCK(2500);
    public final int speed;
    CarType(int speed){
        this.speed = speed;
    }
}
