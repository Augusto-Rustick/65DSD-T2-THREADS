package Simulation.mesh.monitor;

import Simulation.mesh.MeshTile;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorMeshTile extends MeshTile {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public MonitorMeshTile(int height, int depth, int id) {
        super(height, depth, id);
    }

    @Override
    protected String getExtraLayer(){
        if (lock.readLock().tryLock()){
            try{
                return "assets/free.png";
            }finally {
                lock.readLock().unlock();
            }
        }else {
            return "assets/waiting.png";
        }
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return "MeshTileWithMonitor{" +
                "lock=" + lock +
                '}';
    }

    @Override
    public void allocate() {
        this.lock.writeLock().lock();
    }

    @Override
    public void deallocate() {
        this.lock.writeLock().unlock();
    }

    @Override
    public boolean canAllocate() {
        try{
            return this.lock.readLock().tryLock();
        }finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public boolean tryAllocate() {
        return this.lock.writeLock().tryLock();
    }
}