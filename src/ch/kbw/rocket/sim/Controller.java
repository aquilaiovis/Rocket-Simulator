package ch.kbw.rocket.sim;

import ch.kbw.rocket.sim.model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TransferQueue;

public class Controller implements Initializable {

    @FXML
    public MenuButton rocketSelection, algorithmSelection;

    @FXML
    private VBox options;

    @FXML
    private Button launchButton, resetButton;

    @FXML
    private NumberAxis xAxis, yAxis;

    @FXML
    private LineChart<Number, Number> mainChart, bottomChart, bottomCenterChart, bottomRightChart,
            centerRightChart, topRightChart;

    // Each rocket has it's own algorithm!
    private String[] algorithmsNames;
    private ArrayList<Algorithm> algorithms;
    private ArrayList<Rocket> selectableRockets;
    private ArrayList<CustomMenuItem> rocketItems, algorithmItems;
    private HashMap<Algorithm, XYChart.Series<Number, Number>[]> algorithmsGraphs;
    private boolean reset;
    private int calculationInterval; // in ms

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeChart(mainChart, "Velocity", "m/s");
        initializeChart(bottomChart, "height", "m");
        initializeChart(bottomCenterChart, "mass", "kg");
        initializeChart(bottomRightChart, "gravitation", "N");
        initializeChart(centerRightChart, "ResultingForce", "m/s");
        initializeChart(topRightChart, "Joules", "J");

        algorithms = new ArrayList<>();
        selectableRockets = new ArrayList<>();
        algorithmsGraphs = new HashMap<>();
        calculationInterval = 1000;

        // Add all default rockets here
        Rocket falconHeavy = new Rocket("Falcon Heavy", 549054 - 507500, 348, 507500, 7607000, true);
        selectableRockets.add(falconHeavy);
        // TODO: Add proper values for Sputnik
        Rocket sputnik = new Rocket("Sputnik", 267000 - 12345, 678, 12345, 3890000, true);
        selectableRockets.add(sputnik);

        updateRocketItemSelection();

        algorithmsNames = new String[]{Euler.class.getSimpleName(), Midpoint.class.getSimpleName(),
                PredictorCorrector.class.getSimpleName()};
        algorithmItems = new ArrayList<>();
        for (String algorithmName : algorithmsNames) {
            CheckBox algorithmCheckBox = new CheckBox(algorithmName);
            CustomMenuItem algorithmItem = new CustomMenuItem(algorithmCheckBox);
            algorithmItem.setText(algorithmName);
            algorithmItem.setHideOnClick(false);
            algorithmItems.add(algorithmItem);
        }
        algorithmSelection.getItems().setAll(algorithmItems);
    }

    private void initializeChart(LineChart<Number, Number> chart, String label, String y) {
        chart.setCreateSymbols(false);

        chart.getXAxis().setLabel("Time");
        chart.getYAxis().setLabel(y);
        chart.setTitle(label);
        chart.getYAxis().setAnimated(false);
        ((NumberAxis) chart.getXAxis()).setUpperBound(100000);
    }

    private void addGraphToChart(LineChart<Number, Number> chart, XYChart.Series<Number, Number> graph, String name) {
        chart.getData().add(graph);
        graph.setName(name);
    }

    public void handleLaunch(ActionEvent event) {
        options.setDisable(true);
        launchButton.setDisable(true);
        launchButton.setVisible(false);
        resetButton.setDisable(false);
        resetButton.setVisible(true);

        ArrayList<Rocket> selectedRockets = new ArrayList<>();
        for (CustomMenuItem rocketItem : rocketItems) {
            if (((CheckBox) rocketItem.getContent()).isSelected()) {
                for (Rocket rocket : selectableRockets) {
                    if (rocket.getName().equals(rocketItem.getText())) {
                        selectedRockets.add(rocket);
                    }
                }
            }
        }
        for (Rocket rocket : selectedRockets) {
            ArrayList<Algorithm> algorithmsForThisRocket = new ArrayList<>();
            for (CustomMenuItem algorithmItem : algorithmItems) {
                if (((CheckBox) algorithmItem.getContent()).isSelected()) {
                    if (algorithmItem.getText().equals(algorithmsNames[0])) {
                        algorithmsForThisRocket.add(new Euler(rocket, calculationInterval));
                    } else if (algorithmItem.getText().equals(algorithmsNames[1])) {
                        algorithmsForThisRocket.add(new Midpoint(rocket, calculationInterval));
                    } else if (algorithmItem.getText().equals(algorithmsNames[2])) {
                        algorithmsForThisRocket.add(new PredictorCorrector(rocket, calculationInterval));
                    }
                }
            }

            for (Algorithm algorithm : algorithmsForThisRocket) {
                XYChart.Series<Number, Number> velocity, height, mass, gravitation, resultingForce, joules;
                velocity = new XYChart.Series<>();
                height = new XYChart.Series<>();
                mass = new XYChart.Series<>();
                gravitation = new XYChart.Series<>();
                resultingForce = new XYChart.Series<>();
                joules = new XYChart.Series<>();

                algorithms.add(algorithm);
                algorithmsGraphs.put(algorithm, new XYChart.Series[]{velocity, height, mass, gravitation,
                        resultingForce, joules});

                String graphName = algorithm.getClass().getSimpleName() + " " + algorithm.getRocket().getName();
                addGraphToChart(mainChart, velocity, graphName);
                addGraphToChart(bottomChart, height, graphName);
                addGraphToChart(bottomCenterChart, mass, graphName);
                addGraphToChart(bottomRightChart, gravitation, graphName);
                addGraphToChart(centerRightChart, resultingForce, graphName);
                addGraphToChart(topRightChart, joules, graphName);
            }
        }

        for (Algorithm algorithm : algorithms) {
            Thread calculation = new Thread(algorithm);
            calculation.setName(algorithm.getClass().getSimpleName() + " " + algorithm.getRocket().getName() + " Calculation Thread");
            calculation.start();
        }

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (reset) {
                    stop();
                }
                for (Algorithm algorithm : algorithms) {
                    Rocket algorithmRocket = algorithm.getRocket();

                    addDataQueue(algorithmRocket.getVelocityQueue(), algorithmsGraphs.get(algorithm)[0], algorithm.stalling());
                    addDataQueue(algorithmRocket.getHeightQueue(), algorithmsGraphs.get(algorithm)[1], algorithm.stalling());
                    addDataQueue(algorithmRocket.getMassQueue(), algorithmsGraphs.get(algorithm)[2], algorithm.stalling());
                    addDataQueue(algorithmRocket.getGravityQueue(), algorithmsGraphs.get(algorithm)[3], algorithm.stalling());
                    addDataQueue(algorithmRocket.getResultingForceQueue(), algorithmsGraphs.get(algorithm)[4], algorithm.stalling());
                    addDataQueue(algorithmRocket.getJouleForceQueue(), algorithmsGraphs.get(algorithm)[5], algorithm.stalling());

                }
            }
        };
        animationTimer.start();
    }

    public void handleReset(ActionEvent event) {
        // TODO: Make Threads end
        reset = true;
        options.setDisable(false);
        launchButton.setDisable(false);
        launchButton.setVisible(true);
        resetButton.setDisable(true);
        resetButton.setVisible(false);
    }

    public void handleRocketCreation(ActionEvent event) {
        // Create the custom dialog.
        Dialog<ArrayList<String>> dialog = new Dialog<>();
        dialog.setTitle("Custom Rocket Creator");
        dialog.setHeaderText("Erstelle deine eigene Rakete!");

        // Set the icon (must be included in the project).
        // dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField weight = new TextField();
        name.setPromptText("Gewicht in kg");
        TextField weightLossRate = new TextField();
        name.setPromptText("Gewichtsverlustrate in kg/s");
        TextField fuel = new TextField();
        name.setPromptText("Treibstoff in kg");
        TextField thrust = new TextField();
        name.setPromptText("Schubkraft in N");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Gewicht in kg (ohne Treibstoff):"), 0, 1);
        grid.add(weight, 1, 1);
        grid.add(new Label("Gewichtsverlustrate in kg/s:"), 0, 2);
        grid.add(weightLossRate, 1, 2);
        grid.add(new Label("Treibstoff in kg:"), 0, 3);
        grid.add(fuel, 1, 3);
        grid.add(new Label("Schubkraft in N:"), 0, 4);
        grid.add(thrust, 1, 4);

        // Enable/Disable login button depending on whether a username was entered.
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> name.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                ArrayList<String> inputs = new ArrayList<>();
                inputs.add(name.getText());
                inputs.add(weight.getText());
                inputs.add(weightLossRate.getText());
                inputs.add(fuel.getText());
                inputs.add(thrust.getText());
                return inputs;
            }
            return null;
        });

        Optional<ArrayList<String>> result = dialog.showAndWait();

        result.ifPresent(content -> {
            // TODO: Add regex check if number
            selectableRockets.add(new Rocket(result.get().get(0),
                    Double.parseDouble(result.get().get(1)),
                    Double.parseDouble(result.get().get(2)),
                    Double.parseDouble(result.get().get(3)),
                    Double.parseDouble(result.get().get(4))));
        });

        updateRocketItemSelection();
    }

    private void updateRocketItemSelection() {
        rocketItems = new ArrayList<>();
        for (Rocket rocket : selectableRockets) {
            CheckBox rocketCheckBox = new CheckBox(rocket.getName());
            CustomMenuItem rocketItem = new CustomMenuItem(rocketCheckBox);
            rocketItem.setText(rocket.getName());
            rocketItem.setHideOnClick(false);
            rocketItems.add(rocketItem);
        }
        rocketSelection.getItems().setAll(rocketItems);
    }

    private void addDataQueue(TransferQueue<Data> dataQueue, XYChart.Series chart, boolean stalling) {
           /*
                ArrayList<Data> list = new ArrayList<>();
                dataQueue.drainTo(list);
                XYChart.Data[] array = new XYChart.Data[list.size()];
                System.out.println("asdsadd");
                for (int i = 0; i < list.size(); i++) {
                    array[i] = new XYChart.Data(list.get(i).getTimestamp() / 1000.0, list.get(i).getValue());
                    System.out.println("copying....");
                }
                System.out.println("painting....");
                chart.getData().addAll(array);
                System.out.println("finished....");
            }else {*/
        if (!dataQueue.isEmpty()) {
            Data data = dataQueue.poll();
            chart.getData().add(new XYChart.Data(data.getTimestamp() / 1000.0, data.getValue()));
        }
    }
}