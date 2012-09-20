package org.jreserve.project.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="LOB", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.project.entities.LoB",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    allocationSize=EntityRegistration.ALLOCATION_SIZE,
    initialValue=EntityRegistration.INITIAL_VALUE,
    pkColumnValue="org.jreserve.project.entities.LoB"
)
public class LoB implements Serializable {
    private final static long serialVersionUID = 1L;

    private final static int NAME_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.project.entities.LoB")
    @Column(name="ID", nullable=false)
    private long id;
    
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
    public boolean equals(Object o) {
        if(o instanceof LoB)
            return name.equalsIgnoreCase(((LoB)o).name);
        return false;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("LoB [%d; %s]", id, name);
    }
    
    public String getPath() {
        return toString();
    }
}
