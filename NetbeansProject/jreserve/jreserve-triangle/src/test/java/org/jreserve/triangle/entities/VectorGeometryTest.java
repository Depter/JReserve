package org.jreserve.triangle.entities;

import java.util.Date;
import org.jreserve.triangle.TestUtil;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class VectorGeometryTest {

    final static Date START = TestUtil.getDate("2000-01-01");
    final static int PERIODS_IN_ACCIDENT = 10;
    final static int MONTH_IN_ACCIDENT = 12;
    
    private VectorGeometry geometry;
    
    public VectorGeometryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        geometry = new VectorGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewMonths() {
        new VectorGeometry(START, PERIODS_IN_ACCIDENT, 0);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_StartNull() {
        new VectorGeometry(null, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewPeriods() {
        new VectorGeometry(START, 0, MONTH_IN_ACCIDENT);
    }    

    @Test
    public void testGetAccidentPeriods() {
        assertEquals(PERIODS_IN_ACCIDENT, geometry.getAccidentPeriods());
    }

    @Test
    public void testSetAccidentPeriods() {
        geometry.setAccidentPeriods(PERIODS_IN_ACCIDENT+1);
        assertEquals(PERIODS_IN_ACCIDENT+1, geometry.getAccidentPeriods());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetAccidentPeriods_TooFewo() {
        geometry.setAccidentPeriods(0);
    }

    @Test
    public void testGetAccidentStart() {
        assertEquals(START, geometry.getAccidentStart());
    }

    @Test
    public void testSetAccidentStart() {
        geometry.setAccidentStart(START);
        assertEquals(START, geometry.getAccidentStart());
    }

    @Test
    public void testGetMonthInAccident() {
        assertEquals(MONTH_IN_ACCIDENT, geometry.getMonthInAccident());
    }

    @Test
    public void testSetMonthInAccident() {
        int months = MONTH_IN_ACCIDENT+2;
        geometry.setMonthInAccident(months);
        assertEquals(months, geometry.getMonthInAccident());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetMonthInAccident_TooFewMonths() {
        geometry.setMonthInAccident(0);
    }

    @Test
    public void testToString() {
        String expected = String.format("Geometry [%tF; %d; %d]",
            START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT);
        assertEquals(expected, geometry.toString());
    }

}