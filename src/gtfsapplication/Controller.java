/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: SE 2030 011 Team C
 * Created: 10/11/2020
 * MIT License
 *
 * Copyright (c) 2020 Team C (Kenneth McDonough, William Lauer, Thy Lee, Luke Miller)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package gtfsapplication;

import gtfsapplication.data.GTFS;
import gtfsapplication.data.Route;
import gtfsapplication.data.Stop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the user interface
 *
 * @author Kenneth McDonough, Luke Miller
 * @version 10/6/2020
 */
public class Controller {

    private GTFS gtfs;
    private Stage stage = new Stage();

    @FXML
    private ListView listView;

    @FXML
    private MapView mapView;

    @FXML
    private VBox vbox, vbox1;

    @FXML
    private BorderPane borderPane;

    private Stage secStage;
    private SecondController secondController;

    /**
     * Sets up the controller of the second window
     * @param secondController the controller of the second window
     */
    public void setSecondController(SecondController secondController){
        this.secondController = secondController;
    }

    /**
     * Sets up the second window
     * @param secStage the stage of the second window
     */
    public void setSecStage(Stage secStage){
        this.secStage = secStage;
    }

    /**
     * Shows the second window
     * @param instance the instance to start updating
     * @param id the Id of the instance
     */
    public void showSecondStage(String instance, String id) {
        if(secStage.isShowing()){
            secStage.hide();
        } else{
            secStage.setX(borderPane.getLayoutX() + borderPane.getWidth());
            secStage.setY(borderPane.getLayoutY());
            secStage.show();
        }
        secondController.handle(gtfs, instance, id);
    }

    /**
     * Hides the second window
     */
    public void hideSecondStage(){
        gtfs.updateObservers();
        secStage.hide();
    }

    /**
     * Called when the UI is initialized.
     */
    @FXML
    public void initialize() {
        gtfs = new GTFS();

        listView = new ListView(gtfs);
        VBox.setVgrow(listView, Priority.ALWAYS);
        vbox.getChildren().add(listView);

        mapView = new MapView(gtfs);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        HBox.setHgrow(mapView, Priority.ALWAYS);
        vbox1.getChildren().add(mapView);

        gtfs.addObserver(listView);
        gtfs.addObserver(mapView);
    }

    @FXML
    private void handleLoadFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select one or more gtfs .txt files " +
                "to add to GTFS Data Structure");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                    "GTFS Files",
                    "routes.txt",
                    "stops.txt",
                    "stop_times.txt",
                    "trips.txt"
                )
        );
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if (files == null || files.size() < 1) {
            // User cancelled import
            return;
        }
        for (File file : files) {
            try {
                gtfs.importFile(file);
            } catch (GTFSException ex) {
                showErrorMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleExport(ActionEvent event) {
        File directory = showDirectoryChooser("Select the folder to save GTFS files to");
        if (directory != null) {
            try {
                gtfs.export(directory);
            } catch (GTFSException ex) {
                showErrorMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleExportStops(ActionEvent event) {
        if (!gtfs.hasStops()) {
            showErrorMessage("There are no stops to export.");
            return;
        }
        File directory = showDirectoryChooser("Select the folder to save stops.txt to");
        if (directory != null) {
            try {
                gtfs.exportStops(
                        new File(directory.getAbsolutePath() + File.separator + "stops.txt")
                );
            } catch (GTFSException ex) {
                showErrorMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleExportStopTimes(ActionEvent event) {
        if (!gtfs.hasStopTimes()) {
            showErrorMessage("There are no stop times to export.");
            return;
        }
        File directory = showDirectoryChooser("Select the folder to save stop_times.txt to");
        if (directory != null) {
            try {
                gtfs.exportStopTimes(
                        new File(directory.getAbsolutePath() + File.separator + "stop_times.txt")
                );
            } catch (GTFSException ex) {
                showErrorMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleExportRoutes(ActionEvent event) {
        if (!gtfs.hasRoutes()) {
            showErrorMessage("There are no routes to export.");
            return;
        }
        File directory = showDirectoryChooser("Select the folder to save routes.txt to");
        if (directory != null) {
            try {
                gtfs.exportRoutes(
                        new File(directory.getAbsolutePath() + File.separator + "routes.txt")
                );
            } catch (GTFSException ex) {
                showErrorMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleExportTrips(ActionEvent event) {
        if (!gtfs.hasTrips()) {
            showErrorMessage("There are no trips to export.");
            return;
        }
        File directory = showDirectoryChooser("Select the folder to save trips.txt to");
        if (directory != null) {
            try {
                gtfs.exportTrips(
                        new File(directory.getAbsolutePath() + File.separator + "trips.txt")
                );
            } catch (GTFSException ex) {
                showErrorMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void searchStop(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search for a stop by ID");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Stop stop = gtfs.getStop(result.get());
            if (stop == null) {
                showErrorMessage("No stop exists with the id: " + result.get());
            } else {
                gtfs.searchByStop(stop);
            }
        }
    }

    @FXML
    private void searchRoute(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search for a route by ID");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Route route = gtfs.getRoute(result.get());
            if (route == null) {
                showErrorMessage("No route exists with the id: " + result.get());
            } else {
                gtfs.searchByRoute(route);
            }
        }
    }

    @FXML
    private void updateAttributes(ActionEvent event) {
        String[] instances = new String[] {"Route", "Stop", "Stop Time", "Trip"};
        List<String> choices = Arrays.asList(instances);
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Route", choices);
        choiceDialog.setTitle("Update an attributes");
        choiceDialog.setHeaderText("Select an instance to update");
        choiceDialog.setContentText("Update on:");
        Optional<String> result = choiceDialog.showAndWait();
        if(result.isPresent()) {
            String selected = result.get();
            String id = getInput(selected);
            showSecondStage(selected, id);
        }
    }

    /**
     * Get user's input for updating the attributes
     * @param instance the instance that the user chose to update
     * @return the id of the instance
     */
    private String getInput(String instance) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Choose a " + instance.toLowerCase() + " to update");
        if(instance.equalsIgnoreCase("Stop Time")){
            dialog.setHeaderText("Enter stop_id and trip_id to update stop time: ");
            dialog.setContentText("stop_id, trip_id: ");
        } else{
            dialog.setHeaderText("Enter the " + instance.toLowerCase() + "_id");
            dialog.setContentText(instance + "_id: ");
        }
        String id = "";
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            id += result.get();
        }
        return id;
    }

    private void showErrorMessage(String message) {
        new Alert(
                Alert.AlertType.ERROR,
                message
        ).showAndWait();
    }

    private File showDirectoryChooser(String title) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        return chooser.showDialog(null);
    }
}
