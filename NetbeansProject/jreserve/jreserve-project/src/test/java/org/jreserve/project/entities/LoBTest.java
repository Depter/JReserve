package org.jreserve.project.entities;

import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.TestUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class LoBTest {

    private final static String NAME = "Motor";
    
    private LoB lob;
    
    public LoBTest() {
    }

    @Before
    public void setUp() {
        lob = new LoB(NAME);
    }

    @Test
    public void testGetId() {
        assertEquals(0, lob.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, lob.getName());
    }

    @Test
    public void testSetName() {
        lob.setName("bela");
        assertEquals("bela", lob.getName());
    }

    @Test(expected=NullPointerException.class)
    public void testSetName_Null() {
        lob.setName(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_EmptyString() {
        lob.setName("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_TooLong() {
        lob.setName(TestUtil.TEXT_65);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_Null() {
        new LoB(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EmptyString() {
        new LoB("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooLong() {
        new LoB(TestUtil.TEXT_65);
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddClaimType_Null() {
        lob.addClaimType(null);
    }
    
    @Test
    public void testClaimType() {
        assertTrue(lob.getClaimTypes().isEmpty());
        ClaimType ct = new ClaimType("MTPL");
        
        lob.addClaimType(ct);
        assertEquals(1, lob.getClaimTypes().size());
        assertTrue(lob == ct.getLoB());
        
        ClaimType ct2 = new ClaimType("mtpl");
        lob.addClaimType(ct2);
        assertEquals(1, lob.getClaimTypes().size());
        assertTrue(ct2.getLoB() == null);
        
        lob.removeClaimType(ct2);
        assertEquals(1, lob.getClaimTypes().size());
        
        lob.removeClaimType(ct);
        assertTrue(lob.getClaimTypes().isEmpty());
        assertEquals(null, ct.getLoB());
    }

    @Test
    public void testEquals() {
        LoB lob1 = new LoB(NAME.toUpperCase());
        LoB lob2 = new LoB(NAME.toLowerCase());
        LoB lob3 = new LoB(NAME+NAME);
        
        assertFalse(lob1.equals(null));
        assertTrue(lob1.equals(lob2));
        assertFalse(lob1.equals(lob3));
    }

    @Test
    public void testToString() {
        String expected = String.format("LoB [0; %s]", NAME);
        assertEquals(expected, lob.toString());
    }

}