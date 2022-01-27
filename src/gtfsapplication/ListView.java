/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: Kenneth McDonough
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
import gtfsapplication.data.StopTime;
import gtfsapplication.data.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern ListView
 *
 * @author Kenneth McDonough
 * @version 10/25/2020
 */
public class ListView extends javafx.scene.control.ListView<String> implements Observer {

    private GTFS gtfs;

    /**
     * Creates a new listview
     * @param gtfs gtfs object to observe
     */
    public ListView(GTFS gtfs) {
        this.gtfs = gtfs;

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                handleClickedCell(newValue);
            }
        });
    }

    private void handleClickedCell(String cell) {
        MapView mapView = new MapView(gtfs);
        List<Object> obj = new ArrayList<>();
        String id = cell.substring(cell.indexOf(":") + 1, cell.indexOf("(")).replace(" ", "");
        if(cell.contains("Route")){
            Route route = gtfs.getRoute(id);
            obj.add(route);
            System.out.println(obj);
            mapView.update(obj);
        }
        if(cell.contains("Stop")){
            Stop stop = gtfs.getStop(id);
            obj.add(stop);
            mapView.update(obj);
        }
    }

    /**
     * Notify the observer that it should update itself from the GTFS object.
     * @param items A list of String, Stop, Trip, StopTime or Route objects;
     *                all other types ignored
     */
    @Override
    public void update(List<Object> items) {
        if (items.size() < 1) {
            return;
        }
        getItems().clear();
        for (Object o : items) {
            if (o instanceof String) {
                getItems().add((String) o);
            } else if (o instanceof Route) {
                // o is a route, display the id and name if it exists
                Route route = (Route) o;
                getItems().add("Route: " + route.getId() +
                               (route.getShortName() != null ?
                                       String.format(" (%s)", route.getLongName()) : ""));
            } else if (o instanceof Stop) {
                // o is a stop, display the id and name
                Stop stop = (Stop) o;
                getItems().add("Stop: " + stop.getId() + " (" + stop.getName() + ")");
            } else if (o instanceof Trip) {
                // o is a trip, display the id
                Trip trip = (Trip) o;
                getItems().add(
                        "Trip: " + trip.getId() + " (" +
                        (trip.getDirectionId() == 0 ? "Outbound" : "Inbound") + ")"
                );
            } else if (o instanceof StopTime) {
                // o is a stoptime, display the stop id and trip id
                StopTime stopTime = (StopTime) o;
                getItems().add("Stop Time: " + stopTime.getStopId() + ", " + stopTime.getTripId() +
                               "(" + stopTime.getArrivalTime() + ")");
            }
        }
    }
}
