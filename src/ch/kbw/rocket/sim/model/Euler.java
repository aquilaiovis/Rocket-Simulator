package ch.kbw.rocket.sim.model;

public class Euler extends Algorithm {

    public Euler(Rocket rocket, int interval) {
        super(rocket, interval);
        passedTime = 0;
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

    @Override
    public void increment() {
        if (!stalling()) {
            calculateGravitation();
            calculateResultingForce();
            calculateVelocity();
            calculateHeight();
            calculateFuel();
            rocket.saveStep(passedTime);
            passedTime += interval;
        } else {
            running = false;
        }
    }

    private boolean stalling() {
        return rocket.getFuel() < 0;
    }

    private void calculateGravitation() {
        // needs distance, distance will be the old one
        rocket.setGravity(getGravitationalForce(rocket.getBaseMass() + rocket.getFuel(), rocket.getHeight() + Constant.EARTH_RADIUS_KM));
    }

    private void calculateFuel() {
        rocket.setFuel(getMass(interval, rocket.getFuel(), rocket.getMassLossRate()));
    }

    private void calculateResultingForce() {
        rocket.setResultingForce(getResultingForce(rocket.getForce(), rocket.getGravity()));
    }

    private void calculateVelocity() {
        System.out.println(rocket.getVelocity());
        rocket.setVelocity(getVelocity(interval, rocket.getBaseMass() + rocket.getFuel(), rocket.getResultingForce(), rocket.getVelocity()));
    }

    private void calculateHeight() {
        rocket.setHeight(getHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }

    double getVelocity(long deltaTime, double mass, double resultingForce, double oldVelocity) {
        // v2 = deltaTime * (ResultingForce/m1)+ v1
        return deltaTime / 1000.0 * resultingForce / mass + oldVelocity;
    }
}
