package ch.kbw.rocket.sim;

import ch.kbw.rocket.sim.model.Algorithm;
import ch.kbw.rocket.sim.model.Data;
import ch.kbw.rocket.sim.model.Euler;
import ch.kbw.rocket.sim.model.Rocket;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TransferQueue;

public class Controller implements Initializable {

    public LineChart<Number, Number> test1;
    public LineChart<Number, Number> test2;
    public LineChart<Number, Number> test3;
    public LineChart<Number, Number> test4;
    public LineChart<Number, Number> test5;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<Number, Number> test;
    private Algorithm algorithm,algorithm2;
    private Thread calculation;
    private Rocket rocket;
    private XYChart.Series<Number, Number> velocity = new XYChart.Series<>();
    private XYChart.Series<Number, Number> height = new XYChart.Series<>();
    private XYChart.Series<Number, Number> mass = new XYChart.Series<>();
    private XYChart.Series<Number, Number> gravitation = new XYChart.Series<>();
    private XYChart.Series<Number, Number> resultingForce = new XYChart.Series<>();
    private XYChart.Series<Number, Number> joules = new XYChart.Series<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Falcon heavy                               549054 - 507,500           348.0s       507,500   7607000 true
        algorithm = new Euler(new Rocket(549054 - 507500, 348.0, 507500, 7607000, true), 10000);
       // algorithm = new Euler(new Rocket(549054 - 507500, 2228,  507500 , 760700000), 100);

        //algorithm = new Euler(new Rocket(1420788.0 - 488370, 5038, 1020788.0, 22819000), 100);
        //algorithm = new Euler(new Rocket(1420788 - 488370, 640, 488370, 22819000, true), 10);
        initChart(test, velocity, "Velocity", "m/s", "Falcon Heavy");
        initChart(test1, height, "height", "m", "Falcon Heavy");
        initChart(test2, mass, "mass", "kg", "Falcon Heavy");
        initChart(test3, gravitation, "gravitation", "N", "Falcon Heavy");
        initChart(test4, resultingForce, "ResultingForce", "m/s", "Falcon Heavy");
        initChart(test5, joules, "Joules", "J", "Joules");


    }

    public void initChart(LineChart<Number, Number> chart, XYChart.Series<Number, Number> graph, String label, String y, String name) {
        chart.setCreateSymbols(false);
        chart.getData().add(graph);
        chart.getXAxis().setLabel("Time");
        chart.setTitle(label);
        chart.getYAxis().setLabel(y);
        graph.setName(name);
        graph.getData();
        chart.getYAxis().setAnimated(false);
        ((NumberAxis) chart.getXAxis()).setUpperBound(100000);
    }

    public void handleLaunch(ActionEvent actionEvent) {
        rocket = algorithm.getRocket();
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                try {
                    addQueue(rocket.getMassQueue(), mass);
                    addQueue(rocket.getVelocityQueue(), velocity);
                    addQueue(rocket.getHeightQueue(), height);
                    addQueue(rocket.getGravityQueue(), gravitation);
                    addQueue(rocket.getResultingForceQueue(), resultingForce);
                    addQueue(rocket.getJouleForceQueue(), joules);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        calculation = new Thread(algorithm);
        calculation.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        animationTimer.start();
    }


    public void addQueue(TransferQueue<Data> queue, XYChart.Series chart) throws InterruptedException {
        ArrayList<Data> list = new ArrayList<>();
        queue.drainTo(list);
        XYChart.Data[] array = new XYChart.Data[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = new XYChart.Data(list.get(i).getTimestamp() / 1000.0, list.get(i).getValue());
        }
        chart.getData().addAll(array);
    }
}
