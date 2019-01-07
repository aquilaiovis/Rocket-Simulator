package ch.kbw.rocket.sim.model;

import java.util.concurrent.PriorityBlockingQueue;

public class Rocket {

    private double baseMass;        //in kg
    private double massLossRate;    //in kg/s
    private double fuel;            //in kg
    private double height;          //in m
    private double velocity;        //in m/s
    private double acceleration;    // ?
    private double force;           //in N
    private double resultingForce;  //in N
    private double gravity;         //in N
    //TODO: Convert to Lists
    private PriorityBlockingQueue<Data> massQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Data> heightQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Data> velocityQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Data> resultingForceQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Data> gravityQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Data> jouleForceQueue = new PriorityBlockingQueue<>();



    public Rocket(double baseMass, double massLossRate, double fuel, double force) {
        this.baseMass = baseMass;
        this.massLossRate = massLossRate;
        this.fuel = fuel;
        this.force = force;
        height = 0;
        acceleration = 0;
        resultingForce = 0;
        gravity = 0;
        velocity = 0;
    }

    public Rocket(double baseMass, double ISP, double fuel, double force, boolean usesISP) {
        this.baseMass = baseMass;
        this.massLossRate = force / (Constant.GRAVITATIONAL_ACCELERATION * ISP);
        System.out.println(massLossRate);
        this.fuel = fuel;
        this.force = force;
        height = 0;
        acceleration = 0;
        resultingForce = 0;
        gravity = 0;
        velocity = 0;
    }


    public Rocket(Rocket rocket) {
        this.baseMass = rocket.getBaseMass();
        this.massLossRate = rocket.getMassLossRate();
        this.fuel = rocket.getFuel();
        this.height = rocket.getHeight();
        this.velocity = rocket.getVelocity();
        this.acceleration = rocket.getAcceleration();
        this.force = rocket.getForce();
        this.resultingForce = rocket.getResultingForce();
        this.gravity = rocket.getGravity();
    }

    public void saveStep(long passedTime) {
        massQueue.add(new Data(baseMass + fuel, passedTime));
        heightQueue.add(new Data(height, passedTime));
        velocityQueue.add(new Data(velocity, passedTime));
        resultingForceQueue.add(new Data(resultingForce, passedTime));
        gravityQueue.add(new Data(gravity, passedTime));
        jouleForceQueue.add(new Data(force * height, passedTime));
    }

    @Override
    public String toString() {

        return "\nRocket data:" +
                "\n Basemass: " + baseMass +
                "\n Fuel: " + fuel +
                "\n Masslossrate: " + massLossRate +
                "\n Force: " + force +
                "\n Height: " + height + "\n";
    }

    public double getResultingForce() {
        return resultingForce;
    }

    public void setResultingForce(double resultingForce) {
        this.resultingForce = resultingForce;
    }

    public double getBaseMass() {
        return baseMass;
    }

    public void setBaseMass(double baseMass) {
        this.baseMass = baseMass;
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

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public PriorityBlockingQueue<Data> getMassQueue() {
        return massQueue;
    }

    public PriorityBlockingQueue<Data> getHeightQueue() {
        return heightQueue;
    }

    public PriorityBlockingQueue<Data> getVelocityQueue() {
        return velocityQueue;
    }

    public PriorityBlockingQueue<Data> getResultingForceQueue() {
        return resultingForceQueue;
    }

    public PriorityBlockingQueue<Data> getGravityQueue() {
        return gravityQueue;
    }

    public PriorityBlockingQueue<Data> getJouleForceQueue() {
        return jouleForceQueue;
    }
}
