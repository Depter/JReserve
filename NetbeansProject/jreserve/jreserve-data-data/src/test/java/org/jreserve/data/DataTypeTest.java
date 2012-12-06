package org.jreserve.data;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DataTypeTest {

    private final static int DB_ID = 100;
    private final static String NAME = "bela";
    private final static boolean IS_TRIANGLE = true;
    
    private DataType dt;
    
    public DataTypeTest() {
    }

    @Before
    public void setUp() throws Exception {
        dt = new DataType(DB_ID, NAME, IS_TRIANGLE);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_NullName() {
        dt = new DataType(DB_ID, null, IS_TRIANGLE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EmptyName() {
        dt = new DataType(DB_ID, "", IS_TRIANGLE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_WhitespaceName() {
        dt = new DataType(DB_ID, "   \n\t  ", IS_TRIANGLE);
    }
    
    @Test
    public void testGetDbId() {
        assertEquals(DB_ID, dt.getDbId());
    }

    @Test
    public void testName() {
        assertEquals(NAME, dt.getName());
        
        dt.setName("geza");
        assertEquals("geza", dt.getName());
    }

    @Test(expected=NullPointerException.class)
    public void testSetName_NullName() {
        dt.setName(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_EmptyName() {
        dt.setName("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_WhitespaceName() {
        dt.setName("\n  \t");
    }

    @Test
    public void testTriangle() {
        assertTrue(dt.isTriangle());
        dt.setTriangle(false);
        assertFalse(dt.isTriangle());
        dt.setTriangle(true);
        assertTrue(dt.isTriangle());
    }

    @Test
    public void testEquals() {
        assertTrue(dt.equals(dt));
        
        DataType dt2 = null;
        assertFalse(dt.equals(dt2));
        
        dt2 = new DataType(DB_ID, "geza", !IS_TRIANGLE);
        assertTrue(dt.equals(dt2));
        assertTrue(dt2.equals(dt));
        
        dt2 = new DataType(DB_ID+1, "geza", !IS_TRIANGLE);
        assertFalse(dt.equals(dt2));
        assertFalse(dt2.equals(dt));
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, dt.compareTo(dt));
        
        DataType dt2 = null;
        assertTrue(dt.compareTo(dt2) < 0);
        
        dt2 = new DataType(DB_ID, "geza", !IS_TRIANGLE);
        assertEquals(0, dt.compareTo(dt2));
        assertEquals(0, dt2.compareTo(dt));
        
        dt2 = new DataType(DB_ID+1, "geza", !IS_TRIANGLE);
        assertTrue(dt.compareTo(dt2) < 0);
        assertTrue(dt2.compareTo(dt) > 0);
    }

    @Test
    public void testHashCode() {
        assertEquals(DB_ID, dt.hashCode());
        assertEquals(DB_ID+1, new DataType(DB_ID+1, "geza", !IS_TRIANGLE).hashCode());
    }
}