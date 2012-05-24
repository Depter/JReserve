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
    PREMIUM(3, true),
    POLICIES(4, true);
    
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
