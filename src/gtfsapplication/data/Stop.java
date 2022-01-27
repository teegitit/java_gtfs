/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: William Lauer
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
 * Data implementation for Stop
 *
 * @author William Lauer
 * @version 10/6/2020
 */
public class Stop {
    private String description;
    private String id;
    private double latitude;
    private double longitude;
    private String name;

    private List<String> presentFields;
    private static String[] requiredFields = new String[] {
            "stop_id",
            "stop_lat",
            "stop_lon",
            "stop_name"
    };
    private static String[] optionalFields = new String[] {
            "stop_desc"
    };

    /**
     * Constructor for a Stop that already exists
     * @param description The stop description
     * @param id identifies a stop, station, or entrance
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     * @param name name of the location
     */
    public Stop(String description, String id, double latitude, double longitude, String name) {
        this.description = description;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;

    }

    /**
     * Creates a stop object from a raw line and list of fields
     * @param fields list of fields retrieved from Stop.validateHeader
     * @param raw raw line from stops.txt
     * @throws GTFSException if there is a problem importing the file
     */
    public Stop(List<String> fields, String raw) throws GTFSException {
        presentFields = fields;
        List<String> values = ImportHelper.parseLine(raw);
        if (fields.size() != values.size()) {
            throw new GTFSException("stops.txt\n" +
                                    "Expected " + fields.size() + " values, " +
                                    "got " + values.size());
        }

        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String value = values.get(i);

            try {
                switch (field) {
                    case "stop_id":
                        if (value.length() == 0) {
                            throw new GTFSException("stops.txt\nstop_id is required");
                        }
                        id = value;
                        break;
                    case "stop_name":
                        if (value.length() == 0) {
                            throw new GTFSException("stops.txt\nstop_name is required");
                        }
                        name = value;
                        break;
                    case "stop_lon":
                        longitude = Double.parseDouble(value);
                        break;
                    case "stop_lat":
                        latitude = Double.parseDouble(value);
                        break;
                    case "stop_desc":
                        description = value;
                        break;
                }
            } catch (NumberFormatException ex) {
                throw new GTFSException("stops.txt\n" +
                                        field + " needs to be a valid integer. Got: " + value);
            } catch (IllegalArgumentException ex) {
                throw new GTFSException("stops.txt\n" +
                                        field + " needs to be a valid color. Got: " + value);
            }
        }
    }

    /**
     * Validates that the format of the header is valid
     * @param header The header to validate
     * @return the variables included in the header
     * @throws GTFSException if an error should be shown to the user
     */
    public static List<String> validateHeader(String header) throws GTFSException {
        return ImportHelper.validateHeader(
                "stops.txt",
                Arrays.asList(requiredFields),
                Arrays.asList(optionalFields),
                header
        );
    }

    /**
     * Validates that the format of the line is valid
     * @param header The valid header format
     * @param line The contents of the line
     * @return Boolean indicating if the line was valid
     */
    public static boolean validateLine(List<String> header, String line) {
        try {
            new Stop(header, line);
            return true;
        } catch (GTFSException ex) {
            return false;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.id + "," + this.name + "," + this.description + "," + this.latitude
                + "," + this.longitude;
    }
}
