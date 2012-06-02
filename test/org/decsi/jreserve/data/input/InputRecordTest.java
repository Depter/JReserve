package org.decsi.jreserve.data.input;

import org.decsi.jreserve.TestUtil;
import org.decsi.jreserve.data.MonthDate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputRecordTest {
    
    final static MonthDate ACCIDENT = new MonthDate(2012, 1);
    final static MonthDate DEVELOPMENT = new MonthDate(0, 0);
    private double value = 12.65;
    private InputRecord record;
    
    public InputRecordTest() {
    }

    @Before
    public void setUp() {
        record = new InputRecord(ACCIDENT, DEVELOPMENT, value);
    }

    @Test
    public void testGetAccidentPeriod() {
        assertEquals(ACCIDENT, record.getAccidentPeriod());
    }

    /**
     * Test of getDevelopmentPeriod method, of class InputRecord.
     */
    @Test
    public void testGetDevelopmentPeriod() {
        assertEquals(DEVELOPMENT, record.getDevelopmentPeriod());
    }

    /**
     * Test of getValue method, of class InputRecord.
     */
    @Test
    public void testGetValue() {
        TestUtil.assertEquals(value, record.getValue());
    }

    /**
     * Test of equals method, of class InputRecord.
     */
    @Test
    public void testEquals_Object() {
        assertFalse(record.equals(null));
        assertFalse(record.equals(new Object()));
    }

    /**
     * Test of equals method, of class InputRecord.
     */
    @Test
    public void testEquals_InputRecord() {
        InputRecord r2 = new InputRecord(ACCIDENT, DEVELOPMENT, value);
        assertTrue(record.equals(r2));
        assertTrue(r2.equals(record));
        
        r2 = new InputRecord(ACCIDENT, DEVELOPMENT, value-1d);
        assertTrue(record.equals(r2));
        assertTrue(r2.equals(record));
        
        r2 = new InputRecord(DEVELOPMENT, ACCIDENT, value);
        assertFalse(record.equals(r2));
        assertFalse(r2.equals(record));
    }
}