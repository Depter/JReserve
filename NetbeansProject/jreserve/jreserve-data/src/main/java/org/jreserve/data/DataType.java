package org.jreserve.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public enum DataType {

    CLAIM_INCURRED(10),
    CLAIM_PAID(20),
    CLAIM_RESERVE(30),
    CLAIM_COUNT(40),
    PREMIUM(50),
    POLICIES(60),
    OTHER(100);
    
    private final static Map<Integer, DataType> dbMap = new HashMap<Integer, DataType>();
    static {
        for(DataType dt : values()) {
            DataType old = dbMap.put(dt.dbId, dt);
            if(old != null) {
                throw new IllegalStateException(
                        String.format("Db id %d is used by two datatypes: %s and %s", 
                        dt.dbId, old, dt));
            }
        }
    }
    
    private final int dbId;
    
    private DataType(int dbId) {
        this.dbId = dbId;
    }
    
    public int getDbId() {
        return dbId;
    }
    
    public static DataType parse(int dbId) {
        DataType dt = dbMap.get(dbId);
        if(dt != null)
            return dt;
        throw new IllegalArgumentException("Unknown database id: "+dbId);
    }
}
