package ch.kbw.rocket.sim.model;


import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public abstract class Algorithm implements Runnable {
    Rocket rocket;
    int interval;
    long passedTime, startTime, stopTime;
    boolean running;


    public Algorithm(Rocket rocket, int interval) {
        running = true;
        this.rocket = rocket;
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

    double getGravitationalForce(double mass, double r) {
        // g * (m1 * m2 / r*r)
        return Constant.GRAVITATIONAL * (mass * Constant.EARTH_MASS / Math.pow(r, 2));
    }

    double getMass(long deltaTime/*in ms*/, double oldMass, double massLossRate) {
        // m2 = m1 - deltaTime * WeightLoss
        return oldMass - deltaTime / 1000.0 * massLossRate;
    }

    double getResultingForce(double force, double gravitationalForce) {
        // ResultingForce = Force - gravitation
        return force - gravitationalForce;
    }

    abstract double getVelocity(long deltaTime, double mass, double resultingForce, double oldVelocity);



    double getHeight(long deltaTime/*in ms*/, double height, double velocity) {
        // h2= h1+ deltaTime * v2
        return height + deltaTime / 1000.0 * velocity;
    }

    public Rocket getRocket() {
        return rocket;
    }

     double velocity(long t, double v1) {
        return getVelocity(t, rocket.getBaseMass() + rocket.getFuel(), rocket.getResultingForce(), v1);
    }

    public abstract void increment();
}
