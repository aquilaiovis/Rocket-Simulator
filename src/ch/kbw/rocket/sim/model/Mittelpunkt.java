package ch.kbw.rocket.sim.model;

public class Mittelpunkt extends Algorithm {

    public Mittelpunkt(Rocket rocket, int interval) {
        super(rocket, interval);
    }

    @Override
    public void increment() {
        if (!stalling()) {
            calculateGravitation();
            calculateResultingForce();
            calculateVelocity();
            calculateHeight();
            System.out.println("add");
            calculateFuel();
            System.out.println("sadsa");
            rocket.saveStep(passedTime);
            passedTime += interval;

        } else {
            running = false;
        }
    }

    private boolean stalling() {
        return rocket.getFuel() < 0;
    }

    private void calculateFuel() {

    }

    private void calculateHeight() {

    }

    private void calculateVelocity() {

    }

    private void calculateResultingForce() {

    }

    private void calculateGravitation() {

    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        while (running) {
            increment();
        }
        stopTime = System.currentTimeMillis();
        logPerformance();
    }
}
