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

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized import logic for GTFS files
 *
 * @author Kenneth McDonough
 * @version 10/21/2020
 */
public class ImportHelper {
    /**
     * Validates that the header of a GTFS file is valid
     * and determines the included fields
     * @param filename file name that is being imported, used exclusively for error messages
     * @param requiredFields a list of fields that MUST be included
     * @param optionalFields a list of fields that can be optionally included
     * @param header the actual header provided in the first line of the GTFS file
     * @return a list of included fields
     * @throws GTFSException if an error occurs in validation and needs to show an error to user
     */
    public static List<String> validateHeader(
            String filename, List<String> requiredFields, List<String> optionalFields, String header
    ) throws GTFSException {
        List<String> fields = parseLine(header);

        if (!fields.containsAll(requiredFields)) {
            throw new GTFSException(filename + "\n" +
                                    "Header is missing one more required fields.\n" +
                                    "Required fields: " + requiredFields);
        }

        for (String field : fields) {
            if (!requiredFields.contains(field) && !optionalFields.contains(field)) {
                throw new GTFSException(filename + "\n" +
                                        "Header has unexpected field: [" + field + "]");
            }
        }

        return fields;
    }

    /**
     * Parses a line
     * @param line the line to parse
     * @return a list of all values in the line
     */
    public static List<String> parseLine(String line) {
        char[] chars = line.toCharArray();
        List<String> values = new ArrayList<>();

        String value = "";
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == ',') {
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(0, value.length() - 1);
                    value = value.substring(1);
                    values.add(value.trim());
                    value = "";
                } else if (!value.contains("\"")) {
                    values.add(value.trim());
                    value = "";
                } else if (value.length() == 0) {
                    values.add("");
                } else {
                    value += c;
                }
                if (i == chars.length - 1) {
                    values.add("");
                }
            } else {
                value += c;
                if (i == chars.length - 1) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(0, value.length() - 1);
                        value = value.substring(1);
                        values.add(value.trim());
                    } else if (!value.contains("\"")) {
                        values.add(value.trim());
                    } else {
                        throw new IllegalArgumentException("Missing \" at end of header.");
                    }
                }
            }
        }

        return values;
    }
}
