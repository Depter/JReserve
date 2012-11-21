package org.jreserve.triangle.entities;

import static org.jreserve.triangle.entities.VectorGeometryTest.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class TriangleGeometryTest {

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
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewMonths() {
        new TriangleGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT, PERIODS_IN_DEVELOPMENT, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewPeriods() {
        new TriangleGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT, 0, MONTH_IN_ACCIDENT);
    }

    @Test
    public void testGetMonthInDevelopment() {
        assertEquals(MONTH_IN_DEVELOPMENT, geometry.getDevelopmentMonths());
    }

    @Test
    public void testSetMonthInDevelopment() {
        geometry.setDevelopmentMonths(10);
        assertEquals(10, geometry.getDevelopmentMonths());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetMonthInDevelopment_TooFewMonths() {
        geometry.setDevelopmentMonths(0);
    }

    @Test
    public void testGetDevelopmentPeriods() {
        assertEquals(PERIODS_IN_DEVELOPMENT, geometry.getDevelopmentPeriods());
    }

    @Test
    public void testSetDevelopmentPeriods() {
        geometry.setDevelopmentPeriods(PERIODS_IN_DEVELOPMENT+1);
        assertEquals(PERIODS_IN_DEVELOPMENT+1, geometry.getDevelopmentPeriods());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetDevelopmentPeriods_TooFew() {
        geometry.setDevelopmentPeriods(0);
    }

    @Test
    public void testToString() {
        String expected = String.format("Geometry [%tF; %d; %d] / [%tF; %d; %d]",
            START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT,
            START, PERIODS_IN_DEVELOPMENT, MONTH_IN_DEVELOPMENT);
        assertEquals(expected, geometry.toString());
    }

}