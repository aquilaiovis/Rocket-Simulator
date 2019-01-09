package ch.kbw.rocket.sim.model;

public class RK4 extends Algorithm {

    public RK4(Rocket rocket, int interval) {
        super(rocket, interval);
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

    public boolean stalling() {
        return rocket.getFuel() < 0;
    }

    private void calculateFuel() {
      //  rocket.setFuel(getMass(interval, rocket.getFuel(), rocket.getMassLossRate()));

    }

    private void calculateHeight() {
        rocket.setHeight(getHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }

    private void calculateVelocity() {
        //FIXME: not correct
        double seconds = interval / 1000.0;
        double temp = velocity(passedTime, rocket.getVelocity());
        double k1 = seconds * velocity(passedTime, rocket.getVelocity());
        double k2 = seconds * velocity(passedTime + interval / 2, k1 / 2.0);
        double k3 = seconds * velocity(passedTime + interval / 2, k2 / 2.0);
        double k4 = seconds * velocity(passedTime + interval, k3);
        System.out.println();
        System.out.println("k1 = " + k1);
        System.out.println("k2 = " + k2);
        System.out.println("k3 = " + k3);
        System.out.println("k4 = " + k4);
        double v = (k1/ 6.0 + k2/ 3.0 + k3/ 3.0 + k4/ 6.0);
        System.out.println("v = " + v);
        System.out.println("result" + (rocket.getVelocity() + v));
        rocket.setVelocity(v);
    }


    double getVelocity(long deltaTime, double mass, double resultingForce, double oldVelocity) {
        //here delta time is removed to make it more true to the formula
        // v2 =  (ResultingForce/m1)+ v1
        return  resultingForce / mass + oldVelocity;
    }

    private void calculateResultingForce() {
        //rocket.setResultingForce(getResultingForce(rocket.getForce(), rocket.getGravity()));

    }

    private void calculateGravitation() {
        //rocket.setGravity(getGravitationalForce(rocket.getBaseMass() + rocket.getFuel(), rocket.getHeight() + Constant.EARTH_RADIUS_KM));
    }

    private void init() {
        calculateGravitation();
        calculateResultingForce();
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
