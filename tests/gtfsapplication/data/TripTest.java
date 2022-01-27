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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import gtfsapplication.GTFSException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Trip validation
 * @author Thy Le
 * @version 10/22/2020
 */
class TripTest {

    GTFS gtfs;

    @BeforeEach
    void setUp() {
        gtfs = new GTFS();
    }

    @AfterEach
    void tearDown() {
        gtfs = null;
    }

    /**
     * Test if validateHeader returns 2 fields given 2 required fields.
     * @author Thy Le
     */
    @Test
    void validateHeader_requiredFields() throws GTFSException {
        String header = "route_id,trip_id";
        List<String> fields = Trip.validateHeader(header);
        assertEquals(2, fields.size());
    }

    /**
     * Test if validateHeader returns 7 fields given 7 required and optional fields.
     * @author Thy Le
     */
    @Test
    void validateHeader_optionalFields() throws GTFSException {
        String header = "route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id";
        List<String> fields = Trip.validateHeader(header);
        assertEquals(7, fields.size());
    }

    /**
     * Test if validateHeader throws IllegalArgumentException given the required fields are missing.
     * @author Thy Le
     */
    @Test
    void validateHeader_missingRequiredFields() {
        String header = "service_id";
        assertThrows(GTFSException.class, () -> Trip.validateHeader(header));
    }

    /**
     * Test if validateHeader throws IllegalArgumentException given that the header has syntax errors.
     * @author Thy Le
     */
    @Test
    void validateHeader_syntaxError() {
        String header = "route,trip,headsign,direction,block,shape";
        assertThrows(GTFSException.class, () -> Trip.validateHeader(header));
    }

    /**
     * Test if validateLine returns true given a valid line with all required fields filled.
     * @author Thy Le
     */
    @Test
    void validateLine_validLine(){
        List<String> fields = Arrays.asList("route_id", "service_id", "trip_id", "trip_headsign",
                "direction_id" ,"block_id", "shape_id");
        String line = "64,17-SEP_SUN,21736564_2535,60TH-VLIET,0,64102,17-SEP_64_0_23";
        assertTrue(Trip.validateLine(fields, line));
    }

    /**
     * Test if validateLine returns false if the line is missing required fields.
     * @author Thy Le
     */
    @Test
    void validateLine_invalidLine() {
        List<String> fields = Arrays.asList("route_id", "trip_id", "trip_headsign",
                "direction_id" ,"block_id", "shape_id");
        String line = "64,17-SEP_SUN";
        assertFalse(Trip.validateLine(fields, line));
    }

    /**
     * Test if validateLine throws IllegalArgumentException given the empty fields.
     * @author Thy Le
     */
    @Test
    void validateLine_emptyField(){
        List<String> fields = Arrays.asList("route_id", "service_id", "trip_id", "trip_headsign",
                "direction_id" ,"block_id", "shape_id");
        String line = ",MF,,,,,Route1_shp";
        assertFalse(Trip.validateLine(fields, line));
    }












}