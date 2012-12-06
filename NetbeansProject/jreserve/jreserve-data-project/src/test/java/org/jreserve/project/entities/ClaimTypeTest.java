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
public class ClaimTypeTest {
    
    private final static String NAME = "MTPL";
    
    private ClaimType claimType;
    
    public ClaimTypeTest() {
    }

    @Before
    public void setUp() {
        claimType = new ClaimType(NAME);
    }

    @Test
    public void testGetId() {
        assertEquals(0, claimType.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, claimType.getName());
    }

    @Test
    public void testSetName() {
        claimType.setName("bela");
        assertEquals("bela", claimType.getName());
    }

    @Test(expected=NullPointerException.class)
    public void testSetName_Null() {
        claimType.setName(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_EmptyString() {
        claimType.setName("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_TooLong() {
        claimType.setName(TestUtil.TEXT_65);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetName_ExistsInLoB() {
        LoB lob = new LoB("lob");
        lob.addClaimType(claimType);
        lob.addClaimType(new ClaimType("bela"));
        claimType.setName("bela");
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_Null() {
        new ClaimType(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EmptyString() {
        new ClaimType("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooLong() {
        new ClaimType(TestUtil.TEXT_65);
    }

    @Test
    public void testGetLoB() {
        assertEquals(null, claimType.getLoB());
        LoB lob = new LoB("lob");
        lob.addClaimType(claimType);
        assertEquals(lob, claimType.getLoB());
    }

    @Test
    public void testEquals() {
        ClaimType ct1 = new ClaimType(NAME.toUpperCase());
        ClaimType ct2 = new ClaimType(NAME.toLowerCase());
        ClaimType ct3 = new ClaimType(NAME+NAME);
        
        assertFalse(ct1.equals(null));
        assertTrue(ct1.equals(ct2));
        assertFalse(ct1.equals(ct3));
        
        LoB lob1 = new LoB("LoB 1");
        LoB lob2 = new LoB("LoB 2");
        lob1.addClaimType(ct1);
        assertFalse(ct1.equals(ct2));
        
        lob2.addClaimType(ct2);
        assertFalse(ct1.equals(ct2));
        
    }

    @Test
    public void testToString() {
        String expected = String.format("ClaimType [0; %s; %s]", NAME, null);
        assertEquals(expected, claimType.toString());
        
        LoB lob = new LoB("Motor");
        lob.addClaimType(claimType);
        expected = String.format("ClaimType [0; %s; %s]", NAME, lob);
        assertEquals(expected, claimType.toString());
    }

}