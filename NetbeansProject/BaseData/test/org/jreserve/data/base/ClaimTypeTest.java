package org.jreserve.data.base;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTypeTest {
    
    private final static String NAME = "CT";
    private final static String LONG_NAME = "Claim Type";
    
    private ClaimType ct;
    
    public ClaimTypeTest() {
    }

    @Before
    public void setUp() {
        ct = new ClaimType(NAME, LONG_NAME);
    }

    @Test
    public void testGetId() {
        assertEquals(0, ct.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, ct.getName());
        ct.setName("Bela");
        assertEquals("Bela", ct.getName());
    }

    @Test
    public void testGetLongName() {
        assertEquals(LONG_NAME, ct.getLongName());
        ct.setLongName("Bela");
        assertEquals("Bela", ct.getLongName());
    }

    @Test
    public void testGetLob() {
        assertEquals(null, ct.getLob());
        Lob lob = new Lob("LoB", "LoB");
        ct.setLob(lob);
        assertEquals(lob, ct.getLob());
    }

    @Test
    public void testToString() {
        String found = ct.toString();
        String expected = "ClaimType ["+NAME+"]";
        assertEquals(expected, found);
    }

}