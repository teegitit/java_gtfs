/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: Thy Lee
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
 * Data implementation for Trip
 * @author Thy Le
 * @version 10/22/2020
 */
public class Trip {
    private String blockID;
    private int directionId;
    private String headSign;
    private String serviceId;
    private String tripId;
    private String routeId;
    private String shapeId;

    private static String[] requiredFields = {"route_id", "trip_id"};
    private static String[] optionalFields = {
            "trip_headsign",
            "direction_id",
            "block_id",
            "shape_id",
            "service_id"
    };


    /**
     * Constructor for Trip that already exists
     * @param blockID identifies the block to which a trip belongs
     * @param directionId indicates the direction of travel for a trip
     * @param headSign text that appears on signage identifying the trip's destination to riders
     * @param serviceId identifies a set of dates when service is available for one or more routes
     * @param id identifies a trip
     * @param routeId identifies a route id on a trip
     * @param shapeId identifies the shape id of a trip
     */
    public Trip(String blockID, int directionId, String headSign, String serviceId, String id,
                String routeId, String shapeId) {
        this.blockID = blockID;
        this.directionId = directionId;
        this.headSign = headSign;
        this.serviceId = serviceId;
        this.tripId = id;
        this.routeId = routeId;
        this.shapeId = shapeId;
    }

    /**
     * Creates a trip object from a raw line and list of fields
     * @param fields list of fields retrieved from Route.validateHeader
     * @param raw raw line from trips.txt
     * @throws GTFSException if there is a problem importing the file
     */
    public Trip(List<String> fields, String raw) throws GTFSException {
        List<String> values = ImportHelper.parseLine(raw);
        if (fields.size() != values.size()) {
            throw new GTFSException("trips.txt\n" +
                    "Expected " + fields.size() + " values, " +
                    "got " + values.size());
        }

        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String value = values.get(i);

            try {
                switch (field) {
                    case "route_id":
                        if (value.isEmpty()) {
                            throw new GTFSException("trips.txt\n" +
                                    "route_id is required");
                        }
                        routeId = value;
                        break;
                    case "service_id":
                        serviceId = value;
                        break;
                    case "trip_id":
                        if (value.isEmpty()) {
                            throw new GTFSException("trips.txt\n" +
                                    "trip_id is required");
                        }
                        tripId = value;
                        break;
                    case "trip_headsign":
                        headSign = value;
                        break;
                    case "direction_id":
                        if(!value.isEmpty()){
                            directionId = Integer.parseInt(value);
                        }
                        break;
                    case "block_id":
                        blockID = value;
                        break;
                    case "shape_id":
                        shapeId = value;
                        break;
                }
            } catch (NumberFormatException ex) {
                throw new GTFSException("trips.txt\n" +
                        field + " needs to be an integer. Got: " + value);
            } catch (IllegalArgumentException ex) {
                throw new GTFSException("trips.txt\n" +
                        field + " is required.");
            }
        }
    }

    /**
     * Validates that the format of a header is valid
     * @param header the header to validate
     * @return the variables included in the header
     * @throws GTFSException if the error occurs while reading the first line
     */
    public static List<String> validateHeader(String header) throws GTFSException {
        return ImportHelper.validateHeader(
                "trips.txt",
                Arrays.asList(requiredFields),
                Arrays.asList(optionalFields),
                header
        );
    }

    /**
     * Validate each line of the trip data
     * @param fields each field of the line
     * @param line the line to validate
     * @return true if the line is valid, false if the line is missing required fields.
     */
    public static boolean validateLine(List<String> fields, String line) {
        try {
            new Trip(fields, line);
            return true;
        } catch (GTFSException ex) {
            return false;
        }
    }

    public String getServiceId(){
        return this.serviceId;
    }

    public void setServiceId(String serviceId){
        this.serviceId = serviceId;
    }

    public String getBlockID() {

        return blockID;
    }

    public void setBlockID(String blockID) {

        this.blockID = blockID;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {

        this.directionId = directionId;
    }

    public String getHeadSign() {

        return headSign;
    }

    public void setHeadSign(String headSign) {

        this.headSign = headSign;
    }

    public String getId() {

        return tripId;
    }

    public void setId(String id) {

        this.tripId = id;
    }

    public String getRouteId() {

        return routeId;
    }

    public void setRouteId(String routeId) {

        this.routeId = routeId;
    }


    public String getShapeId() {

        return shapeId;
    }

    public void setShapeId(String shapeId) {

        this.shapeId = shapeId;
    }

    @Override
    public String toString(){
        return this.routeId + ","
                + this.serviceId + ","
                + this.tripId + ","
                + this.headSign + ","
                + this.directionId + ","
                + this.blockID + ","
                + this.shapeId;
    }



}
