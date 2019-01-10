package ch.kbw.rocket.sim.model;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class Rocket {

    private String name;
    private double baseMass;        //in kg
    private double massLossRate;    //in kg/s
    private double fuel;            //in kg
    private double height;          //in m
    private double velocity;        //in m/s
    private double acceleration;    //in m/s*s
    private double force;           //in N
    private double resultingForce;  //in N
    private double gravity;         //in N

    private TransferQueue<Data> massQueue = new LinkedTransferQueue<>();
    private TransferQueue<Data> heightQueue = new LinkedTransferQueue<>();
    private TransferQueue<Data> velocityQueue = new LinkedTransferQueue<>();
    private TransferQueue<Data> resultingForceQueue = new LinkedTransferQueue<>();
    private TransferQueue<Data> gravityQueue = new LinkedTransferQueue<>();
    private TransferQueue<Data> jouleForceQueue = new LinkedTransferQueue<>();


    public Rocket(String name, double baseMass, double massLossRate, double fuel, double force) {
        this.name = name;
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

    public Rocket(String name, double baseMass, double ISP, double fuel, double force, boolean usesISP) {
        this.name = name;
        this.baseMass = baseMass;
        this.massLossRate = force / Constant.GRAVITATIONAL_ACCELERATION / ISP;
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
        this.name = rocket.getName();
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
        try {
            massQueue.put(new Data(baseMass + fuel, passedTime));
            heightQueue.put(new Data(height, passedTime));
            velocityQueue.put(new Data(velocity, passedTime));
            resultingForceQueue.put(new Data(resultingForce, passedTime));
            gravityQueue.put(new Data(gravity, passedTime));
            jouleForceQueue.put(new Data(force * height, passedTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public double getFullWeigth() {
        return baseMass + fuel;
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

    public TransferQueue<Data> getMassQueue() {
        return massQueue;
    }

    public TransferQueue<Data> getHeightQueue() {
        return heightQueue;
    }

    public TransferQueue<Data> getVelocityQueue() {
        return velocityQueue;
    }

    public TransferQueue<Data> getResultingForceQueue() {
        return resultingForceQueue;
    }

    public TransferQueue<Data> getGravityQueue() {
        return gravityQueue;
    }

    public TransferQueue<Data> getJouleForceQueue() {
        return jouleForceQueue;
    }

    public String getName()
    {
        return name;
    }
}