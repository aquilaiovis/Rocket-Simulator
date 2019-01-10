package ch.kbw.rocket.sim.model;

public class PredictorCorrector extends Algorithm {

    public PredictorCorrector(Rocket rocket, int interval) {
        super(rocket, interval);
        passedTime = 0;
    }

    private void init() {
        calculateGravitation();
        calculateResultingForce();
        rocket.setVelocity(getNewVelocity(interval, rocket.getVelocity()));
    }

    @Override
    public void run() {
        init();
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
            calculatePrediction();
            calculateHeight();
            calculateFuel();

            rocket.saveStep(passedTime);
            passedTime += interval;
        } else {
            running = false;
        }
    }

    @Override
    double getNewVelocity(long deltaTime, double v1) {
        return 0;
    }

    private void calculatePrediction() {
        double sum = rocket.getVelocity() + getNewVelocity(interval, getNewVelocity(interval, rocket.getVelocity()));
        rocket.setVelocity(sum / 2.0);
    }


    private void calculateFuel() {
        rocket.setFuel(calculateNewWeight(interval, rocket.getFuel(), rocket.getMassLossRate()));
    }

    private void calculateHeight() {
        rocket.setHeight(getNewHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }

    private void calculateResultingForce() {
        rocket.setResultingForce(calculateResultingForce(rocket.getForce(), rocket.getGravity()));
    }

    private void calculateGravitation() {
        rocket.setGravity(calculateGravitation(rocket.getBaseMass() + rocket.getFuel(), rocket.getHeight() + Constant.EARTH_RADIUS_M));
    }
}
