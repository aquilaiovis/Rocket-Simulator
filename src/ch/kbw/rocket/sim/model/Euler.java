package ch.kbw.rocket.sim.model;

public class Euler extends Algorithm {


    @Override
    public void increment() {
        calculateResultingForce();
        calculateGravitation();
        calculateVelocity();
        calculateHeight();
        calculateMass();
    }

    private void calculateGravitation() {
        // needs distance, distance will be the old one
        rocket.setGravitation(getGravitationalForce(rocket.getMass(), rocket.getHeight() + Constant.EARTH_RADIUS_KM));
    }

    private void calculateMass() {
        rocket.setMass(getMass(interval, rocket.getMass(), rocket.getMassLossRate()));
    }

    private void calculateResultingForce() {
        rocket.setResultingForce(getResultingForce(rocket.getForce(), rocket.getGravitation()));
    }


    private void calculateVelocity() {
        rocket.setVelocity(getVelocity(interval, rocket.getMass(), rocket.getResultingForce()));
    }

    private void calculateHeight() {
        rocket.setHeight(getHeight(interval, rocket.getHeight(), rocket.getVelocity()));
    }

}
