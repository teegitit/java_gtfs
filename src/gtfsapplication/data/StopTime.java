/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: Luke Miller
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

import java.util.Arrays;
import java.util.List;

/**
 * Data implementation for StopTime
 *
 * @author Luke Miller, Kenneth McDonough
 * @version 10/11/2020
 */
public class StopTime {
    private String tripId;
    private String arrivalTime;
    private String departureTime;
    private int dropOffType;
    private int pickupType;
    private String stopHeadsign;
    private String stopId;
    private int stopSequence;

    private List<String> presentFields;
    private static String[] requiredFields = {
            "trip_id",
            "stop_id",
            "stop_sequence"
    };
    private static String[] optionalFields = {
            "stop_headsign",
            "pickup_type",
            "drop_off_type",
            "arrival_time",
            "departure_time"
    };

    /**
     * Constructor for a StopTime that already exists.
     * @param tripId identifies a trip
     * @param arrivalTime arrival time at a specific stop for a specific trip on a route
     * @param departureTime departure time at a specific stop for a specific trip on a route
     * @param stopId identifies the serviced stop
     * @param stopSequence order of stops for a particular trip
     * @param stopHeadsign text that appears on signage identifying the trip's destination to riders
     * @param pickupType indicates pickup method (0-3, see GTFS reference)
     * @param dropOffType indicates drop off method (0-3, see GTFS reference)
     */
    public StopTime(String tripId, String arrivalTime, String departureTime, String stopId,
                    int stopSequence, String stopHeadsign, int pickupType, int dropOffType) {
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.dropOffType = dropOffType;
        this.pickupType = pickupType;
        this.stopHeadsign = stopHeadsign;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.tripId = tripId;
    }

    /**
     * Creates a stop times object from a raw line and list of fields
     * @param fields list of fields retrieved from StopTime.validateHeader
     * @param raw raw line from stop_times.txt
     * @throws GTFSException if there is a problem importing the file
     * @author Luke Miller, Kenneth McDonough
     */
    public StopTime(List<String> fields, String raw) throws GTFSException {
        presentFields = fields;
        List<String> values = ImportHelper.parseLine(raw);
        if (fields.size() != values.size()) {
            throw new GTFSException("stop_times.txt\n" +
                                    "Expected " + fields.size() + " values, " +
                                    "got " + values.size());
        }

        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String value = values.get(i);

            try {
                switch (field) {
                    case "trip_id":
                        if (value.length() == 0) {
                            throw new GTFSException("stop_times.txt\ntrip_id is required");
                        }
                        tripId = value;
                        break;
                    case "stop_id":
                        if (value.length() == 0) {
                            throw new GTFSException("stop_times.txt\nstop_id is required");
                        }
                        stopId = value;
                        break;
                    case "stop_sequence":
                        stopSequence = Integer.parseInt(value);
                        break;
                    case "stop_headsign":
                        stopHeadsign = value;
                        break;
                    case "pickup_type":
                        if (!value.isEmpty()) {
                            pickupType = Integer.parseInt(value);
                        }
                        break;
                    case "drop_off_type":
                        if (!value.isEmpty()) {
                            dropOffType = Integer.parseInt(value); 
                        }
                        break;
                    case "arrival_time":
                        arrivalTime = value;
                        break;
                    case "departure_time":
                        departureTime = value;
                        break;

                }
            } catch (NumberFormatException ex) {
                throw new GTFSException("stop_times.txt\n" +
                                        field + " needs to be a valid integer. Got: " + value);
            } catch (IllegalArgumentException ex) {
                throw new GTFSException("stop_times.txt\n" +
                                        field + " needs to be a valid color. Got: " + value);
            }
        }
    }

    /**
     * Validates that the format of a header is valid
     * @param header the header to validate
     * @return the variables included in the header
     * @throws GTFSException if an error is found parsing and should be shown to user
     * @author Luke Miller, Kenneth McDonough
     */
    public static List<String> validateHeader(String header) throws GTFSException {
        return ImportHelper.validateHeader(
                "stop_times.txt",
                Arrays.asList(requiredFields),
                Arrays.asList(optionalFields),
                header
        );
    }


    /**
     * Validates that the lines containing the data about stops are in the correct format.
     * @param fields the header to cross
     * @param line the line to validate
     * @return true or false
     * @author Luke Miller, Kenneth McDonough
     */
    public static boolean validateLine(List<String> fields, String line) {
        try {
            new StopTime(fields, line);
            return true;
        } catch (GTFSException ex) {
            return false;
        }
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getDropOffType() {
        return dropOffType;
    }

    public void setDropOffType(int dropOffType) {
        this.dropOffType = dropOffType;
    }

    public int getPickupType() {
        return pickupType;
    }

    public void setPickupType(int pickupType) {
        this.pickupType = pickupType;
    }

    public String getStopHeadsign() {
        return stopHeadsign;
    }

    public void setStopHeadsign(String stopHeadsign) {
        this.stopHeadsign = stopHeadsign;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return tripId + "," + arrivalTime + "," + departureTime + "," + stopId
                + "," + stopSequence + "," + stopHeadsign + "," + pickupType + "," + dropOffType;
    }
}
