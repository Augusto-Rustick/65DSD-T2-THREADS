import java.util.Random;
import java.util.concurrent.Semaphore;

public class Car extends Thread{

    private final Mesh mesh;
    private final CarType carType;
    private final Semaphore vehicle_limit;

    public Car(Mesh mesh, CarType speed, Semaphore vehicle_limit){
        this.mesh = mesh;
        this.carType = speed;
        this.vehicle_limit = vehicle_limit;
    }

    @Override
    public void run() {

        Random rand = new Random();

        // Pega um ponto de inserção aleatório, e tenta obter acesso a aquele bloco
        MeshTile currentLane = mesh.getEntryPoints().get(rand.nextInt(mesh.getEntryPoints().size()));
        try {
            currentLane.getSemaphore().acquire();
            vehicle_limit.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        MeshTile previousLane = null;
        while(true){
            if(previousLane != null){
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
            int[] actions = currentLane.getActionsFromId(currentLane.getId());
            int action = 0;
            int random = 0;
            while (action == 0){
                random = rand.nextInt(2);
                action = actions[random];
            }
            if(mesh.getExitPoints().contains(currentLane)){
                currentLane.getSemaphore().release();
                currentLane.leaveTile();
                vehicle_limit.release();
                break;
            }
            if (random == 0){
                currentLane = mesh.getMeshTiles()[currentLane.getHeight()+action][currentLane.getDepth()];
            }else{
                currentLane = mesh.getMeshTiles()[currentLane.getHeight()][currentLane.getDepth()+action];
            }
            try {
                currentLane.getSemaphore().acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            previousLane.leaveTile();
        }
    }
}
