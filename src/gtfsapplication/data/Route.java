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
package gtfsapplication.data;

import gtfsapplication.GTFSException;
import javafx.scene.paint.Color;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data implementation for Route
 *
 * @author Kenneth McDonough
 * @version 10/6/2020
 */
public class Route {
    private String agencyId = "";
    private Color color;
    private Color textColor;
    private String description = "";
    private String id = "";
    private String longName = "";
    private String shortName = "";
    private int type;
    private URL url;

    private static List<String> presentFields;
    private static String[] requiredFields = {"route_id", "route_color"};
    private static String[] optionalFields = {
            "route_short_name", "route_long_name",
            "route_desc", "route_type", "route_url",
            "route_text_color", "agency_id"
    };

    /**
     * Constructor for a Route that already exists.
     * @param id identifies a route
     * @param shortName short name of a route
     * @param longName full name of a route
     * @param description description of a route that provides useful, quality information
     * @param type indicates the type of transportation used on a route (0-12, see GTFS reference)
     * @param url URL of a web page about the particular route
     * @param agencyId agency for the specified route
     * @param color route color designation that matches public facing material
     *              (defaults to white when omitted or empty)
     * @param textColor legible color to use for text drawn against a background of route_color
     *                  (defaults to black when omitted or empty)
     */
    public Route(String id, String shortName, String longName, String description, int type, URL url, String agencyId, Color color, Color textColor) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.type = type;
        this.url = url;
        this.agencyId = agencyId;
        this.color = color;
        this.textColor = textColor;
    }

    /**
     * Creates a route object from a raw line and list of fields
     * @param fields list of fields retrieved from Route.validateHeader
     * @param raw raw line from routes.txt
     * @throws GTFSException if there is a problem importing the file
     */
    public Route(List<String> fields, String raw) throws GTFSException {
        presentFields = fields;
        List<String> values = ImportHelper.parseLine(raw);
        if (fields.size() != values.size()) {
            throw new GTFSException("routes.txt\n" +
                                    "Expected " + fields.size() + " values, " +
                                    "got " + values.size());
        }

        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String value = values.get(i);

            try {
                switch (field) {
                    case "route_id":
                        if (value.length() < 1) {
                            throw new GTFSException("routes.txt\n" +
                                                    "route_id is required");
                        }
                        id = value;
                        break;
                    case "route_short_name":
                        shortName = value;
                        break;
                    case "route_long_name":
                        longName = value;
                        break;
                    case "route_desc":
                        description = value;
                        break;
                    case "route_type":
                        type = Integer.parseInt(value);
                        break;
                    case "route_url":
                        if (value.length() != 0) {
                            url = new URL(value);
                        }
                        break;
                    case "route_color":
                        color = Color.web("#" + value);
                        break;
                    case "route_text_color":
                        if (value.length() != 0) {
                            textColor = Color.web("#" + value);
                        }
                        break;
                }
            } catch (NumberFormatException ex) {
                throw new GTFSException("routes.txt\n" +
                                        field + " needs to be a valid integer. Got: " + value);
            } catch (MalformedURLException ex) {
                throw new GTFSException("routes.txt\n" +
                                        field + " needs to be a valid url or not provided. Got: " +
                                        value);
            } catch (IllegalArgumentException ex) {
                throw new GTFSException("routes.txt\n" +
                                        field + " needs to be a valid color. Got: " + value);
            }
        }
    }

    /**
     * Validates that the format of a header is valid
     * @param header the header to validate
     * @return the variables included in the header
     * @throws GTFSException if an error is found parsing and should be shown to user
     */
    public static List<String> validateHeader(String header) throws GTFSException {
        return ImportHelper.validateHeader(
                "routes.txt",
                Arrays.asList(requiredFields),
                Arrays.asList(optionalFields),
                header
        );
    }

    /**
     * Validates that the line provided matches the fields from the header
     * @param fields list of included fields retrieved from Route.validateHeader
     * @param line line to validate
     * @return true if line is valid, false if line is invalid
     */
    public static boolean validateLine(List<String> fields, String line) {
        try {
            new Route(fields, line);
            return true;
        } catch (GTFSException ex) {
            return false;
        }
    }

    private static List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        char[] chars = line.toCharArray();

        String value = "";
        boolean quoted = false;
        for (int i = 0; i <= chars.length; i++) {
            value += i == chars.length ? "" : chars[i];
            if (value.length() == 1 && value.equals("\"")) {
                quoted = true;
            }
            if ((!quoted && value.endsWith(",")) ||
                    (i == chars.length && !quoted)) {
                final int endSub = i == chars.length ? 0 : 1;
                values.add(value.substring(0, value.length() - endSub));
                value = "";
            }
            if ((quoted && value.endsWith("\",")) ||
                    (i == chars.length && quoted && value.endsWith("\""))) {
                final int endSub = i == chars.length ? 1 : 2;
                value = value.substring(1);
                value = value.substring(0, value.length() - endSub);
                values.add(value);
                value = "";
                quoted = false;
            }
        }
        return values;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
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

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        String line = "";
        for (String field : presentFields) {
            String value = "";
            switch (field) {
                case "route_id":
                    value = id;
                    break;
                case "route_short_name":
                    value = shortName;
                    break;
                case "route_long_name":
                    value = longName;
                    break;
                case "route_desc":
                    value = description;
                    break;
                case "agency_id":
                    value = agencyId;
                    break;
                case "route_type":
                    value = String.valueOf(type);
                    break;
                case "route_url":
                    value = (url != null ? url.toString() : "");
                    break;
                case "route_color":
                    value = toRGB(color);
                    break;
                case "route_text_color":
                    value = (textColor != null ? toRGB(textColor) : "");
                    break;
            }
            if (value.contains(",")) {
                value = "\"" + value + "\"";
            }
            line += value + ",";
        }
        return line.substring(0, line.length() - 1);
    }

    /*
     * This method was pulled from one of my Data Structures labs.
     */
    private static String toRGB(Color color) {
        final int multiply = 255;
        return String.format("%02X%02X%02X",
                (int)(color.getRed() * multiply),
                (int)(color.getGreen() * multiply),
                (int)(color.getBlue() * multiply));
    }
}
