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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class StopTest {

    GTFS gtfs;

    @BeforeEach
    void setUp() {
        gtfs = new GTFS();
    }

    @AfterEach
    void tearDown() {
        gtfs  = null;
    }


    /**
     * Test to validate that the header contains all 5 of the expected parameters
     * @Author William Lauer
     */
    @Test
    void validateHeader_validHeader() throws GTFSException {
        String header = "stop_id,stop_name,stop_desc,stop_lat,stop_lon";
        List<String> parameters = Stop.validateHeader(header);
        assertEquals(5, parameters.size());
    }

    /**
     * Test to see if the header is invalid in that there are less than the required number of parameters
     * @Author William Lauer
     */
    @Test
    void validateHeader_invalidHeader() {
        String header = "stop_id,stop_name,stop_lat";
        assertThrows(GTFSException.class, () -> Stop.validateHeader(header));
    }

    @Test
    void validateHeader_differentlyOrderedHeader() throws GTFSException{
        String header = "stop_desc,stop_lon,stop_id,stop_lat,stop_name";
        List<String> parameters = Stop.validateHeader(header);
        assertEquals(5, parameters.size());
    }

    /**
     * Test to see if an empty header, when passed into validate header, throws the necessary exception
     * @Author William Lauer
     */
    @Test
    void validateHeader_emptyHeader() {
        String header = "";
        assertThrows(GTFSException.class, () -> Stop.validateHeader(header));
    }

    /**
     * Test that validates that the lines contain the necessary fields
     * @Author William Lauer
     */
    @Test
    void validateLines_validLine() {
        List<String> validHeader = Arrays.asList("stop_id", "stop_name", "stop_desc",
                "stop_lat", "stop_lon");
        String validLine = "1801,S92 & ORCHARD #1801,,43.0138967,-88.0272162";

        assertTrue(Stop.validateLine(validHeader, validLine));
    }

    /**
     * Test that asserts that there is an invalid line structure
     * @Author William Lauer
     */
    @Test
    void validateLines_invalidLine() {
        List<String> validHeader = Arrays.asList("stop_id", "stop_name", "stop_desc",
                "stop_lat", "stop_lon");
        String invalidLine = "1802,D33 & CHERRY #2366,22.1230221";

        assertFalse(Stop.validateLine(validHeader, invalidLine));
    }

    /**
     * Test that checks that an empty line will return false when fed into validateLines
     * @Author William Lauer
     */
    @Test
    void validateLines_emptyLine() {
        List<String> validHeader = Arrays.asList("stop_id", "stop_name", "stop_desc",
                "stop_lat", "stop_lon");
        String emptyLine = "";
        assertFalse(Stop.validateLine(validHeader, emptyLine));
    }
}