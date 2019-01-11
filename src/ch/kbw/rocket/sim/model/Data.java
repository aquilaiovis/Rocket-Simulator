package ch.kbw.rocket.sim.model;

public class Data {
    private double value;
    private long timestamp;

    Data(double value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
