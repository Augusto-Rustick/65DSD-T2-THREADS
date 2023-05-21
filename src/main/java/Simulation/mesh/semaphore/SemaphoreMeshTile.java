package Simulation.mesh.semaphore;

import Simulation.mesh.MeshTile;

import java.util.concurrent.Semaphore;

public class SemaphoreMeshTile extends MeshTile {

    private final int permits = 1;
    private final Semaphore semaphore = new Semaphore(permits);

    public SemaphoreMeshTile(int height, int depth, int id) {
        super(height, depth, id);
    }

    @Override
    protected String getExtraLayer(){
        if (semaphore.availablePermits() == 1){
            return "assets/free.png";
        }else if(semaphore.availablePermits() == 0){
            return "assets/waiting.png";
        }else{
            return "assets/car_null.png";
        }
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    @Override
    public String toString() {
        return "MeshTileWithSemaphore{" +
                "semaphore=" + semaphore +
                '}';
    }

    @Override
    public void allocate() throws InterruptedException {
        this.semaphore.acquire();
    }

    @Override
    public void deallocate() {
        this.semaphore.release();
    }

    @Override
    public boolean canAllocate() {
        return this.semaphore.availablePermits() == permits;
    }

    @Override
    public boolean tryAllocate() {
        return this.semaphore.tryAcquire();
    }

}