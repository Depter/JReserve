package org.jreserve.triangle.data.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.jreserve.triangle.data.TestUtil;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 */
public class GeometryUtilTest {

    private final static int START_YEAR = 2001;
    private final static String START_DATE = ""+START_YEAR+"-01-01";
    private final static int A_PERIODS = 4;
    private final static int A_MONTHS = 12;
    private final static int D_PERIODS = 13;
    private final static int D_MONTHS = 3;
    
    private GeometryUtil util;
    private TriangleGeometry geometry;

    public GeometryUtilTest() {
    }

    @Before
    public void setUp() {
        util = new GeometryUtil();
        geometry = new TriangleGeometry(TestUtil.getDate(START_DATE), A_PERIODS, A_MONTHS, D_PERIODS, D_MONTHS);
    }

    @Test(expected=IllegalStateException.class)
    public void testGetEDTInstance_NotEDT() {
        GeometryUtil.getEDTInstance();
    }

    @Test
    public void testGetAccidentBegin() {
        int year = START_YEAR;
        for(int i=0; i<A_PERIODS; i++) {
            Date expected = TestUtil.getDate(""+(year++)+"-01-01");
            Date found = util.getAccidentBegin(geometry, i);
            assertEquals(expected, found);
        }
    }

    @Test
    public void testGetAccidentEnd() {
        int year = START_YEAR+1;
        for(int i=0; i<A_PERIODS; i++) {
            Date expected = TestUtil.getDate(""+(year++)+"-01-01");
            Date found = util.getAccidentEnd(geometry, i);
            assertEquals(expected, found);
        }
    }

    @Test
    public void testGetDevelopmentBegin() {
        Calendar calendar = GregorianCalendar.getInstance();
        for(int accident=0; accident<A_PERIODS; accident++) {
            Date date = util.getAccidentBegin(geometry, accident);
            calendar.setTime(date);
            int lastDev = D_PERIODS - accident*4;
            for(int dev=0; dev<=lastDev; dev++) {
                assertEquals(date, util.getDevelopmentBegin(geometry, accident, dev));
                
                calendar.add(Calendar.MONTH, D_MONTHS);
                date = calendar.getTime();
            }
        }
    }

    @Test
    public void testGetDevelopmentEnd() {
        Calendar calendar = GregorianCalendar.getInstance();
        for(int accident=0; accident<A_PERIODS; accident++) {
            Date date = util.getAccidentBegin(geometry, accident);
            calendar.setTime(date);
            int lastDev = D_PERIODS - accident*4;
            for(int dev=0; dev<=lastDev; dev++) {
                calendar.add(Calendar.MONTH, D_MONTHS);
                date = calendar.getTime();
                
                assertEquals(date, util.getDevelopmentEnd(geometry, accident, dev));
            }
        }
    }

}