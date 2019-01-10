package ch.kbw.rocket.sim.model;

//FIXME: not correct
public class Midpoint extends Algorithm {
    public Midpoint(Rocket rocket, int interval) {
        super(rocket, interval);
        passedTime = 0;

    }

    @Override
    public void run() {
      /*  startTime = System.currentTimeMillis();

        rocket.setGravity(calculateGravitation(rocket.getBaseMass() + rocket.getFuel(), rocket.getHeight() + Constant.EARTH_RADIUS_M));
        while (running) {
            increment();
        }
        stopTime = System.currentTimeMillis();
        logPerformance();*/
    }

    @Override
    public void increment() {
        if (!stalling()) {

            rocket.setResultingForce(calculateResultingForce(rocket.getForce(), rocket.getGravity()));

            midpointCalculation();
            calculateHeight();
            rocket.setGravity(calculateGravitation(rocket.getFuel() + rocket.getBaseMass(), rocket.getHeight() + Constant.EARTH_RADIUS_M));

            rocket.setFuel(calculateNewWeight(interval, rocket.getFuel(), rocket.getMassLossRate()));
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

    private void midpointCalculation() {
        double k1 = interval / 1000.0 * rocket.getVelocity();
        double k2 = interval / 1000.0 * getNewSpeed(interval / 2.0, rocket.getVelocity() + k1 / 2);
        rocket.setVelocity(rocket.getVelocity() + k2);
    }


    private double getNewSpeed(double deltaTime, double v1) {
        double m2 = calculateGravitation(rocket.getFuel() + rocket.getBaseMass(), rocket.getHeight() + Constant.EARTH_RADIUS_M);
        return (rocket.getForce() - m2) / m2 * deltaTime / 1000.0 + v1;
    }


    private void calculateHeight() {
        rocket.setHeight(getNewHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }

}
