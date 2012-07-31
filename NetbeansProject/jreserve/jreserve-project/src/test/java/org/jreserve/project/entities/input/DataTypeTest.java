package org.jreserve.project.entities.input;

import org.jreserve.project.entities.TestUtil;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Peter Decsi
 */
public class DataTypeTest {
    
    private final static String NAME = "Paid";
    
    private DataType dataType;
    
    public DataTypeTest() {
    }
    
    @Before
    public void setUp() {
        dataType = new DataType(NAME);
    }
    
    @Test
    public void testGetId() {
        assertEquals(0, dataType.getId());
    }
    
    @Test
    public void testGetName() {
        assertEquals(NAME, dataType.getName());
    }

    @Test
    public void testSetName() {
        dataType.setName("bela");
        assertEquals("bela", dataType.getName());
    }

    @Test(expected=NullPointerException.class)
    public void testSetName_Null() {
        dataType.setName(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_EmptyString() {
        dataType.setName("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_TooLong() {
        dataType.setName(TestUtil.TEXT_65);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_Null() {
        new DataType(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EmptyString() {
        new DataType("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooLong() {
        new DataType(TestUtil.TEXT_65);
    }

    @Test
    public void testEquals() {
        DataType dt1 = new DataType(NAME.toUpperCase());
        DataType dt2 = new DataType(NAME.toLowerCase());
        DataType dt3 = new DataType(NAME+NAME);
        
        assertFalse(dt1.equals(null));
        assertTrue(dt1.equals(dt2));
        assertFalse(dt1.equals(dt3));
    }

    @Test
    public void testToString() {
        String expected = String.format("DataType [0; %s]", NAME);
        assertEquals(expected, dataType.toString());
    }

}