package org.decsi.jreserve;

import org.junit.Assert;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TestUtil {
    
    private final static double EPSILON = 0.0000000001;
    
    public static void assertEquals(double expected, double found) {
        Assert.assertEquals(expected, found, EPSILON);
    }
}
