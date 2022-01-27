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
package gtfsapplication.data;

import gtfsapplication.GTFSException;
import gtfsapplication.Observer;
import gtfsapplication.Subject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Data implementation for GTFS
 *
 * @author Kenneth McDonough, Luke Miller, Thy Le, William Lauer
 * @version 10/11/2020
 */
public class GTFS implements Subject {
    private final HashMap<String, Route> routes = new HashMap<>();
    private final HashMap<String, Stop> stops = new HashMap<>();
    private final HashMap<String, Trip> trips = new HashMap<>();
    private final List<StopTime> stopTimes = new ArrayList<>();

    private final List<Observer> observers = new ArrayList<>();

    private String routesHeader;
    private String stopsHeader;
    private String tripsHeader;
    private String stopTimesHeader;

    private double meanLatitude;
    private double meanLongitude;

    /**
     * Takes a file, determines what type of file it is,
     * and passes it off to another method for handling.
     *
     * @param file file to import
     * @throws GTFSException if an error is to be shown to the user
     */
    public void importFile(File file) throws GTFSException {
        if (!file.exists()) {
            throw new GTFSException(file.getName() + " does not exist.");
        }
        try {
            switch (file.getName().toLowerCase()) {
                case "routes.txt":
                    routes.clear();
                    importRouteFile(file);
                    break;
                case "trips.txt":
                    trips.clear();
                    importTripFile(file);
                    break;
                case "stop_times.txt":
                    stopTimes.clear();
                    importStopTimesFile(file);
                    break;
                case "stops.txt":
                    stops.clear();
                    importStopFile(file);
                    break;
                default:
                    throw new GTFSException("The application only accepts routes.txt," +
                            "trips.txt, stop_times.txt, or stops.txt");
            }
            List<Object> objects = new ArrayList<>();
            objects.add("All Routes:");
            objects.addAll(routes.values());
            notifyObservers(objects);
        } catch (IllegalArgumentException ex) {
            throw new GTFSException(ex.getMessage());
        }
    }

    /**
     * Imports and stores a stop file
     *
     * @author William Lauer
     * @param file stop file
     * @throws GTFSException if an error should be shown to the user
     */
    private void importStopFile(File file) throws GTFSException {
        try (Scanner reader = new Scanner(file)) {
            String header = reader.nextLine();
            List<String> fields = Stop.validateHeader(header);
            stopsHeader = header; // Valid because validateHeader throws an exception
            double latitude = Double.MIN_VALUE;
            double longitude = Double.MIN_VALUE;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (Stop.validateLine(fields, line)) {
                    Stop stop = new Stop(fields, line);
                    stops.put(stop.getId(), stop);

                    if (latitude == Double.MIN_VALUE || longitude == Double.MIN_VALUE) {
                        latitude = stop.getLatitude();
                        longitude = stop.getLongitude();
                    } else {
                        latitude += stop.getLatitude();
                        latitude /= 2;
                        longitude += stop.getLongitude();
                        longitude /= 2;
                    }
                }
            }
            this.meanLatitude = latitude;
            this.meanLongitude = longitude;
        } catch (FileNotFoundException ex) {
            throw new GTFSException("The stops.txt file did not exist.");
        }
    }

    /**
     * Imports and stores a route file
     *
     * @author Kenneth McDonough
     * @param file route file
     * @throws GTFSException if an error should be shown to the user
     */
    private void importRouteFile(File file) throws GTFSException {
        try (Scanner reader = new Scanner(file)) {
            String header = reader.nextLine();
            List<String> fields = Route.validateHeader(header);
            routesHeader = header; // Valid because validateHeader throws an exception
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (Route.validateLine(fields, line)) {
                    Route route = new Route(fields, line);
                    routes.put(route.getId(), route);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new GTFSException("The routes.txt file did not exist.");
        }
    }

    /**
     * Imports and stores a trip file
     *
     * @author Kenneth McDonough, Thy Le
     * @param file trip file
     * @throws GTFSException if error occurs while reading txt file
     */
    private void importTripFile(File file) throws GTFSException {
        try (Scanner reader = new Scanner(file)) {
            String header = reader.nextLine();
            List<String> fields = Trip.validateHeader(header);
            tripsHeader = header; // Valid because validateHeader throws an exception
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (Trip.validateLine(fields, line)) {
                    Trip trip = new Trip(fields, line);
                    trips.put(trip.getId(), trip);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new GTFSException("The routes.txt file did not exist.");
        }
    }

    /**
     * Imports and stores a stop times file
     *
     * @author Luke Miller
     * @param file stop times file
     * @throws GTFSException if an error should be shown to the user
     */
    private void importStopTimesFile(File file) throws GTFSException {
        try (Scanner reader = new Scanner(file)) {
            String header = reader.nextLine();
            List<String> fields = StopTime.validateHeader(header);
            stopTimesHeader = header; // Valid because validateHeader throws an exception
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (StopTime.validateLine(fields, line)) {
                    StopTime stopTime = new StopTime(fields, line);
                    stopTimes.add(stopTime);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new GTFSException("The stop_times.txt file did not exist.");
        }
    }

    /**
     * Gets a route by its id
     *
     * @param routeId id for the route to get
     * @return the route
     */
    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }

    /**
     * Get all routes in GTFS
     *
     * @return all routes
     */
    public List<Route> getRoutes() {
        return new ArrayList<>(this.routes.values());
    }

    /**
     * Gets a stop by its id
     *
     * @param stopId id for the stop to get
     * @return the stop
     */
    public Stop getStop(String stopId) {
        return stops.get(stopId);
    }

    /**
     * Gets a stop time based on a stopId and tripId
     *
     * @param stopId stopId to search
     * @param tripId tripId to search
     * @return the stop time
     */
    public StopTime getStopTime(String stopId, String tripId) {
        for (StopTime stopTime : stopTimes) {
            if (stopTime.getStopId().equals(stopId) && stopTime.getTripId().equals(tripId)) {
                return stopTime;
            }
        }
        return null;
    }

    /**
     * Searches for a stop and shows all routes that stop there and the next trip for each route
     * Feature 5 & 8
     *
     * @param stop stop to search
     */
    public void searchByStop(Stop stop) {
        List<Object> items = new ArrayList<>();
        List<Route> routes = getRoutesContainingStop(stop);
        List<Trip> trips = getUpcomingTrips(stop);
        items.add(stop);
        items.add("All Routes containing stop " + stop.getId());
        items.addAll(routes);
        items.add("Upcoming Trips for stop " + stop.getId());
        items.addAll(trips);
        notifyObservers(items);
    }

    /**
     * Searches for a route and show all stops on that route
     * Feature 6 & 7
     *
     * @param route route to search
     */
    public void searchByRoute(Route route) {
        List<Object> items = new ArrayList<>();
        List<Stop> stops = getStopsOnRoute(route);
        items.add(route);
        items.add("All stops on route " + route.getId() + ":");
        items.addAll(stops);
        List<Trip> trips = getFutureTripsOnRoute(route);
        items.add("All future trips on route " + route.getId() + ":");
        items.addAll(trips);
        notifyObservers(items);
    }

    /**
     * Gets all routes that contain the given stop
     *
     * @author Kenneth McDonough, Luke Miller, Thy Le, William Lauer
     * @param stop stop to search
     * @return all routes containing stop
     */
    public List<Route> getRoutesContainingStop(Stop stop) {
        List<Route> routeList = new ArrayList<>();
        for (StopTime stopTime : stopTimes) {
            if (stopTime.getStopId().equals(stop.getId())) {
                Trip trip = getTrip(stopTime.getTripId());
                if (trip != null) {
                    Route route = getRoute(trip.getRouteId());
                    if (route != null) {
                        if (!routeList.contains(route)) {
                            routeList.add(route);
                        }
                    }
                }
            }
        }
        return routeList;
    }

    /**
     * Gets all stops present on a route
     *
     * @param route route to search on
     * @return list of all stops on routes
     */
    public List<Stop> getStopsOnRoute(Route route) {
        List<Stop> stopList = new ArrayList<>();
        for (Trip trip : trips.values()) {
            if (trip.getRouteId().equalsIgnoreCase(route.getId())) {
                for (StopTime stopTime : stopTimes) {
                    if (stopTime.getTripId().equalsIgnoreCase(trip.getId())) {
                        Stop stop = getStop(stopTime.getStopId());
                        if (stop != null && !stopList.contains(stop)) {
                            stopList.add(stop);
                        }
                    }
                }
            }
        }
        return stopList;
    }

    /**
     * Gets all trips happening in the future on a route
     *
     * @param route route to search on
     * @return list of all future trips on route
     */
    public List<Trip> getFutureTripsOnRoute(Route route) {
        LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        HashMap<Trip, Boolean> tripStatuses = new HashMap<>();
        for (Trip trip : trips.values()) {
            if (trip.getRouteId().equalsIgnoreCase(route.getId())) {
                for (StopTime stopTime : stopTimes) {
                    if (stopTime.getTripId().equalsIgnoreCase(trip.getId())) {
                        if (!getDifference(
                                currentTime.format(DateTimeFormatter.ISO_LOCAL_TIME),
                                stopTime.getArrivalTime()).isNegative()
                        ) {
                            tripStatuses.put(trip, true);
                        }
                    }
                }
            }
        }
        return tripStatuses.entrySet().stream()
                .filter(Map.Entry::getValue) // only include true values
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Finds upcoming trips for all routes at a specified stop
     *
     * @author Kenneth McDonough, Luke Miller, Thy Le, William Lauer
     * @param stop stop to search
     * @return 1 upcoming trip for each route
     */
    public List<Trip> getUpcomingTrips(Stop stop) {
        HashMap<Route, Trip> upcomingTripsByRoute = new HashMap<>();

        LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        for (StopTime stopTime : stopTimes) {
            if (stopTime.getStopId().equalsIgnoreCase(stop.getId())) {
                Trip trip = getTrip(stopTime.getTripId());
                if (trip != null && getRoute(trip.getRouteId()) != null) {
                    Route route = getRoute(trip.getRouteId());
                    Duration difference = getDifference(
                            currentTime.format(DateTimeFormatter.ISO_LOCAL_TIME),
                            stopTime.getArrivalTime()
                    );
                    if (!upcomingTripsByRoute.containsKey(route) && !difference.isNegative()) {
                        upcomingTripsByRoute.put(route, trip);
                    } else if (upcomingTripsByRoute.containsKey(route)) {
                        StopTime time =
                                getStopTime(stop.getId(), upcomingTripsByRoute.get(route).getId());
                        Duration currentDifference = getDifference(
                                currentTime.format(DateTimeFormatter.ISO_LOCAL_TIME),
                                time.getArrivalTime()
                        );
                        if (!difference.minus(currentDifference).isNegative()) {
                            upcomingTripsByRoute.put(route, trip);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(upcomingTripsByRoute.values());
    }

    /**
     * Gets a trip based on its trip id
     *
     * @param tripId id for the trip to get
     * @return the trip
     */
    public Trip getTrip(String tripId) {
        return trips.get(tripId);
    }

    public double getMeanLatitude() {
        return this.meanLatitude;
    }

    public double getMeanLongitude() {
        return this.meanLongitude;
    }

    /**
     * Checks if the GTFS object has any stops
     *
     * @return true if stops exist, false otherwise
     */
    public boolean hasStops() {
        return stops.size() >= 1;
    }

    /**
     * Checks if the GTFS object has any stop times
     *
     * @return true if stop times exist, false otherwise
     */
    public boolean hasStopTimes() {
        return stopTimes.size() >= 1;
    }

    /**
     * Checks if the GTFS object has any routes
     *
     * @return true if routes exist, false otherwise
     */
    public boolean hasRoutes() {
        return routes.size() >= 1;
    }

    /**
     * Checks if the GTFS object has any trips
     *
     * @return true if trips exist, false otherwise
     */
    public boolean hasTrips() {
        return trips.size() >= 1;
    }


    /**
     * Adds an observer to the subject
     *
     * @param observer observer to add
     */
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            observer.update(new ArrayList<>(routes.values()));
        }
    }

    /**
     * Removes an observer from the subject
     *
     * @param observer observer to remove
     */
    public void deleteObserver(Observer observer) {
        if (observer != null & observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    /**
     * Notifies all added observers that a change occurred
     *
     * @param objects A list of String, Stop, Trip, StopTime or Route objects;
     *                all other types ignored
     */
    public void notifyObservers(List<Object> objects) {
        for (Observer o : observers) {
            o.update(objects);
        }
    }

    /**
     * Exports all present GTFS objects to the specified directory
     *
     * @author Kenneth McDonough
     * @param directory directory to export files to
     * @throws GTFSException if an error is to be showed to the user
     */
    public void export(File directory) throws GTFSException {
        if (!directory.exists()) {
            throw new GTFSException("The directory you provided does not exist.");
        }
        if (!directory.isDirectory()) {
            throw new GTFSException("You must provide a directory, not a file.");
        }

        if (hasStops()) {
            exportStops(
                    new File(directory.getAbsolutePath() + File.separator + "stops.txt")
            );
        }
        if (hasStopTimes()) {
            exportStopTimes(
                    new File(directory.getAbsolutePath() + File.separator + "stop_times.txt")
            );
        }
        if (hasRoutes()) {
            exportRoutes(
                    new File(directory.getAbsolutePath() + File.separator + "routes.txt")
            );
        }
        if (hasTrips()) {
            exportTrips(
                    new File(directory.getAbsolutePath() + File.separator + "trips.txt")
            );
        }
    }

    /**
     * Exports the stops GTFS object to the specified location
     *
     * @author Kenneth McDonough
     * @param file location; file name MUST be stops.txt
     * @throws GTFSException if an error is to be showed to the user
     */
    public void exportStops(File file) throws GTFSException {
        if (!hasStops()) {
            throw new GTFSException("There are no stops to export.");
        }
        createIfNotExists(file);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(stopsHeader);
            for (Stop stop : stops.values()) {
                writer.println(stop);
            }
        } catch (FileNotFoundException ex) {
            throw new GTFSException("An error occurred exporting the stops file.");
        }
    }

    /**
     * Exports the stop times GTFS object to the specified location
     *
     * @author Kenneth McDonough
     * @param file location; file name MUST be stop_times.txt
     * @throws GTFSException if an error is to be showed to the user
     */
    public void exportStopTimes(File file) throws GTFSException {
        if (!hasStopTimes()) {
            throw new GTFSException("There are no stop times to export.");
        }
        createIfNotExists(file);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(stopTimesHeader);
            for (StopTime stopTime : stopTimes) {
                writer.println(stopTime);
            }
        } catch (FileNotFoundException ex) {
            throw new GTFSException("An error occurred exporting the stop times file.");
        }
    }

    /**
     * Exports the routes GTFS object to the specified location
     *
     * @author Kenneth McDonough
     * @param file location; file name MUST be routes.txt
     * @throws GTFSException if an error is to be showed to the user
     */
    public void exportRoutes(File file) throws GTFSException {
        if (!hasRoutes()) {
            throw new GTFSException("There are no routes to export.");
        }
        createIfNotExists(file);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(routesHeader);
            for (Route route : routes.values()) {
                writer.println(route);
            }
        } catch (FileNotFoundException ex) {
            throw new GTFSException("An error occurred exporting the routes file.");
        }
    }

    /**
     * Exports the trips GTFS object to the specified location
     *
     * @author Kenneth McDonough
     * @param file location; file name MUST be trips.txt
     * @throws GTFSException if an error is to be showed to the user
     */
    public void exportTrips(File file) throws GTFSException {
        if (!hasTrips()) {
            throw new GTFSException("There are no trips to export.");
        }
        createIfNotExists(file);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(tripsHeader);
            for (Trip trip : trips.values()) {
                writer.println(trip);
            }
        } catch (FileNotFoundException ex) {
            throw new GTFSException("An error occurred exporting the trips file.");
        }
    }

    /**
     * Gets the difference between two times
     *
     * @param time1 Format should be HH:mm:ss (may pass midnight)
     * @param time2 Format should be HH:mm:ss (may pass midnight)
     * @return duration between two stops
     */
    private Duration getDifference(String time1, String time2) {
        if (time1.split(":").length < 2 ||
                time2.split(":").length < 2) {
            throw new IllegalArgumentException(time1 + " and/or " + time2 + " are invalid times.");
        }
        try {
            String[] tg1 = time1.split(":");
            String[] tg2 = time2.split(":");

            int hour1 = Integer.parseInt(tg1[0]);
            int minute1 = Integer.parseInt(tg1[1]);
            int second1 = tg1[2] == null ? 0 : Integer.parseInt(tg1[2]);

            int hour2 = Integer.parseInt(tg2[0]);
            int minute2 = Integer.parseInt(tg2[1]);
            int second2 = tg2[2] == null ? 0 : Integer.parseInt(tg2[2]);

            int hourDiff = hour1 - hour2;
            int minuteDiff = minute1 - minute2;
            int secondDiff = second1 - second2;

            return Duration.parse(String.format("PT%dH%dM%dS", hourDiff, minuteDiff, secondDiff));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(time1 + " and/or " + time2 + " are invalid times.");
        }
    }

    private void createIfNotExists(File file) throws GTFSException {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new GTFSException("An error occurred creating a file.");
                }
            } catch (IOException ex) {
                throw new GTFSException("An error occurred creating a file.");
            }
        }
    }

    /**
     * Method so SecondController can remotely update Observers, does so whenever SecondController
     * closes out of the window, making the Observers update as the window closes.
     * <p>
     * This method is designed as an way to be able to update observers outside of the GTFS class.
     *
     * @author Luke Miller
     */
    public void updateObservers() {
        List<Object> routes = new ArrayList<>();
        routes.add("All Routes:");
        routes.addAll(getRoutes());
        notifyObservers(routes);
    }
}
