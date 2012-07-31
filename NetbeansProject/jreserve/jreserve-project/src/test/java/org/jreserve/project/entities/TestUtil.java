package org.jreserve.project.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TestUtil {

    private final static double DELTE = 0.000000001;
    
    private final static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    
    public final static String TEXT_65 = getText(65);
    
    private static String getText(int length) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++)
            sb.append('a');
        return sb.toString();
    }
    
    public static Date getDate(String date) {
        try {
            return DF.parse(date);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Unable to parse date: "+date, ex);
        }
    }
    
    public static void assertEquals(double expected, double found) {
        org.junit.Assert.assertEquals(expected, found, DELTE);
    }
}
