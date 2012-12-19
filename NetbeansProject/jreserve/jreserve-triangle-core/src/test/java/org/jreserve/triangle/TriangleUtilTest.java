package org.jreserve.triangle;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class TriangleUtilTest {

    private final static double[][] DECUMMULATED = {
        {653, 56, 7 , 2 , 3, 8, 0, 0},
        {538, 46, 12, 2 , 6, 1, 1},
        {613, 43, 9 , 11, 0, 1},
        {676, 86, 31, 3 , 0},
        {767, 92, 9 , 2},
        {760, 73, 5},
        {789, 67},
        {896}
    };
    
    private final static double[][] CUMMULATED = {
        {653, 709, 716, 718, 721, 729, 729, 729},
        {538, 584, 596, 598, 604, 605, 606},
        {613, 656, 665, 676, 676, 677},
        {676, 762, 793, 796, 796},
        {767, 859, 868, 870},
        {760, 833, 838},
        {789, 856},
        {896}							
    };
    
    private static double[][] copyData(double[][] data) {
        double[][] copy = new double[data.length][];
        for(int i=0; i<data.length; i++) {
            int size = data[i].length;
            copy[i] = new double[size];
            System.arraycopy(data[i], 0, copy[i], 0, size);
        }
            
        return copy;
    }
    
    public TriangleUtilTest() {
    }

    @Test
    public void testCummulate() {
        double[][] data = copyData(DECUMMULATED);
        TriangleUtil.cummulate(data);
        testEquals(CUMMULATED, data);
    }

    private void testEquals(double[][] expected, double[][] found) {
        assertEquals(expected.length, found.length);
        
        for(int i=0; i<expected.length; i++) {
            assertEquals(expected[i].length, found[i].length);
            for(int d=0; d<expected[i].length; d++)
                TestUtil.assertEquals(expected[i][d], found[i][d]);
        }
    }

    @Test
    public void testDeCummulate() {
        double[][] data = copyData(CUMMULATED);
        TriangleUtil.deCummulate(data);
        testEquals(DECUMMULATED, data);
    }
}