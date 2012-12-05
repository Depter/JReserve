package org.jreserve.data;

import org.jreserve.project.entities.ClaimType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ProjectDataTypeTest {

    private final static String CT_NAME = "ClaimType";
    private final static ClaimType CT = new ClaimType(CT_NAME);
    private final static int DB_ID = 100;
    private final static String NAME = "bela";
    private final static boolean IS_TRIANGLE = true;

    private ProjectDataType pdt;
    
    public ProjectDataTypeTest() {
    }

    @Before
    public void setUp() throws Exception {
        pdt = new ProjectDataType(CT, DB_ID, NAME, IS_TRIANGLE);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_NullName() {
        pdt = new ProjectDataType(CT, DB_ID, null, IS_TRIANGLE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EmptyName() {
        pdt = new ProjectDataType(CT, DB_ID, "", IS_TRIANGLE);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_WhitespaceName() {
        pdt = new ProjectDataType(CT, DB_ID, " \n  \t  ", IS_TRIANGLE);
    }
    
    @Test
    public void testGetClaimType() {
        assertEquals(CT_NAME, pdt.getClaimType().getName());
    }

    @Test
    public void testGetDbId() {
        assertEquals(DB_ID, pdt.getDbId());
    }

    @Test
    public void testName() {
        assertEquals(NAME, pdt.getName());
        
        pdt.setName("geza");
        assertEquals("geza", pdt.getName());
    }

    @Test(expected=NullPointerException.class)
    public void testSetName_NullName() {
        pdt.setName(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_EmptyName() {
        pdt.setName("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_WhitespaceName() {
        pdt.setName("\n  \t");
    }

    @Test
    public void testTriangle() {
        assertTrue(pdt.isTriangle());
        pdt.setTriangle(false);
        assertFalse(pdt.isTriangle());
        pdt.setTriangle(true);
        assertTrue(pdt.isTriangle());
    }

    @Test
    public void testEquals() {
        assertTrue(pdt.equals(pdt));
        
        ProjectDataType pdt2 = null;
        assertFalse(pdt.equals(pdt2));
        
        pdt2 = new ProjectDataType(new ClaimType("ct"), DB_ID, "geza", !IS_TRIANGLE);
        assertTrue(pdt.equals(pdt2));
        assertTrue(pdt2.equals(pdt));
        
        pdt2 = new ProjectDataType(CT, DB_ID+1, "geza", !IS_TRIANGLE);
        assertFalse(pdt.equals(pdt2));
        assertFalse(pdt2.equals(pdt));
    }

    @Test
    public void testCompareTo() {
        assertTrue(pdt.compareTo(pdt) == 0);
        
        ProjectDataType pdt2 = null;
        assertTrue(pdt.compareTo(pdt2) < 0);
        
        pdt2 = new ProjectDataType(new ClaimType("ct"), DB_ID, "geza", !IS_TRIANGLE);
        assertTrue(pdt.compareTo(pdt2) == 0);
        assertTrue(pdt2.compareTo(pdt) == 0);
        
        pdt2 = new ProjectDataType(CT, DB_ID+1, "geza", !IS_TRIANGLE);
        assertTrue(pdt.compareTo(pdt2) < 0);
        assertTrue(pdt2.compareTo(pdt) > 0);
    }
}