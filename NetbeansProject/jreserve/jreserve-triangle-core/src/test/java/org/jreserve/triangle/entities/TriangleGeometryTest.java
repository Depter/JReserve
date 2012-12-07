package org.jreserve.triangle.entities;

import java.util.Date;
import org.jreserve.triangle.TestUtil;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class TriangleGeometryTest {

    final static Date START = TestUtil.getDate("2000-01-01");
    final static int PERIODS_IN_ACCIDENT = 10;
    final static int MONTH_IN_ACCIDENT = 12;
    private final static int MONTH_IN_DEVELOPMENT = 2;
    private final static int PERIODS_IN_DEVELOPMENT = 2;
    
    private TriangleGeometry geometry;
    
    public TriangleGeometryTest() {
    }

    @Before
    public void setUp() {
        geometry = new TriangleGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT, 
                PERIODS_IN_DEVELOPMENT, MONTH_IN_DEVELOPMENT);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_StartNull() {
        new TriangleGeometry(null, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewAccPeriods() {
        new TriangleGeometry(START, 0, MONTH_IN_ACCIDENT);
    }    
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewAccMonths() {
        new TriangleGeometry(START, PERIODS_IN_ACCIDENT, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewDevPeriods() {
        new TriangleGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT, 0, MONTH_IN_ACCIDENT);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewDevMonths() {
        new TriangleGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT, PERIODS_IN_DEVELOPMENT, 0);
    }

    @Test
    public void testAccidentStart() {
        assertEquals(START, geometry.getStartDate());

        geometry.setStartDate(START);
        assertEquals(START, geometry.getStartDate());
    }

    @Test
    public void testAccidentPeriods() {
        assertEquals(PERIODS_IN_ACCIDENT, geometry.getAccidentPeriods());

        geometry.setAccidentPeriods(PERIODS_IN_ACCIDENT+1);
        assertEquals(PERIODS_IN_ACCIDENT+1, geometry.getAccidentPeriods());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetAccidentPeriods_TooFewo() {
        geometry.setAccidentPeriods(0);
    }

    @Test
    public void testAccidentMonths() {
        assertEquals(MONTH_IN_ACCIDENT, geometry.getAccidentMonths());

        int months = MONTH_IN_ACCIDENT+2;
        geometry.setAccidentMonths(months);
        assertEquals(months, geometry.getAccidentMonths());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetAccidentMonths_TooFewMonths() {
        geometry.setAccidentMonths(0);
    }

    @Test
    public void testDevelopmentPeriods() {
        assertEquals(PERIODS_IN_DEVELOPMENT, geometry.getDevelopmentPeriods());

        geometry.setDevelopmentPeriods(PERIODS_IN_DEVELOPMENT+1);
        assertEquals(PERIODS_IN_DEVELOPMENT+1, geometry.getDevelopmentPeriods());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetDevelopmentPeriods_TooFew() {
        geometry.setDevelopmentPeriods(0);
    }

    @Test
    public void testDevelopmentMonths() {
        assertEquals(MONTH_IN_DEVELOPMENT, geometry.getDevelopmentMonths());

        geometry.setDevelopmentMonths(10);
        assertEquals(10, geometry.getDevelopmentMonths());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetDevelopmentMonths_TooFewMonths() {
        geometry.setDevelopmentMonths(0);
    }

    @Test
    public void testToString() {
        String expected = String.format("Geometry [%tF] [%d; %d] / [%d; %d]",
            START, 
            PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT,
            PERIODS_IN_DEVELOPMENT, MONTH_IN_DEVELOPMENT);
        assertEquals(expected, geometry.toString());
    }
}