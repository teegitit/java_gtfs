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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Stop Time Class validation.
 *
 * @author Luke Miller
 * @version 10/19/2020
 */
class StopTimeTest {

    GTFS gtfs;
    private String possibleHeaders = "trip_id,arrival_time,departure_time,stop_id," +
            "stop_sequence,stop_headsign,pickup_type,drop_off_type";
    private List<String> possibleHeadersList = new ArrayList<>();
    private String[] possibleHeadersArray = {"trip_id","arrival_time","departure_time","stop_id",
           "stop_sequence,","stop_headsign","pickup_type","drop_off_type"};
    private String[] requiredHeaders = {"trip_id", "stop_id", "stop_sequence"};
    private String[] conditionalRequiredHeaders = {"arrival_time", "departure_time"};
    private String[] optionalHeaders = {"stop_headsign", "pickup_type", "drop_off_type"};

    @BeforeEach
    void setUp() {
        gtfs = new GTFS();
        for (int i = 0; i < possibleHeadersArray.length; i++) {
            possibleHeadersList.add(possibleHeadersArray[i]);
        }
    }

    @AfterEach
    void tearDown() { gtfs = null; }

    /**
     * Check to makes sure all fields present in the header are valid fields and are suppose to be
     * in the stop_times header.
     *
     * @author Luke Miller
     */
    @Test
    void validateHeader_AllValidHeaders() throws GTFSException {
        String validHeader = "[trip_id, arrival_time, departure_time, stop_id, " +
                "stop_sequence, stop_headsign, pickup_type, drop_off_type]";
        List<String> returnedHeader = StopTime.validateHeader(possibleHeaders);
        assertEquals(returnedHeader.toString(), validHeader);
    }

    //Test for all required headers are included
    /**
     *Tests to makes sure all required fields are in the header.
     *
     * @author Luke Miller
     */
    @Test
    void validateHeader_allRequiredHeadersIncluded() throws GTFSException {
        String submittableHeader = "";
        for (int i = 0; i < requiredHeaders.length; i++) {
            submittableHeader += requiredHeaders[i] + ",";
        }
        submittableHeader = submittableHeader.substring(0, submittableHeader.length() - 1);
        List<String> returnedHeader = StopTime.validateHeader(submittableHeader);
        assertEquals(returnedHeader, Arrays.asList(requiredHeaders));
    }

    /**
     *Counts all Conditionally Required fields and optional fields in an complete and full header.
     *
     * @author Luke Miller
     */
    @Test
    void validateHeader_optionalHeaders() throws GTFSException {
        List<String> returnedHeader = StopTime.validateHeader(possibleHeaders);
        int countedOptionalHeaders = 0;
        ArrayList<String> conditReqList = new ArrayList(Arrays.asList(conditionalRequiredHeaders));
        ArrayList<String> conditList = new ArrayList(Arrays.asList(optionalHeaders));

        for (int i = 0; i < returnedHeader.size(); i++) {
            if (conditReqList.contains(returnedHeader.get(i)) || conditList.contains(returnedHeader.get(i))) {
                countedOptionalHeaders++;
            }
        }

        assertEquals(countedOptionalHeaders, 5);
    }

    /**
     *There are no repeating or redundant fields in the header.
     *
     * @author Luke Miller
     */
    @Test
    void validateHeader_NoRepeatingHeaders() throws GTFSException {
        List<String> returnedHeader = StopTime.validateHeader(possibleHeaders);
        boolean[] checkOffHeader = {false, false, false, false, false, false, false, false};
        ArrayList<String> possibleHeaderList = new ArrayList(Arrays.asList(possibleHeaders));

        for (int i = 0; i < returnedHeader.size(); i++) {
            if (possibleHeaderList.contains(returnedHeader.get(i))) {
                assertEquals(checkOffHeader[i], true);

                checkOffHeader[i] = true;
            }
        }
    }

    /**
     *Checks to makes sure no more than 8 headers are present in the header line
     * @author Luke Miller
     */
    @Test
    void validateHeader_NumberOfHeadersCheck() throws GTFSException {
        List<String> returnedHeader = StopTime.validateHeader(possibleHeaders);
        assertEquals(returnedHeader.size(), possibleHeadersList.size());
    }

    /**
     *Test trips if Header is empty or not existent.
     *
     * @author Luke Miller
     */
    @Test
    void validateHeader_emptyHeader() throws GTFSException {
        List<String> returnedHeader = StopTime.validateHeader(possibleHeaders);
        assertNotEquals(returnedHeader, null);
        assertNotEquals(returnedHeader.isEmpty(), true);
    }

    //Validate Line tests go below

    /**
     *Test to check to make sure that dropOffType, pickupType, stopSequence are all Integers.
     *
     * @author Luke Miller
     */
    @Test
    void validateLine_IntegerCheck() {
        String exampleLine = "1,,,3,10,,0,0";
        String exampleLine2 = "21742829_734,10:55:00,10:55:00,25,80,,0,0";
        String exampleLine3 = "21799867_1462,18:30:00,18:30:00,482,1,,0,0";

        boolean lineReturned = StopTime.validateLine(possibleHeadersList, exampleLine);
        assertTrue(lineReturned);
        lineReturned = StopTime.validateLine(possibleHeadersList,  exampleLine2);
        assertTrue(lineReturned);
        lineReturned = StopTime.validateLine(possibleHeadersList,  exampleLine3);
        assertTrue(lineReturned);
    }

    /**
     *Test to check to make sure that trip_id, arrival_time, departure_time, stop_id, stop_headsign are all String
     *
     * @author Luke Miller
     */
    @Test
    void validateLine_StringCheck() {
        String exampleLine1 = "21736564_2535,08:55:00,08:55:00,4763,10,,0,0";
        String exampleLine2 = "21742829_734,10:55:00,10:55:00,25,80,,0,0";
        String exampleLine3 = "21799867_1462,18:30:00,18:30:00,482,1,,0,0";

        Boolean lineValidated = StopTime.validateLine(possibleHeadersList,  exampleLine1);
        assertTrue(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList,  exampleLine2);
        assertTrue(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList,  exampleLine3);
        assertTrue(lineValidated);
    }

    /**
     * Test to check to see all fields are detected to be present in the lines
     *
     * @author Luke Miller
     */
    @Test
    void validateLine_AllHeadersIncluded() {
        String exampleLine1 = "1,,0,3,10,,0,0";
        String exampleLine2 = "21742829_734,10:55:00,10:55:00,25,80,,0,0";
        String exampleLine3 = "21799867_1462,18:30:00,,0,0";
        String exampleLine4 = "21736564_2535,,,4763,10,,0,0";
        String exampleLine5 = "21736564_2535,09:08:00,09:08:00,6292,31,,0,";
        String exampleLine6 = ",18:30:00,18:30:00,482,1,,0,0";
        String exampleLine7 = "1,,0,,,,0,0";
        String exampleLine8 = "trip_id,arrival_time,departure_time,stop_id,stop_sequence," +
                "stop_headsign,pickup_type,drop_off_type";
        String exampleLine9 = "21736564_2535,09:19:00,09:19:00,462,47,,0,";
        String exampleLine10 = ",,,,,,,";

        Boolean lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine1);
        assertTrue(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine2);
        assertTrue(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine3);
        assertFalse(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine4);
        assertTrue(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine5);
        assertTrue(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine6);
        assertFalse(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine7);
        assertFalse(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine8);
        assertFalse(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine9);
        assertTrue(lineValidated);
        lineValidated = StopTime.validateLine(possibleHeadersList, exampleLine10);
        assertFalse(lineValidated);

    }
}