package org.decsi.jreserve.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public enum DataType {
    
    PAID(0, true),
    INCURED(1, true),
    CLAIM_NUMBER(2, true),
    CASE_LOSS(3, true),
    PREMIUM(4, false),
    POLICIES(5, false);
    
    private boolean isTriangle;
    private int id;
    
    private DataType(int id, boolean isTriangle) {
        this.id = id;
        this.isTriangle = isTriangle;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean isTriangle() {
        return isTriangle;
    }
}
