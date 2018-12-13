package ch.kbw.rocket.sim.model;

public class Rocket {

    private double mass;          //in kg
    private double massLossRate;  //in kg/s
    private double fuel;            //in kg
    private double height;          //in km
    private double velocity;        //in km/s
    private double acceleration;    // ?
    private double force;           //in N
    private double resultingForce;  //in N

    public double getResultingForce() {
        return resultingForce;
    }

    public void setResultingForce(double resultingForce) {
        this.resultingForce = resultingForce;
    }

    private double gravitation;

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMassLossRate() {
        return massLossRate;
    }

    public void setMassLossRate(double massLossRate) {
        this.massLossRate = massLossRate;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getForce() {
        return force;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public double getGravitation() {
        return gravitation;
    }

    public void setGravitation(double gravitation) {
        this.gravitation = gravitation;
    }
}
