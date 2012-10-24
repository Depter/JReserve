package org.jreserve.data.query;

import org.jreserve.data.entities.ClaimValue;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class QueryUtil {
    
    static String getEntityName(Class clazz) {
        if(ClaimValue.class.equals(clazz))
            return "ClaimValue";
        
        throw new IllegalArgumentException("Unknown class: "+clazz);
    }
}
