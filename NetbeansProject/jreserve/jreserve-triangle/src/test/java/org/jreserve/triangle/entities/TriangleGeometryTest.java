package org.jreserve.triangle.entities;

import org.jreserve.triangle.entities.TriangleGeometry;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.jreserve.triangle.entities.VectorGeometryTest.*;

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
                START, PERIODS_IN_DEVELOPMENT, MONTH_IN_DEVELOPMENT);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewMonths() {
        new TriangleGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT, START, PERIODS_IN_DEVELOPMENT, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewPeriods() {
        new TriangleGeometry(START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT, START, 0, MONTH_IN_ACCIDENT);
    }

    @Test
    public void testGetMonthInDevelopment() {
        assertEquals(MONTH_IN_DEVELOPMENT, geometry.getMonthInDevelopment());
    }

    @Test
    public void testSetMonthInDevelopment() {
        geometry.setMonthInDevelopment(10);
        assertEquals(10, geometry.getMonthInDevelopment());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetMonthInDevelopment_TooFewMonths() {
        geometry.setMonthInDevelopment(0);
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
    public void testGetDevelopmentStart() {
        assertEquals(START, geometry.getDevelopmentStart());
    }

    @Test
    public void testSetDevelopmentStart() {
        geometry.setDevelopmentStart(START);
        assertEquals(START, geometry.getDevelopmentStart());
    }

    @Test
    public void testToString() {
        String expected = String.format("Geometry [%tF; %d; %d] / [%tF; %d; %d]",
            START, PERIODS_IN_ACCIDENT, MONTH_IN_ACCIDENT,
            START, PERIODS_IN_DEVELOPMENT, MONTH_IN_DEVELOPMENT);
        assertEquals(expected, geometry.toString());
    }

}