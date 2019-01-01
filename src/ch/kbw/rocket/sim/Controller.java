package ch.kbw.rocket.sim;

import ch.kbw.rocket.sim.model.Algorithm;
import ch.kbw.rocket.sim.model.Euler;
import ch.kbw.rocket.sim.model.Rocket;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

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
    private Algorithm algorithm;
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

        //Falcon heavy
        //algorithm = new Euler(new Rocket(1420788.0, 5038, 1020788.0, 22819000), 1000);
        algorithm = new Euler(new Rocket(1420788 - 488370, 640, 488370, 22819000, true), 10);
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
                addData(rocket.getVelocityHistory(), velocity);
                addData(rocket.getHeightHistory(), height);
                addData(rocket.getMassHistory(), mass);
                addData(rocket.getGravityHistory(), gravitation);
                addData(rocket.getResultingForceHistory(), resultingForce);
                addData(rocket.getVelocityHistory(), velocity);
                addData(rocket.getJouleHistory(), joules);
            }
        };
        animationTimer.start();

        calculation = new Thread(algorithm);
        calculation.start();
    }

    public void addData(HashMap<Long, Double> map, XYChart.Series chart) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            chart.getData().add(new XYChart.Data(pair.getKey(), pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }

    }
}