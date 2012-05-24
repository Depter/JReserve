package org.decsi.jreserve.data;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peti
 */
public class MonthDateTest {
    
    private final static int YEAR = 2012;
    private final static int MONTH = 4;
    
    private MonthDate date;
    
    public MonthDateTest() {
    }
    
    @Before
    public void setUp() {
        date = new MonthDate(YEAR, MONTH);
    }
    @Test
    public void testGetMonth() {
        assertEquals(MONTH, date.getMonth());
    }

    /**
     * Test of getYear method, of class MonthDate.
     */
    @Test
    public void testGetYear() {
        assertEquals(YEAR, date.getYear());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, date.compareTo(date));
        
        MonthDate date2 = new MonthDate(YEAR, MONTH+1);
        assertTrue(date.compareTo(date2) < 0);
        assertTrue(date2.compareTo(date) > 0);
        
        date2 = new MonthDate(YEAR+1, MONTH);
        assertTrue(date.compareTo(date2) < 0);
        assertTrue(date2.compareTo(date) > 0);
    }

    @Test
    public void testEquals() {
        assertTrue(date.equals(date));
        
        MonthDate date2 = new MonthDate(YEAR, MONTH+1);
        assertFalse(date.equals(date2));
        assertFalse(date2.equals(date));
        
        date2 = new MonthDate(YEAR+1, MONTH);
        assertFalse(date.equals(date2));
        assertFalse(date2.equals(date));
    }

    @Test
    public void testToString() {
        assertEquals("2012-05", date.toString());
    }
}
