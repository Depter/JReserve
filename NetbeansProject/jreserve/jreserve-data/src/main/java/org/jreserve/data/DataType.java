package org.jreserve.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name="data-type")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataType implements Comparable<DataType> {
    
    @XmlAttribute(name="dbId", required=true)
    private int dbId;
    @XmlAttribute(name="name", required=true)
    private String name;
    @XmlAttribute(name="isTrinagle", required=true)
    private boolean isTrinagle;
        
    DataType() {}
    
    DataType(int dbId, String name, boolean isTriangle) {
        checkName(name);
        this.dbId = dbId;
        this.name = name;
        this.isTrinagle = isTriangle;
    }
    
    private void checkName(String userName) {
        if(userName == null)
            throw new NullPointerException("Name can not be null!");
        if(userName.trim().length() == 0)
            throw new IllegalArgumentException("Name can not be an empty string!");
    }
    
    public int getDbId() {
        return dbId;
    }
    
    public String getName() {
        return name;
    }
    
    void setName(String name) {
        checkName(name);
        this.name = name;
    }
    
    public boolean isTriangle() {
        return isTrinagle;
    }

    void setTriangle(boolean isTriangle) {
        this.isTrinagle = isTriangle;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof DataType)
            return dbId == ((DataType)o).dbId;
        return false;
    }
    
    @Override
    public int compareTo(DataType o) {
        if(o == null)
            return -1;
        return dbId - o.dbId;
    }
    
    @Override
    public int hashCode() {
        return dbId;
    }
    
    @Override
    public String toString() {
        return String.format("DataType [%d; %s; %s]", dbId, name, isTrinagle);
    }
}
