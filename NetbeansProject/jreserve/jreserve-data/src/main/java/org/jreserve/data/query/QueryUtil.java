package org.jreserve.data.query;

import org.jreserve.data.entities.ClaimValue;
import org.jreserve.data.DataComment;
import org.jreserve.data.entities.DataCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class QueryUtil {
    
    static String getEntityName(Class clazz) {
        if(ClaimValue.class.equals(clazz))
            return "ClaimValue";
        else if(DataCorrection.class.equals(clazz))
            return "DataCorrection";
        else if(DataComment.class.equals(clazz))
            return "DataComment";
        throw new IllegalArgumentException("Unknown class: "+clazz);
    }
}
