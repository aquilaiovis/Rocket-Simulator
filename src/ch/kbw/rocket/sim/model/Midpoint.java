package ch.kbw.rocket.sim.model;

public class Midpoint extends Algorithm {

    public Midpoint(Rocket rocket, int interval) {
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

            //calculates the new gravity
            rocket.setGravity(calculateGravitation(rocket.getFuel() + rocket.getBaseMass(), rocket.getHeight() + Constant.EARTH_RADIUS_M));
            //updates the current result force
            rocket.setResultingForce(calculateResultingForce(rocket.getForce(), rocket.getGravity()));

            midpointCalculation();

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

    @Override
    double getNewVelocity(long deltaTime, double v1) {
        //this method isn't required here, because with the midpoint method uses other arguments
        return 0;
    }

    private void midpointCalculation() {
        double v = getNewSpeed((rocket.getFullWeight() + rocket.getBaseMass() + calculateNewWeight(interval, rocket.getFuel(), rocket.getMassLossRate())) / 2, rocket.getVelocity());
        rocket.setVelocity(v);
    }

    private double getNewSpeed(double m, double v1) {
        double m2 = calculateGravitation(m, rocket.getHeight() + Constant.EARTH_RADIUS_M);
        return (rocket.getForce() - m2) / rocket.getFullWeight() * interval / 1000.0 + v1;
    }
}
