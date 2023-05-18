import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class CarInserter extends Thread {

    private final static CarType[] carTypes = new CarType[]{
        CarType.FAMILY_CAR,
//        CarType.SPORTIVE_CAR,
//        CarType.TRUCK,
//        CarType.HEAVY_TRUCK
    };
    private final Mesh mesh;
    private int delay = 1000;
    private final Semaphore vehicle_limit;

    public CarInserter(int delay, Mesh mesh, int vehicle_limit) {
        this.delay = delay;
        this.mesh = mesh;
        this.vehicle_limit = new Semaphore(vehicle_limit);
    }

    public CarInserter(Mesh mesh) {
        this.delay = 1000;
        this.mesh = mesh;
        this.vehicle_limit = new Semaphore(10);
    }

    public CarInserter(Mesh mesh, int vehicle_limits) {
        this.delay = 1000;
        this.mesh = mesh;
        this.vehicle_limit = new Semaphore(vehicle_limits);
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setVehicle_limit(int vehicle_limit) {
//        this.vehicle_limit = this.vehicle_limit.drainPermits(vehicle_limit);
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Car car = new Car(mesh, carTypes[random.nextInt(carTypes.length)], vehicle_limit);
            car.start();
        }
    }
}
