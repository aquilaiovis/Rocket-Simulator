package ch.kbw.rocket.sim;

import ch.kbw.rocket.sim.model.Constant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.awt.font.NumericShaper;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<Number,Number > test;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleLaunch(ActionEvent actionEvent) {
        xAxis.setLabel("Number of Month");
        //creating the chart
       // test = new LineChart<Number, Number>(xAxis,yAxis);

        test.setTitle("This is");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Data");
        //populating the series with data
        series.getData();
        for (int i = 0; i < 12; i++) {
            series.getData().add(new XYChart.Data(i, Math.random()*100));
        }




        test.getData().add(series);
    }
}
