package org.jreserve.data.settings;

import org.jreserve.data.DataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DTDummy implements Comparable<DTDummy> {

    private int id;
    private String name;
    private boolean isTriangle;

    public DTDummy() {
    }

    public DTDummy(DataType dt) {
        this.id = dt.getDbId();
        this.name = dt.getName();
        this.isTriangle = dt.isTriangle();
    }

    public DTDummy(Object[] row) {
        this.id = (Integer) row[DataTypeTableModel.ID_COLUMN];
        this.name = (String) row[DataTypeTableModel.NAME_COLUMN];
        this.isTriangle = (Boolean) row[DataTypeTableModel.TRIANGLE_COLUMN];
    }
    
    public DTDummy(int id, String name, boolean isTriangle) {
        this.id = id;
        this.name = name;
        this.isTriangle = isTriangle;
    }

    @Override
    public int compareTo(DTDummy o) {
        if(o == null)
            return -1;
        return id - o.id;
    }

    public int getId() {
        return id;
    }
    
    void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    void setName(String name) {
        this.name = name;
    } 

    public boolean isTriangle() {
        return isTriangle;
    }
    
    void setTriangle(boolean isTriangle) {
        this.isTriangle = isTriangle;
    } 
}
