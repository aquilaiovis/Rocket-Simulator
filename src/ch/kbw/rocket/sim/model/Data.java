package ch.kbw.rocket.sim.model;

public class Data {
    private double value;
    private long timestamp;

    public Data(double value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Data(){}

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
