package org.jreserve.project.entities.project;

import java.util.Date;
import org.jreserve.project.entities.TestUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class VectorGeometryTest {

    final static Date START = TestUtil.getDate("2000-01-01");
    final static Date END = TestUtil.getDate("2010-01-01");
    final static int MONTH_IN_ACCIDENT = 12;
    
    private VectorGeometry geometry;
    
    public VectorGeometryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        geometry = new VectorGeometry(START, END, MONTH_IN_ACCIDENT);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_TooFewMonths() {
        new VectorGeometry(START, END, 0);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_StartNull() {
        new VectorGeometry(null, END, MONTH_IN_ACCIDENT);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_EndNull() {
        new VectorGeometry(START, null, MONTH_IN_ACCIDENT);
    }    
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EndBeforeStart() {
        new VectorGeometry(END, START, MONTH_IN_ACCIDENT);
    }

    @Test
    public void testGetEndDate() {
        assertEquals(END, geometry.getEndDate());
    }

    @Test
    public void testSetEndDate() {
        geometry.setEndDate(START);
        assertEquals(START, geometry.getEndDate());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetEndDate_BeforeStart() {
        geometry.setEndDate(TestUtil.getDate("1999-12-31"));
    }

    @Test
    public void testGetStartDate() {
        assertEquals(START, geometry.getStartDate());
    }

    @Test
    public void testSetStartDate() {
        geometry.setStartDate(END);
        assertEquals(END, geometry.getEndDate());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetStartDate_AfterEnd() {
        geometry.setStartDate(TestUtil.getDate("2010-01-02"));
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
        String expected = String.format("Geometry [%tF - %tF]: %d",
            START, END, MONTH_IN_ACCIDENT);
        assertEquals(expected, geometry.toString());
    }

}