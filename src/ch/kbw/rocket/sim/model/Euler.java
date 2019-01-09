package ch.kbw.rocket.sim.model;

public class Euler extends Algorithm {

    public Euler(Rocket rocket, int interval) {
        super(rocket, interval);
        passedTime = 0;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();

        rocket.setGravity(calculateGravitation(rocket.getBaseMass() + rocket.getFuel(), rocket.getHeight() + Constant.EARTH_RADIUS_M));
        while (running) {
            increment();
        }
        stopTime = System.currentTimeMillis();
        logPerformance();
    }

    @Override
    public void increment() {
        if (!stalling()) {

            rocket.setResultingForce(calculateResultingForce(rocket.getForce(), rocket.getGravity()));

            calculateVelocity();
            calculateHeight();

            rocket.setFuel(calculateNewWeight(interval, rocket.getFuel(), rocket.getMassLossRate()));
            rocket.saveStep(passedTime);
            passedTime += interval;
        } else {
            running = false;
        }
    }

    private void calculateVelocity() {
        rocket.setVelocity(getNewSpeed(rocket.getVelocity(), rocket.getHeight()));
    }

    private double getNewSpeed(double v1, double h1) {
        double m2 = calculateGravitation(rocket.getFuel() + rocket.getBaseMass(), h1 + Constant.EARTH_RADIUS_M);
        System.out.println(rocket.getGravity()-m2);
        rocket.setGravity(m2);
        return (rocket.getForce() - m2) / m2 * interval / 1000.0 + v1;
    }


    private void calculateHeight() {
        rocket.setHeight(getHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }

    double getVelocity(long deltaTime, double mass, double resultingForce, double oldVelocity) {
        // v2 = deltaTime * (ResultingForce/m1)+ v1
        return deltaTime / 1000.0 * resultingForce / mass + oldVelocity;
    }
}
