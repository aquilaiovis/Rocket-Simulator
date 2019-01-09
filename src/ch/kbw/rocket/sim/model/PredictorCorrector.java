package ch.kbw.rocket.sim.model;

public class PredictorCorrector extends Algorithm {

    public PredictorCorrector(Rocket rocket, int interval) {
        super(rocket, interval);
        passedTime = 0;

    }

    @Override
    public void increment() {
        if (!stalling()) {
            calculateGravitation();
            calculateResultingForce();
            calculateAverageVelocity();
            calculateHeight();
            calculateFuel();
            rocket.saveStep(passedTime);
            passedTime += interval;

        } else {
            running = false;
        }
    }

    public boolean stalling() {
        return rocket.getFuel() < 0;
    }

    private void calculateFuel() {
     //   rocket.setFuel(getMass(interval, rocket.getFuel(), rocket.getMassLossRate()));

    }

    private void calculateHeight() {
        rocket.setHeight(getHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }

    private void calculateAverageVelocity() {
        System.out.println(rocket.getVelocity());
        double k1 = velocity(interval,  rocket.getVelocity());
        double averageSpeed = ( rocket.getVelocity()) / 2.0;
        rocket.setVelocity(averageSpeed);

    }

    double getVelocity(long deltaTime, double mass, double resultingForce, double oldVelocity) {
        //here delta time is removed to make it more true to the formula
        // v2 =  (ResultingForce/m1)+ v1
        return  resultingForce / mass + oldVelocity;
    }

    private void calculateResultingForce() {
     //   rocket.setResultingForce(getResultingForce(rocket.getForce(), rocket.getGravity()));

    }

    private void calculateGravitation() {
     //   rocket.setGravity(getGravitationalForce(rocket.getBaseMass() + rocket.getFuel(), rocket.getHeight() + Constant.EARTH_RADIUS_KM));
    }

    private void init() {
        calculateGravitation();
        calculateResultingForce();
        System.out.println();
        System.out.println("Speed: "+rocket.getVelocity());
        rocket.setVelocity(getVelocity(interval, rocket.getBaseMass() + rocket.getFuel(), rocket.getResultingForce(), rocket.getVelocity()));
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
}
