package org.jreserve.data.base;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LobTest {
    
    private final static String NAME = "LoB";
    private final static String LONG_NAME = "Line of Business";
    
    private Lob lob;
    private ClaimType ct; 
    
    public LobTest() {
    }

    @Before
    public void setUp() {
        lob = new Lob(NAME, LONG_NAME);
        ct = new ClaimType("CT", "Claim Type");
    }

    @Test
    public void testGetId() {
        assertEquals(0, lob.getId());
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, lob.getName());
        lob.setName("Bela");
        assertEquals("Bela", lob.getName());
    }

    @Test
    public void testGetLongName() {
        assertEquals(LONG_NAME, lob.getLongName());
        lob.setLongName("Bela");
        assertEquals("Bela", lob.getLongName());
    }

    @Test
    public void testGetClaimTypes() {
        assertTrue(lob.getClaimTypes().isEmpty());
    }

    @Test
    public void testAddClaimType() {
        lob.addClaimType(ct);
        assertEquals(1, lob.getClaimTypes().size());
        assertEquals(lob, ct.getLob());
    }

    @Test
    public void testRemoveClaimType() {
        lob.addClaimType(ct);
        lob.removeClaimType(ct);
        assertTrue(lob.getClaimTypes().isEmpty());
        assertEquals(null, ct.getLob());
    }

    @Test
    public void testToString() {
        String expected = "Lob ["+NAME+"]";
        assertEquals(expected, lob.toString());
    }

}