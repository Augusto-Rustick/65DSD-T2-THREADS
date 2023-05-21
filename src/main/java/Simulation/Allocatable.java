package Simulation;

public interface Allocatable {
    void allocate() throws InterruptedException;
    void deallocate();
    boolean canAllocate();
    boolean tryAllocate();
}
