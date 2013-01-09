package org.jreserve.triangle.data.util;

import java.util.Date;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.data.TestUtil;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class TriangleInputTest {

    private final static Date[] ACCIDENTS = {
        TestUtil.getDate("2001-01-01"),
        TestUtil.getDate("2002-01-01"),
        TestUtil.getDate("2003-01-01")
    };

    private final static Date[][] DEVELOPMENTS = {
        {TestUtil.getDate("2001-01-01"), TestUtil.getDate("2001-07-01"), TestUtil.getDate("2002-01-01"), TestUtil.getDate("2002-07-01"), TestUtil.getDate("2003-01-01")},
        {TestUtil.getDate("2002-01-01"), TestUtil.getDate("2002-07-01"), TestUtil.getDate("2003-01-01")},
        {TestUtil.getDate("2003-01-01")}
    };
    
    private final static double[][] DATA = {
        {1, 2, 3, 4, 5},
        {6, 7, 8},
        {9}
    };
    
    private TriangularData data; 
    
    public TriangleInputTest() {
    }

    @Before
    public void setUp() {
        data = new TriangleInput(ACCIDENTS, DEVELOPMENTS, DATA);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(3, data.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(5, data.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        int devCount = 5;
        for(int accident=0; accident<3; accident++) {
            assertEquals(devCount, data.getDevelopmentCount(accident));
            devCount -= 2;
        }
    }

    @Test
    public void testGetAccidentName() {
        for(int accident=0; accident<3; accident++)
            assertEquals(ACCIDENTS[accident], data.getAccidentName(accident));
    }

    @Test
    public void testGetDevelopmentName() {
        for(int accident=0; accident<data.getAccidentCount(); accident++) {
            int devCount = data.getDevelopmentCount(accident);
            for(int development=0; development<devCount; development++)
                assertEquals(DEVELOPMENTS[accident][development], data.getDevelopmentName(accident, development));
        }
    }

    @Test
    public void testGetValue() {
        for(int accident=0; accident<data.getAccidentCount(); accident++) {
            int devCount = data.getDevelopmentCount(accident);
            for(int development=0; development<devCount; development++)
                TestUtil.assertEquals(DATA[accident][development], data.getValue(accident, development));
        }
    }

    @Test
    public void testToArray() {
        double[][] found = data.toArray();
        
        assertEquals(DATA.length, found.length);
        for(int a=0; a<DATA.length; a++) {
            assertEquals(DATA[a].length, found[a].length);
            for(int d=0; d<DATA[a].length; d++)
                TestUtil.assertEquals(DATA[a][d], found[a][d]);
        }
    }

    @Test
    public void testToString() {
        String found = "TriangleInput ["+DATA.length+"; "+DATA[0].length+"]";
        assertEquals(found, data.toString());
    }

    @Test
    public void testGetLayerTypeId() {
        for(int accident=0; accident<data.getAccidentCount(); accident++) {
            int devCount = data.getDevelopmentCount(accident);
            for(int development=0; development<devCount; development++)
                assertEquals(TriangleInput.LAYER_TYPE_ID, data.getLayerTypeId(accident, development));
        }
    }
}