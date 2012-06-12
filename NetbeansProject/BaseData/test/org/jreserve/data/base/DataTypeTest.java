package org.jreserve.data.base;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataTypeTest {
    
    private final static String NAME = "Paid";
    private DataType dt;
    
    public DataTypeTest() {
    }

    @Before
    public void setUp() {
        dt = new DataType(NAME, true);
    }

    @Test
    public void testGetId() {
        assertEquals(0, dt.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, dt.getName());
        dt.setName("Bela");
        assertEquals("Bela", dt.getName());
    }

    @Test
    public void testGetIsTriangle() {
        assertTrue(dt.isTriangle());
        dt.setIsTriangle(false);
        assertFalse(dt.isTriangle());
    }

    @Test
    public void testToString() {
        String expected = "DataType ["+NAME+"]";
        assertEquals(expected, dt.toString());
    }
}