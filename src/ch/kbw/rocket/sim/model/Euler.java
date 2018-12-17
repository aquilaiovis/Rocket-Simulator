package ch.kbw.rocket.sim.model;

import javafx.scene.control.Alert;

public class Euler extends Algorithm {

    public Euler(Rocket rocket, int interval) {
        super(rocket, interval);
        passedTime = 0;
    }

    @Override
    public void increment() {
        if (!stalling()) {
            calculateGravitation();
            calculateResultingForce();
            calculateVelocity();
            calculateHeight();
            calculateMass();
            rocket.saveStep(passedTime);
            passedTime += interval;
        }

    }

    private boolean stalling() {
        if (rocket.getMass()<0){
            System.out.println(rocket.getResultingForce());
            return true;
        }
        return false;
    }

    private void calculateGravitation() {
        // needs distance, distance will be the old one
        rocket.setGravity(getGravitationalForce(rocket.getMass(), rocket.getHeight() + Constant.EARTH_RADIUS_KM));
    }

    private void calculateMass() {
        rocket.setMass(getMass(interval, rocket.getMass(), rocket.getMassLossRate()));
    }

    private void calculateResultingForce() {
        rocket.setResultingForce(getResultingForce(rocket.getForce(), rocket.getGravity()));
    }

    private void calculateVelocity() {
        rocket.setVelocity(getVelocity(interval, rocket.getMass(), rocket.getResultingForce()));
    }

    private void calculateHeight() {
        rocket.setHeight(getHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }
}
