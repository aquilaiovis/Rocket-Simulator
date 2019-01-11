package ch.kbw.rocket.sim.model;


import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;

public abstract class Algorithm implements Runnable {
    Rocket rocket;
    int interval;
    long passedTime, startTime, stopTime;
    boolean running;


    public Algorithm(Rocket rocket, int interval) {
        running = true;
        this.rocket = new Rocket(rocket);
        this.interval = interval;
    }

    void logPerformance() {
        try {
            String fileContent = millisToDate(System.currentTimeMillis())
                    + "\nCalcTime: " + (stopTime - startTime) +
                    "ms \nInterval: " + interval + "ms" +
                    "\n\nstarttime: " + startTime
                    + "\nstoptime: " + stopTime
                    + "\nalgorithm: " + this.getClass().getSimpleName()
                    + "\n" + rocket.toString();
            System.out.println(fileContent);

            FileWriter fileWriter = new FileWriter(System.currentTimeMillis() + "performance.log");
            fileWriter.write(fileContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String millisToDate(long millis) {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(millis);
    }

    double calculateGravitation(double mass, double r) {
        // g * (m1 * m2 / r*r)
        return Constant.GRAVITATIONAL * (mass * Constant.EARTH_MASS / Math.pow(r, 2));
    }

    double calculateNewWeight(long deltaTime/*in ms*/, double oldMass, double massLossRate) {
        // m2 = m1 - deltaTime * WeightLoss
        return oldMass - deltaTime / 1000.0 * massLossRate;
    }

    double calculateResultingForce(double force, double gravitationalForce) {
        // ResultingForce = Force - gravitation
        return force - gravitationalForce;
    }

    double getNewHeight(long deltaTime/*in ms*/, double height, double velocity) {
        // h2= h1+ deltaTime * v2
        return height + deltaTime / 1000.0 * velocity;
    }

    public Rocket getRocket() {
        return rocket;
    }

    public boolean stalling() {
        return rocket.getFuel() < 0;
    }

    public abstract void increment();

    abstract double getNewVelocity(long deltaTime, double v1);

    public void setRunning(boolean running)
    {
        this.running = running;
    }
}