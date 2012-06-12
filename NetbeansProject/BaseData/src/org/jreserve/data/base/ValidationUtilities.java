package org.jreserve.data.base;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ValidationUtilities {
    
    final static int VARCHAR_64 = 64;
    final static int VARCHAR_255 = 255;
    
    private final static short FIRST_MONTH = 0;
    private final static short LAST_MONTH = 11;
    
    static void checkCalendarMonth(short month) {
        if(month >= FIRST_MONTH && month <= LAST_MONTH)
            return;
        String msg = "Month %d is not within [%d,%d]";
        msg = String.format(msg, month, FIRST_MONTH, LAST_MONTH);
        throw new IllegalArgumentException(msg);
    }
    
    static void checkStringLength64(String name) {
        checkStringLength(name, VARCHAR_64);
    }
    
    static void checkStringLength(String name, int maxLength) {
        int length = name.length();
        if(0 < length && length <= maxLength)
            return;
        throw nameLengthException(name, maxLength);
    }
    
    private static IllegalArgumentException nameLengthException(String name, int maxLength) {
        String msg = "Name '%s' was not within bounds [1, %d]!";
        msg = String.format(msg, name, maxLength);
        return new IllegalArgumentException(msg);
    }
    
    static void checkStringLength255(String name) {
        checkStringLength(name, VARCHAR_255);
    }
}
