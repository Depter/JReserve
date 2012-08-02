package org.jreserve.persistence;


/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PersistenceUtil {

    private final static String ERR_VARCHAR_LONG = 
            "Varchar %s can not be longer then %d characters!";
    private final static String ERR_VARCHAR_SHORT = 
            "Varchar must be at least on character long!";
    
    public static void checkVarchar(String value, int length) {
        if(value==null)
            throw new NullPointerException("Varchar is not nullable!");
        if(value.length() == 0)
            throw new IllegalArgumentException(ERR_VARCHAR_SHORT);
        checkNullableVarchar(value, length);
    }
    
    public static void checkNullableVarchar(String value, int length) {
        if(value == null || value.length() <= length)
            return;
        throw new IllegalArgumentException(
            String.format(ERR_VARCHAR_LONG, value, length));
    }
}
