package org.jreserve.project.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(entityClass=LoB.class)
@Entity
@Table(name="LOB", schema="JRESERVE")
public class LoB implements Serializable {
    private final static long serialVersionUID = 1L;

    private final static int NAME_LENGTH = 64;
    
    @Id
    @Column(name="ID", nullable=false)
    private long id;
    
    @Column(name="LOB_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @OneToMany(mappedBy="lob")
    private List<ClaimType> claimTypes = new ArrayList<ClaimType>();
    
    protected LoB() {
    }
    
    public LoB(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public List<ClaimType> getClaimTypes() {
        return new ArrayList<ClaimType>(claimTypes);
    }
    
    public void addClaimType(ClaimType claimType) {
        if(claimTypes.contains(claimType))
            return;
        claimType.setLoB(this);
        claimTypes.add(claimType);
    }
    
    public void removeClaimType(ClaimType claimType) {
        if(!claimTypes.contains(claimType))
            return;
        claimTypes.remove(claimType);
        claimType.setLoB(null);
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof LoB)
            return id == ((LoB)o).id;
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("LoB [%d; %s]", id, name);
    }
}
