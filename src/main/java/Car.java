import java.util.*;
import java.util.concurrent.Semaphore;

public class Car extends Thread {

    private final Mesh mesh;
    private final CarType carType;
    private final Semaphore vehicle_limit;
    private MeshTile currentLane;
    private MeshTile previousLane;
    private final Random randomNumberGenerator = new Random();

    public Car(Mesh mesh, CarType speed, Semaphore vehicle_limit) {
        this.mesh = mesh;
        this.carType = speed;
        this.vehicle_limit = vehicle_limit;
    }

    private List<int[][]> getCrossings(MeshTile previousLane) {
        List<int[][]> crossingOptions;
        List<int[][]> validCrossingOptions;

        crossingOptions = previousLane.getCrossingFromId();
        validCrossingOptions = new ArrayList<>();
        for (int[][] option : crossingOptions
        ) {
            boolean validOption = false;
            MeshTile simulatedTrip = previousLane;
            for (int[] step : option
            ) {
                validOption = true;
                simulatedTrip = mesh.getMeshTiles()[simulatedTrip.getHeight() + step[0]][simulatedTrip.getDepth() + step[1]];
                if (simulatedTrip.getId() == 0) {
                    validOption = false;
                }
            }
            if (validOption) {
                validCrossingOptions.add(option);
            }
        }
        return validCrossingOptions;
    }

    private void getRandomStart() {
        currentLane = mesh.getEntryPoints().get(randomNumberGenerator.nextInt(mesh.getEntryPoints().size()));
        try {
            vehicle_limit.acquire();
            currentLane.getSemaphore().acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        previousLane = null;
    }

    private void driveThroughTile() {
        if (previousLane != null) {
            previousLane.leaveTile();
            previousLane.getSemaphore().release();
        }
        previousLane = currentLane;
        currentLane.enterTile();
        currentLane.setCar(carType);

        try {
            sleep(carType.speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldExit() {
        if (mesh.getExitPoints().contains(currentLane)) {
            currentLane.getSemaphore().release();
            currentLane.leaveTile();
            vehicle_limit.release();
            return true;
        }
        return false;
    }

    private void pickNextMove() {
        int[] actions = currentLane.getActionsFromId();
        int action = 0;
        int random = 0;
        while (action == 0) {
            random = randomNumberGenerator.nextInt(2);
            action = actions[random];
        }

        if (random == 0) {
            currentLane = mesh.getMeshTiles()[currentLane.getHeight() + action][currentLane.getDepth()];
        } else {
            currentLane = mesh.getMeshTiles()[currentLane.getHeight()][currentLane.getDepth() + action];
        }

        try {
            if (currentLane.getId() >= 5) {
                List<int[][]> validCrossingOptions;
                validCrossingOptions = getCrossings(previousLane);
                allocateTilesForCrossing(validCrossingOptions);
            } else {
                currentLane.getSemaphore().acquire();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void allocateTilesForCrossing(List<int[][]> validCrossingOptions) throws InterruptedException {

        int[][] move = validCrossingOptions.get(randomNumberGenerator.nextInt(validCrossingOptions.size()));
        Stack<MeshTile> path;

        do {
            MeshTile simulatedLane = previousLane;
            path = new Stack<>();
            for (int[] step : move) {
                simulatedLane = mesh.getMeshTiles()[simulatedLane.getHeight() + step[0]][simulatedLane.getDepth() + step[1]];
                if (simulatedLane.getSemaphore().tryAcquire()) {
                    path.push(simulatedLane);
                } else {
                    for (MeshTile lane : path
                    ) {
                        if (lane.getSemaphore().availablePermits() == 0) {
                            lane.getSemaphore().release();
                        }
                    }
                    sleep(randomNumberGenerator.nextInt(250) + 250);
                    break;
                }
            }
        } while (path.size() != move.length);

        performCross(move, path);
    }

    private void performCross(int[][] move, Stack<MeshTile> path) throws InterruptedException {
        previousLane.leaveTile();
        previousLane.getSemaphore().release();
        MeshTile currentLaneDuringCross = previousLane;
        for (int i = 0; i < move.length-1; i++) {
            currentLaneDuringCross = mesh.getMeshTiles()[currentLaneDuringCross.getHeight() + move[i][0]][currentLaneDuringCross.getDepth() + move[i][1]];
            currentLaneDuringCross.setCar(carType);
            currentLaneDuringCross.enterTile();
            sleep((int)((float)carType.speed*1.5));
            currentLaneDuringCross.leaveTile();
            previousLane = currentLane;
        }
        currentLane = mesh.getMeshTiles()[currentLaneDuringCross.getHeight() + move[move.length-1][0]][currentLaneDuringCross.getDepth() + move[move.length-1][1]];
        for (int i = 1; i < path.size()-1; i++) {
            path.get(i).getSemaphore().release();
        }
    }

    @Override
    public void run() {
        getRandomStart();
        while (true) {
            driveThroughTile();
            if (shouldExit()) {
                break;
            }
            pickNextMove();
        }
    }
}
