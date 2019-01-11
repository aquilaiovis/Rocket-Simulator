package ch.kbw.rocket.sim.model;

public class PredictorCorrector extends Algorithm {

    public PredictorCorrector(Rocket rocket, int interval) {
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
            //calculate gravitational force
            rocket.setGravity(calculateGravitation(rocket.getBaseMass() + rocket.getFuel(), rocket.getHeight() + Constant.EARTH_RADIUS_M));

            //calculate resulting force
            rocket.setResultingForce(calculateResultingForce(rocket.getForce(), rocket.getGravity()));

            calculatePrediction();

            //calculates new height
            rocket.setHeight(getNewHeight(interval, rocket.getHeight(), rocket.getVelocity()));

            //calculate fuel loss
            rocket.setFuel(calculateNewWeight(interval, rocket.getFuel(), rocket.getMassLossRate()));

            rocket.saveStep(passedTime);
            passedTime += interval;
        } else {
            running = false;
        }
    }

    @Override
    double getNewVelocity(long deltaTime, double v1) {
        return (rocket.getResultingForce()) / rocket.getFullWeight() * deltaTime / 1000.0 + v1;
    }

    private void calculatePrediction() {
        double sum = rocket.getVelocity() + getNewVelocity(interval, getNewVelocity(interval, rocket.getVelocity()));
        rocket.setVelocity(sum / 2.0);
    }
}
