/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: Thy Lee, Luke Miller, William Lauer
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

import gtfsapplication.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Controller for updating attributes interface
 * @author Thy Le
 * @version 11/7/2020
 */
public class SecondController {
    /**
     * The main window
     */
    private Stage main;

    /**
     * The controller of the main window
     */
    private Controller mainController;

    @FXML private Label one;
    @FXML private Label two;
    @FXML private Label three;
    @FXML private Label four;
    @FXML private Label five;
    @FXML private Label six;
    @FXML private Label seven;
    @FXML private Label eight;
    @FXML private Label nine;

    @FXML private TextField fieldTwo;
    @FXML private TextField fieldThree;
    @FXML private TextField fieldFour;
    @FXML private TextField fieldFive;
    @FXML private TextField fieldSix;
    @FXML private TextField fieldSeven;
    @FXML private TextField fieldEight;
    @FXML private TextField fieldNine;

    @FXML private Button updateButton;

    private Route route;
    private Trip trip;
    private Stop stop;
    private StopTime stopTime;
    private String instance;


    /**
     * Link the second window with the main stage
     * @author Thy Le
     * @param main the main stage
     */
    public void setMainStage(Stage main){
        this.main = main;
    }

    /**
     * Link the second controller with the main controller
     * @author Thy Le
     * @param mainController the controller of the main window
     */
    public void setMainController(Controller mainController){
        this.mainController = mainController;
    }

    /**
     * Generic method to handle label and text fields for each instance
     * @author Thy Le
     * @param gtfs the data structure
     * @param instance the instance that user choose to update
     * @param id the id of the instance
     */
    public void handle(GTFS gtfs, String instance, String id) {
        this.instance = instance;
        if(instance.equalsIgnoreCase("Route")){
            route = gtfs.getRoute(id);
            handleRoute(route);
        } else if(instance.equalsIgnoreCase("Trip")){
            trip = gtfs.getTrip(id);
            handleTrip(trip);
        } else if(instance.equalsIgnoreCase("Stop")){
            stop = gtfs.getStop(id);
            handleStop(stop);
        } else if(instance.equalsIgnoreCase("Stop Time")){
            String stopId = id.split("\\s*,\\s*")[0];
            String tripId = id.split("\\s*,\\s*")[1];
            stopTime = gtfs.getStopTime(stopId, tripId);
            handleStopTime(stopTime);
        }
    }

    /**
     * Handle the event of clicking the update button
     * @author Thy Le
     */
    @FXML
    private void handleButton() {
        if(instance.equalsIgnoreCase("Route")){
            updateRoute();
        } else if(instance.equalsIgnoreCase("Stop")){
            updateStop();
        } else if(instance.equalsIgnoreCase("Stop Time")){
            updateStopTime();
        } else if(instance.equalsIgnoreCase("Trip")){
            updateTrip();
        }
        clearField();
        mainController.hideSecondStage();
    }

    /**
     * Set the appropriate label and text fields for the route instance
     * @author Thy Le
     * @param route the route instance
     */
    private void handleRoute(Route route){
        if(route != null){
            one.setText("route_id " + route.getId());
            two.setText("agency_id from " + route.getAgencyId() + " to:");
            three.setText("route_short_name from " + route.getShortName() + " to:");
            four.setText("route_long_name from " + route.getLongName() + " to:");
            five.setText("route_desc from " + route.getDescription() + " to:");
            six.setText("route_type from " + route.getType() + " to:");
            seven.setText("route_url from " + route.getUrl() + " to:");
            eight.setText("route_color from " + route.getColor() + " to:");
            nine.setText("route_text_color from " + route.getTextColor() + " to:");
        } else{
            failAlert("Unable to find the matching route" +
                    "\nFail to update route attributes");
        }
    }

    /**
     * Update a specific route that the user chose to update
     * @author Thy Le
     */
    private void updateRoute() {
        try{
            if(!fieldTwo.getText().isEmpty()){
                route.setAgencyId(fieldTwo.getText());
            }
            if(!fieldThree.getText().isEmpty()){
                route.setShortName(fieldThree.getText());
            }
            if(!fieldFour.getText().isEmpty()){
                route.setLongName(fieldFour.getText());
            }
            if(!fieldFive.getText().isEmpty()){
                route.setDescription(fieldFive.getText());
            }
            if(!fieldSix.getText().isEmpty()){
                route.setType(Integer.parseInt(fieldSix.getText()));
            }
            if(!fieldSeven.getText().isEmpty()){
                route.setUrl(new URL(fieldSeven.getText()));
            }
            if(!fieldEight.getText().isEmpty()){
                route.setColor(Color.web("#" + fieldEight.getText()));
            }
            if(!fieldNine.getText().isEmpty()){
                route.setTextColor(Color.web("#" + fieldNine.getText()));
            }
            successAlert();
        } catch (NumberFormatException ex) {
            failAlert(six.getText() + " needs to be a valid integer. "
                    + "\nGot: " + fieldSix.getText()
                    + "\nFail to update " + instance + " attributes");
        } catch (MalformedURLException ex) {
            failAlert(seven.getText() + " needs to be a valid url or not provided. "
                    + "\nGot: " + fieldSeven.getText()
                    + "\nFail to update " + instance + " attributes");
        } catch (IllegalArgumentException ex) {
            failAlert(eight.getText()+ " needs to be a valid color. "
                    + "\nGot: " + fieldEight.getText()
                    + "\nFail to update " + instance + " attributes");
        }

    }

    /**
     * Set the appropriate labels and text fields for trip instance
     * @author Thy Le
     * @param trip the trip instance to update
     */
    private void handleTrip(Trip trip){
        if(trip != null){
            one.setText("route_id " + trip.getRouteId());
            two.setText("service_id from: " + trip.getServiceId() + " to:");
            three.setText("trip_id from " + trip.getId() + " to:");
            four.setText("trip_headsign from " + trip.getHeadSign() + " to:");
            five.setText("direction_id from " + trip.getDirectionId() + " to:");
            six.setText("block_id from " + trip.getBlockID() + " to:");
            seven.setText("shape_id from " + trip.getShapeId() + " to:");

            updateButton.setTranslateY(-80);
            eight.setVisible(false);
            fieldEight.setVisible(false);
            nine.setVisible(false);
            fieldNine.setVisible(false);
        } else{
            failAlert("Unable to find the matching trip"
                    + "\nFail to update trip attributes");
        }
    }

    /**
     * Update a specific Trip that the user chose to update
     * @author Thy Le
     */
    private void updateTrip(){
        try{
            if(!fieldTwo.getText().isEmpty()){
                trip.setServiceId(fieldTwo.getText());
            }
            if(!fieldThree.getText().isEmpty()){
                trip.setId(fieldThree.getText());
            }
            if(!fieldFour.getText().isEmpty()){
                trip.setHeadSign(fieldFour.getText());
            }
            if(!fieldFive.getText().isEmpty()){
                trip.setDirectionId(Integer.parseInt(fieldFive.getText()));
            }
            if(!fieldSix.getText().isEmpty()){
                trip.setBlockID(fieldSix.getText());
            }
            if(!fieldSeven.getText().isEmpty()){
                trip.setShapeId(fieldSeven.getText());
            }
            successAlert();
        } catch (NumberFormatException ex) {
            failAlert("direction_id needs to be an integer. "
                    + "\nGot: " + fieldFive.getText()
                    + "\nFail to update " + instance + " attributes");
        }


    }

    /**
     * Set the appropriate labels and text fields for stop instance
     * @author Thy Le
     * @param stop the stop instance to update
     */
    private void handleStop(Stop stop){
        if(stop != null){
            one.setText("stop_id " + stop.getId());
            two.setText("stop_name from " + stop.getName() + " to:");
            three.setText("stop_desc from " + stop.getDescription() + " to:");
            four.setText("stop_lat from " + stop.getLatitude() + " to:");
            five.setText("stop_lon from " + stop.getLongitude() + " to:");

            updateButton.setTranslateY(-160);
            six.setVisible(false);
            fieldSix.setVisible(false);
            seven.setVisible(false);
            fieldSeven.setVisible(false);
            eight.setVisible(false);
            fieldEight.setVisible(false);
            nine.setVisible(false);
            fieldNine.setVisible(false);
        } else {
            failAlert("Unable to find the matching stop"
                    + "\nFail to update stop attributes");
        }

    }

    /**
     * Update a specific stop that the user chose to update
     * @author Thy Le
     */
    private void updateStop() {
        try{
            if(!fieldTwo.getText().isEmpty()){
                stop.setName(fieldTwo.getText());
            } else {
                stop.setName(stop.getName());
            }
            if(!fieldThree.getText().isEmpty()){
                stop.setDescription(fieldThree.getText());
            } else{
                stop.setDescription(stop.getDescription());
            }
            if(!fieldFour.getText().isEmpty()){
                stop.setLatitude(Double.parseDouble(fieldFour.getText()));
            }
            if(!fieldFive.getText().isEmpty()){
                stop.setLongitude(Double.parseDouble(fieldFour.getText()));
            }
            successAlert();
        } catch (NumberFormatException ex) {
            failAlert("stop_lat and stop_lon needs to be a valid double. "
                    + "\nGot: stop_lat: " + fieldFour.getText()
                    + " , stop_lon: " + fieldFive.getText()
                    + "\nFail to update " + instance + " attributes");
        }

    }

    /**
     * Set the appropriate labels and text fields for the stop time instance
     * @author Thy Le
     * @param stopTime the stop time instance to update
     */
    private void handleStopTime(StopTime stopTime){
        if(stopTime != null){
            one.setText("trip_id " + stopTime.getTripId());
            two.setText("arrival_time from " + stopTime.getArrivalTime() + " to:");
            fieldTwo.setPromptText("HH:mm:ss");
            three.setText("departure_time from " + stopTime.getDepartureTime() + " to:");
            fieldThree.setPromptText("HH:mm:ss");
            four.setText("stop_id from " + stopTime.getStopId() + " to:");
            five.setText("stop_sequence from " + stopTime.getStopSequence() + " to:");
            six.setText("stop_headsign from " + stopTime.getStopHeadsign() + " to:");
            seven.setText("pickup_type from " + stopTime.getPickupType() + " to:");
            eight.setText("drop_off_type from " + stopTime.getDropOffType() + " to:");
            updateButton.setTranslateY(-40);
            nine.setVisible(false);
            fieldNine.setVisible(false);
        } else{
            failAlert("Unable to find the matching route\n"
                    + "Fail to update stop time attributes");
        }
    }

    /**
     * Update a specific stop time that the user chose to update
     * @author Thy Le
     */
    private void updateStopTime() {
        try{
            if(!fieldTwo.getText().isEmpty()){
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                time.parse(fieldTwo.getText());
                stopTime.setArrivalTime(fieldTwo.getText());
            }
            if(!fieldThree.getText().isEmpty()){
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                time.parse(fieldThree.getText());
                stopTime.setDepartureTime(fieldThree.getText());
            }
            if(!fieldFour.getText().isEmpty()){
                stopTime.setStopId(fieldFour.getText());
            }
            if(!fieldFive.getText().isEmpty()){
                stopTime.setStopSequence(Integer.parseInt(fieldFive.getText()));
            }
            if(!fieldSix.getText().isEmpty()){
                stopTime.setStopHeadsign(fieldSix.getText());
            }
            if(!fieldSeven.getText().isEmpty()){
                stopTime.setPickupType(Integer.parseInt(fieldSeven.getText()));
            }
            if(!fieldEight.getText().isEmpty()){
                stopTime.setDropOffType(Integer.parseInt(fieldEight.getText()));
            }
            successAlert();
        } catch (NumberFormatException ex) {
            failAlert("stop_sequence, pickup_type, drop_off type " +
                    "needs to be a valid integer. "
                    + "\nGot: stop_sequence: " + fieldFive.getText() + ","
                    + " pickup_type: " + fieldSeven.getText() + ","
                    + " drop_off_type: " + fieldEight.getText()
                    + "\nFail to update " + instance + " attributes");
        } catch (ParseException ex){
            failAlert("arrival_time, departure_time needs to be in " +
                    "HH:mm:ss format. "
                    + "\nGot: arrival_time: " + fieldTwo.getText() + ","
                    + " departure_time: " + fieldThree.getText()
                    + "\nFail to update " + instance + " attributes");
        }
    }

    /**
     * Show the alert if user's inputs are invalid
     * @author Thy Le
     * @param msg the message specify which instance and field
     */
    private void failAlert(String msg){
        mainController.hideSecondStage();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Show the alert if the info successfully updated
     * @author Thy Le
     */
    private void successAlert() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Update Attributes");
        info.setContentText("Successfully update " + instance + " attributes");
        info.showAndWait();
    }


    /**
     * Clear all the text fields after updating information
     * @author Thy Le
     */
    private void clearField(){
        fieldTwo.setText("");
        fieldThree.setText("");
        fieldFour.setText("");
        fieldFive.setText("");
        fieldSix.setText("");
        fieldSeven.setText("");
        fieldEight.setText("");
        fieldNine.setText("");
    }

}
