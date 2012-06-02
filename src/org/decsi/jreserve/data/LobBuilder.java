package org.decsi.jreserve.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LobBuilder extends AbstractNamedDataBuilder<LoB> {
    
    private List<ClaimType> claimTypes = new ArrayList<ClaimType>();
    
    public LobBuilder() {
    }

    public void addClaimType(ClaimType claimType) {
        for(ClaimType ct : claimTypes)
            checkClaimType(ct, claimType);
        claimTypes.add(claimType);
    }
    
    private void checkClaimType(ClaimType c1, ClaimType c2) {
        checkClaimTypeId(c1, c2);
        checkClaimTypeName(c1, c2);
        checkClaimTypeShortName(c1, c2);
    }
    
    private void checkClaimTypeId(ClaimType c1, ClaimType c2) {
        if(c1.getId() != c2.getId())
            return;
        String msg = String.format("ClaimType id '%d' already used!", c1.getId());
        throw new IllegalArgumentException(msg);
    }
    
    private void checkClaimTypeName(ClaimType c1, ClaimType c2) {
        if(!c1.getName().equalsIgnoreCase(c2.getName()))
            return;
        String msg = String.format("ClaimType name '%s' already used!", c1.getName());
        throw new IllegalArgumentException(msg);
    }
    
    private void checkClaimTypeShortName(ClaimType c1, ClaimType c2) {
        if(!c1.getShortName().equalsIgnoreCase(c2.getShortName()))
            return;
        String msg = String.format("ClaimType short name '%s' already used!", c1.getShortName());
        throw new IllegalArgumentException(msg);
    }
    
    @Override
    public LoB build() {
        checkState();
        LoB lob = new LoB(id, shortName, name);
        addClaimTypes(lob);
        return lob;
    }
    
    private void addClaimTypes(LoB lob) {
        for(ClaimType claimType : claimTypes)
            lob.addClaimType(claimType);
    }
    
    @Override
    protected void clear() {
        super.clear();
        claimTypes.clear();
    }
    
}
