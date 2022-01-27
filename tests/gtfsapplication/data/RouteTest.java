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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import gtfsapplication.GTFSException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Route validation
 *
 * @author Kenneth McDonough
 * @version 10/19/2020
 */
class RouteTest {

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
     * Tests that validateHeader returns 9 fields given 9 fields in a valid header
     * @author Kenneth McDonough
     */
    @Test
    void validateHeader_validHeader() {
        String header = "route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color";
        List<String> fields = null;
        try {
            fields = Route.validateHeader(header);
        } catch (GTFSException e) {
            e.printStackTrace();
        }
        assertEquals(9, fields.size());
    }

    /**
     * Tests that validateHeader returns 2 fields when given the minimum required fields in a valid header
     * @author Kenneth McDonough
     */
    @Test
    void validateHeader_validHeader_minimumFields() throws GTFSException {
        String header = "route_id,route_color";
        List<String> fields = Route.validateHeader(header);
        assertEquals(2, fields.size());
    }

    /**
     * Tests that validateHeader throws an IllegalArgument exception when provided an invalid header
     * (in this case, route_long_name is misspelled)
     * @author Kenneth McDonough
     */
    @Test
    void validateHeader_invalidHeader() {
        String header = "route_id,agency_id,route_short_name,route_long_nae,route_desc,route_type,route_url,route_color,route_text_color";
        assertThrows(GTFSException.class, () -> Route.validateHeader(header));
    }

    /**
     * Tests that validateHeader throws an IllegalArgumentException if a required field is left out
     * @author Kenneth McDonough
     */
    @Test
    void validateHeader_invalidHeader_missingRequired() {
        String header = "route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_text_color";
        System.out.println(assertThrows(GTFSException.class, () -> Route.validateHeader(header)).getMessage());
    }

    /**
     * Tests that validateLine returns true when provided a valid line that matches the fields
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color"});
        String line = "30X,00FF00";
        assertTrue(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine respects quotes
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine_quotedValueStart() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color"});
        String line = "\"30X\",00FF00";
        assertTrue(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine validates URLs
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine_validUrl() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color", "route_url"});
        String line = "\"30X\",00FF00,https://example.com";
        assertTrue(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine validates types
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine_validType() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color", "route_type"});
        String line = "\"30X\",00FF00,3";
        assertTrue(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine respects quotes
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine_quotedValueMiddle() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_desc", "route_color"});
        String line = "30X,\"Some thing, you know\",00FF00";
        assertTrue(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine respects quotes
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine_quotedValueEnd() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color"});
        String line = "30X,\"00FF00\"";
        assertTrue(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine returns false if there are more values than fields
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_invalidLine_lengthMismatch() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color"});
        String line = "30X,\"00FF00\",\"00FF00\"";
        assertFalse(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine verifies that values are correct for the specific field
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_invalidLine_typeMismatch() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color"});
        String line = "30X,\"100%notacolor\"";
        assertFalse(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine validates URLs
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine_invalidUrl() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color", "route_url"});
        String line = "\"30X\",00FF00,https/example.com";
        assertFalse(Route.validateLine(fields, line));
    }

    /**
     * Tests that validateLine validates types
     * @author Kenneth McDonough
     */
    @Test
    void validateLine_validLine_invalidType() {
        List<String> fields = Arrays.asList(new String[] {"route_id", "route_color", "route_type"});
        String line = "\"30X\",00FF00,3a";
        assertFalse(Route.validateLine(fields, line));
    }

}