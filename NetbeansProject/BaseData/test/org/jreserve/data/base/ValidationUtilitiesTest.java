package org.jreserve.data.base;

import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ValidationUtilitiesTest {
    
    private static StringBuilder sb = new StringBuilder();
    
    public ValidationUtilitiesTest() {
    }

    @Test
    public void testCheckCalendarMonth() {
        for(short month=0; month<12; month++)
            ValidationUtilities.checkCalendarMonth(month);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCheckCalendarMonth_TooLow() {
        ValidationUtilities.checkCalendarMonth((short)-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCheckCalendarMonth_TooHigh() {
        ValidationUtilities.checkCalendarMonth((short)12);
    }

    @Test
    public void testCheckStringLength64() {
        for(int l=1; l<65; l++) {
            String str = getStirng(l);
            ValidationUtilities.checkStringLength64(str);
        }
    }
    
    private String getStirng(int length) {
        while(sb.length() < length)
            sb.append('a');
        return sb.substring(sb.length()-length);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCheckStringLength64_Short() {
        String str = "";
        ValidationUtilities.checkStringLength64(str);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCheckStringLength64_Long() {
        String str = getStirng(65);
        ValidationUtilities.checkStringLength64(str);
    }

    @Test
    public void testCheckStringLength255() {
        for(int l=1; l<255; l++) {
            String str = getStirng(l);
            ValidationUtilities.checkStringLength255(str);
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCheckStringLength255_Short() {
        String str = "";
        ValidationUtilities.checkStringLength255(str);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCheckStringLength255_Long() {
        String str = getStirng(256);
        ValidationUtilities.checkStringLength255(str);
    }
}