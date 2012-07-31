package org.jreserve.project.entities.project;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.jreserve.project.entities.project.VectorGeometryTest.*;

/**
 *
 * @author Peter Decsi
 */
public class TriangleGeometryTest {

    private final static int MONTH_IN_DEVELOPMENT = 2;
    private TriangleGeometry geometry;
    
    public TriangleGeometryTest() {
    }

    @Before
    public void setUp() {
        geometry = new TriangleGeometry(START, 
                END, MONTH_IN_ACCIDENT, MONTH_IN_DEVELOPMENT);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewMonths() {
        new TriangleGeometry(START, END, MONTH_IN_ACCIDENT, 0);
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
    public void testToString() {
        String expected = String.format("Geometry [%tF - %tF]: %d/%d",
            START, END, 
            MONTH_IN_ACCIDENT, MONTH_IN_DEVELOPMENT);
        assertEquals(expected, geometry.toString());
    }

}