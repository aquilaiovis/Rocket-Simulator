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

            //calculates the new gravity
            rocket.setGravity(calculateGravitation(rocket.getFuel() + rocket.getBaseMass(), rocket.getHeight() + Constant.EARTH_RADIUS_M));
            //updates the current result force
            rocket.setResultingForce(calculateResultingForce(rocket.getForce(), rocket.getGravity()));

            calculateVelocity();

            //updates the height
            rocket.setHeight(getNewHeight(interval, rocket.getHeight(), rocket.getVelocity()));

            //updates the weight
            rocket.setFuel(calculateNewWeight(interval, rocket.getFuel(), rocket.getMassLossRate()));

            rocket.saveStep(passedTime);
            passedTime += interval;
        } else {
            running = false;
        }
    }

    private void calculateVelocity() {
        rocket.setVelocity(getNewVelocity(interval, rocket.getVelocity()));
    }


    double getNewVelocity(long deltaTime, double v1) {
        // v2 = deltaTime * (ResultingForce/m1)+ v1
        return (rocket.getResultingForce()) / rocket.getFullWeight() * deltaTime / 1000.0 + v1;
    }
}
