package org.jreserve.project.entities;

import java.util.Date;
import org.jreserve.project.TestUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ClaimDataTest {

    private final static String LOB = "Motor";
    private final static String CLAIM_TYPE = "MTPL";
    private final static String DATA_TYPE = "Paid";
    
    private final static LoB lob = new LoB(LOB);
    private final static ClaimType claimType = new ClaimType(CLAIM_TYPE);
    private final static DataType dataType = new DataType(DATA_TYPE);
    private final static Date ACCIDENT = TestUtil.getDate("2011-01-01");
    private final static Date DEVLOPMENT = TestUtil.getDate("2011-04-01");
    
    private ClaimData claimData;
    
    public ClaimDataTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        lob.addClaimType(claimType);
    }

    @Before
    public void setUp() {
        claimData = new ClaimData(claimType, dataType, ACCIDENT, DEVLOPMENT);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_ClaimTypeNull() {
        new ClaimData(null, dataType, ACCIDENT, DEVLOPMENT);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_DataTypeNull() {
        new ClaimData(claimType, null, ACCIDENT, DEVLOPMENT);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_AccidentNull() {
        new ClaimData(claimType, dataType, null, DEVLOPMENT);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_DevelopmentNull() {
        new ClaimData(claimType, dataType, ACCIDENT, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_DevelopmentBeforeAccident() {
        new ClaimData(claimType, dataType, DEVLOPMENT, ACCIDENT);
    }

    @Test
    public void testConstructor_DevelopmentAtAccident() {
        new ClaimData(claimType, dataType, ACCIDENT, ACCIDENT);
    }
    
    @Test
    public void testGetClaimType() {
        assertEquals(claimType, claimData.getClaimType());
    }

    @Test
    public void testGetDataType() {
        assertEquals(dataType, claimData.getDataType());
    }

    @Test
    public void testGetAccidentDate() {
        assertEquals(ACCIDENT, claimData.getAccidentDate());
    }

    @Test
    public void testGetDevelopmentDate() {
        assertEquals(DEVLOPMENT, claimData.getDevelopmentDate());
    }

    @Test
    public void testGetClaimValue() {
        TestUtil.assertEquals(0d, claimData.getClaimValue());
        double expected = 13.42;
        claimData.setClaimValue(expected);
        TestUtil.assertEquals(expected, claimData.getClaimValue());
    }

    @Test
    public void testEquals() {
        assertFalse(claimData.equals(null));
        claimData.setClaimValue(10d);
        
        ClaimData cd2 = new ClaimData(claimType, dataType, ACCIDENT, DEVLOPMENT);
        assertTrue(claimData.equals(cd2));
        
        cd2 = new ClaimData(claimType, dataType, ACCIDENT, ACCIDENT);
        assertFalse(claimData.equals(cd2));
        
        cd2 = new ClaimData(claimType, dataType, DEVLOPMENT, DEVLOPMENT);
        assertFalse(claimData.equals(cd2));
        
        cd2 = new ClaimData(claimType, new DataType("Incurred"), ACCIDENT, DEVLOPMENT);
        assertFalse(claimData.equals(cd2));
        
        ClaimType ct2 = new ClaimType("CASCO");
        lob.addClaimType(ct2);
        cd2 = new ClaimData(ct2, dataType, ACCIDENT, DEVLOPMENT);
        assertFalse(claimData.equals(cd2));
        
        LoB lob2 = new LoB("Fire");
        ct2 = new ClaimType(claimType.getName());
        lob2.addClaimType(ct2);
        cd2 = new ClaimData(ct2, dataType, ACCIDENT, DEVLOPMENT);
        assertFalse(claimData.equals(cd2));
    }

    @Test
    public void testToString() {
        String expected = String.format(
            "ClaimData [%s; %s; %tF; %tF; %f]",
            claimType.getName(), dataType.getName(),
            ACCIDENT, DEVLOPMENT, 0d);
        assertEquals(expected, claimData.toString());
    }

}