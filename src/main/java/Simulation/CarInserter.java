package Simulation;

import Simulation.mesh.Mesh;
import Simulation.types.CarType;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class CarInserter extends Thread {

    private final static CarType[] carTypes = new CarType[]{
        CarType.FAMILY_CAR,
        CarType.SPORTIVE_CAR,
        CarType.TRUCK,
        CarType.HEAVY_TRUCK
    };
    private final Mesh mesh;
    private final int delay;
    private final Semaphore vehicle_limit;
    private volatile boolean keepAlive = true;

    public CarInserter(int delay, Mesh mesh, int vehicle_limit) {
        this.delay = delay;
        this.mesh = mesh;
        this.vehicle_limit = new Semaphore(vehicle_limit);
    }


    public void stopThread() {
        keepAlive = false;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (keepAlive) {
            try {
                sleep(delay);
                if (keepAlive & vehicle_limit.availablePermits() > 0) {
                    Car car = new Car(mesh, carTypes[random.nextInt(carTypes.length)], vehicle_limit);
                    car.start();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
