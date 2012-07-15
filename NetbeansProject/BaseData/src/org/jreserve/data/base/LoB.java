package org.jreserve.data.base;

import java.util.List;

/**
 * @author Peter Decsi
 * @version 1.0
 */
public interface LoB {
    
    public String getName();
    
    public void setName(String name);
    
    public List<ClaimType> getClaimTypes();
    
    public boolean addClaimType(ClaimType claimType);
    
    public boolean removeClaimType(ClaimType claimType);
}
