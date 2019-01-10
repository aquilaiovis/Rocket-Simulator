package ch.kbw.rocket.sim;

import ch.kbw.rocket.sim.model.Algorithm;
import ch.kbw.rocket.sim.model.Data;
import ch.kbw.rocket.sim.model.Euler;
import ch.kbw.rocket.sim.model.Rocket;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TransferQueue;

public class Controller implements Initializable {

    // TODO: At end of algorithm, change Launch button to Reset

    @FXML
    public MenuButton rocketSelection, algorithmSelection;

    @FXML
    private VBox options;

    @FXML
    private Button launchButton, resetButton;

    @FXML
    private NumberAxis xAxis, yAxis;

    @FXML
    private LineChart<Number, Number>   mainGraph, bottomLeftGraph, bottomCenterGraph, bottomRightGraph,
                                        centerRightGraph, topRightGraph;

    private ArrayList<Algorithm> algorithms;
    private ArrayList<Rocket> selectableRockets;
    private ArrayList<CustomMenuItem> rocketItems, algorithmItems;
    private XYChart.Series<Number, Number> velocity = new XYChart.Series<>();
    private XYChart.Series<Number, Number> height = new XYChart.Series<>();
    private XYChart.Series<Number, Number> mass = new XYChart.Series<>();
    private XYChart.Series<Number, Number> gravitation = new XYChart.Series<>();
    private XYChart.Series<Number, Number> resultingForce = new XYChart.Series<>();
    private XYChart.Series<Number, Number> joules = new XYChart.Series<>();
    private boolean reset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectableRockets = new ArrayList<>();
        algorithms = new ArrayList<>();

        // Add default rockets here
        Rocket falconHeavy = new Rocket("Falcon Heavy", 1420788 - 488370, 5038, 1020788, 22819000);
        selectableRockets.add(falconHeavy);

        initializeGraph(mainGraph, velocity, "Velocity", "m/s", "Falcon Heavy");
        initializeGraph(bottomLeftGraph, height, "height", "m", "Falcon Heavy");
        initializeGraph(bottomCenterGraph, mass, "mass", "kg", "Falcon Heavy");
        initializeGraph(bottomRightGraph, gravitation, "gravitation", "N", "Falcon Heavy");
        initializeGraph(centerRightGraph, resultingForce, "ResultingForce", "m/s", "Falcon Heavy");
        initializeGraph(topRightGraph, joules, "Joules", "J", "Joules");

        updateRocketItemsSelection();

        algorithmItems = new ArrayList<>();
        for(Algorithm algorithm : algorithms)
        {
            CheckBox algorithmCheckBox = new CheckBox(algorithm.getClass().getSimpleName());
            CustomMenuItem algorithmItem = new CustomMenuItem(algorithmCheckBox);
            algorithmItem.setHideOnClick(false);
            algorithmItems.add(algorithmItem);
        }
        rocketSelection.getItems().setAll(algorithmItems);
    }

    private void initializeGraph(LineChart<Number, Number> chart, XYChart.Series<Number, Number> graph, String label,
                                 String y, String name) {
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

    public void handleLaunch(ActionEvent event) {
        options.setDisable(true);
        launchButton.setDisable(true);
        launchButton.setVisible(false);
        resetButton.setDisable(false);
        resetButton.setVisible(true);
        ArrayList<Rocket> selectedRockets = new ArrayList<>();
        for(CustomMenuItem rocketItem : rocketItems)
        {
            if(((CheckBox)rocketItem.getContent()).isSelected())
            {
                System.out.println("123");
                for(Rocket rocket : selectableRockets)
                {
                    System.out.println("abc");
                    System.out.println(rocket.getName() + " " + rocketItem.getText());
                    if(rocket.getName().equals(rocketItem.getText()))
                    {
                        selectedRockets.add(rocket);
                        System.out.println("Rocket Added");
                    }
                }
            }
        }
        for(Rocket rocket : selectedRockets)
        {
            // Add all algorithms here
            // TODO: If algorithm selected
            algorithms.add(new Euler(rocket, 10));
            //algorithms.add(new Midpoint(rocket, 10));
            //algorithms.add(new RK4(rocket, 10));
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if(reset)
                    {
                        stop();
                    }
                    try
                    {
                        addQueue(rocket.getMassQueue(), mass);
                        addQueue(rocket.getVelocityQueue(), velocity);
                        addQueue(rocket.getHeightQueue(), height);
                        addQueue(rocket.getGravityQueue(), gravitation);
                        addQueue(rocket.getResultingForceQueue(), resultingForce);
                        addQueue(rocket.getJouleForceQueue(), joules);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            for(Algorithm algorithm : algorithms)
            {
                Thread calculation = new Thread(algorithm);
                calculation.setName(algorithm.getClass().getSimpleName() + " " + algorithm.getRocket().getName() + " Calculation Thread");
                calculation.start();
            }

            animationTimer.start();
        }
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

    public void handleRocketCreation(ActionEvent event)
    {
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

        updateRocketItemsSelection();
    }

    private void updateRocketItemsSelection()
    {
        rocketItems = new ArrayList<>();
        for(Rocket rocket : selectableRockets)
        {
            CheckBox rocketCheckBox = new CheckBox(rocket.getName());
            CustomMenuItem rocketItem = new CustomMenuItem(rocketCheckBox);
            rocketItem.setText(rocket.getName());
            rocketItem.setHideOnClick(false);
            rocketItems.add(rocketItem);
        }
        rocketSelection.getItems().setAll(rocketItems);
    }

    private void addQueue(TransferQueue<Data> queue, XYChart.Series chart) throws InterruptedException {
        ArrayList<Data> list = new ArrayList<>();
        queue.drainTo(list);
        XYChart.Data[] array = new XYChart.Data[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = new XYChart.Data(list.get(i).getTimestamp() / 1000.0, list.get(i).getValue());
        }
        chart.getData().addAll(array);
    }
}