package org.jreserve.basedata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Entity
@Table(name="LOB", schema="RESERVE")
public class LoB implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.basedata.LoB", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.LOB.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.basedata.LoB")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lob", fetch = FetchType.EAGER)
    private List<ClaimType> claimTypes = new ArrayList<ClaimType>();
    
    public LoB() {
    }
    
    public LoB(String name) {
        this.name = name;
    }
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<ClaimType> getClaimTypes() {
        return claimTypes;
    }
    
    public boolean addClaimType(ClaimType claimType) {
        if(claimType==null || claimTypes.contains(claimType))
            return false;
        claimType.setLoB(this);
        return claimTypes.add(claimType);
    }
    
    public boolean removeClaimType(ClaimType claimType) {
        if(!claimTypes.remove(claimType))
            return false;
        claimType.setLoB(null);
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof LoB))
            return false;
        LoB lob = (LoB) o;
        return id == lob.id;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("LoB [%s; %d]", name, id);
    }
}
