package ch.kbw.rocket.sim.model;


public abstract class Algorithm {
    Rocket rocket;
    int interval;

    double getGravitationalForce(double mass, double r) {
        // g * (m1 * m2 / r*r)
        return Constant.GRAVITATIONAL * (mass * Constant.EARTH_MASS / Math.pow(r, 2));
    }

    double getMass(long deltaTime/*in ms*/, double oldMass, double massLossRate) {
        // m2 = m1 - deltaTime * WeightLoss
        return oldMass - deltaTime / 1000.0 * massLossRate;
    }

    double getResultingForce(double force, double gravitationalForce) {
        // ResultingForce = Force - gravitation
        return force - gravitationalForce;
    }

    double getVelocity(long deltaTime, double mass, double resultingForce) {
        // v2 = deltaTime * (ResultingForce/m1)
        return deltaTime * resultingForce / mass;
    }

    double getHeight(long deltaTime/*in ms*/, double height, double velocity) {
        // h2= h1+ deltaTime * v2
        return height + deltaTime / 1000.0 * velocity;
    }

    public abstract void increment();
}
