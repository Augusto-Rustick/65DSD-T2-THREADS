package Simulation.types;

public enum Method {
    MONITOR(1, "Monitor"), SEMAPHORE(2, "Semáforo");

    final int value;
    final String title;
    Method(int value, String title){
        this.value = value;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
