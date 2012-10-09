package org.jreserve.project.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(name="LOB", schema="JRESERVE")
public class LoB extends AbstractPersistentObject {
    private final static long serialVersionUID = 1L;

    private final static int NAME_LENGTH = 64;
    
    @Column(name="LOB_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @OneToMany(mappedBy="lob", orphanRemoval=true)
    private List<ClaimType> claimTypes = new ArrayList<ClaimType>();
    
    protected LoB() {
    }
    
    public LoB(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    protected List<ClaimType> getRealClaimTypes() {
        return claimTypes;
    }
    
    public List<ClaimType> getClaimTypes() {
        return new ArrayList<ClaimType>(claimTypes);
    }
    
    public boolean addClaimType(ClaimType claimType) {
        if(containsClaimType(claimType.getName()))
            return false;
        claimType.setLoB(this);
        claimTypes.add(claimType);
        return true;
    }
    
    boolean containsClaimType(String ctName) {
        for(ClaimType ct : claimTypes)
            if(ct.getName().equalsIgnoreCase(ctName))
                return true;
        return false;
    }
    
    public boolean removeClaimType(ClaimType claimType) {
        if(!claimTypes.contains(claimType) || !this.equals(claimType.getLoB()))
            return false;
        claimTypes.remove(claimType);
        claimType.setLoB(null);
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("LoB [%s]", name);
    }
    
    public String getPath() {
        return toString();
    }
}
