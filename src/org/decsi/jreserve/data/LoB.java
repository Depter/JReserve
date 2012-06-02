package org.decsi.jreserve.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoB {
    private final int id;
    private final String shortName;
    private final String name;
    private final List<ClaimType> claimTypes = new ArrayList<ClaimType>();
    
    LoB(int id, String shortName, String name) {
        this.id = id;
        this.shortName = shortName;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getName() {
        return name;
    }
    
    void addClaimType(ClaimType claimType) {
        this.claimTypes.add(claimType);
    }
    
    public List<ClaimType> getClaimTypes() {
        return new ArrayList<ClaimType>(claimTypes);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof LoB)
            return equals((LoB) o);
        return false;
    }
    
    public boolean equals(LoB lob) {
        if(lob == null)
            return false;
        return id == lob.id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("LoB [%d; %s]",
                id, shortName);
    }
}
