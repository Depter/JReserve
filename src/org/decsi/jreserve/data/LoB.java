package org.decsi.jreserve.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An LoB represents a line of business within the company.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class LoB implements Comparable<LoB> {
    
    private int id;
    private String name;
    private String shortName;
    private List<ClaimType> claimTypes = new ArrayList<>();
    
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
    
    public List<ClaimType> getClaimTypes() {
        return claimTypes;
    }
    
    public void addClaimType(ClaimType claimType) {
        if(claimTypes.contains(claimType))
           return;
        claimTypes.add(claimType);
        Collections.sort(claimTypes);
    }
    
    @Override
    public int compareTo(LoB lob) {
        if(lob == null)
            return -1;
        return id - lob.id;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof LoB)
            return compareTo((LoB)o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("LoB [%d, %s]", id, shortName);
    }
}
