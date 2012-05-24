package org.decsi.jreserve.data;

/**
 * A ClaimType represents a special claim type within a line of busniess, such as 
 * bodily injury or material damages.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimType implements Comparable<ClaimType> {
    
    private int id;
    private String shortName;
    private String name;
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    @Override
    public int compareTo(ClaimType ct) {
        if(ct == null)
            return -1;
        return id - ct.id;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof ClaimType)
            return compareTo((ClaimType)o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("ClaimType [%d, %s]", id, shortName);
    }
}
