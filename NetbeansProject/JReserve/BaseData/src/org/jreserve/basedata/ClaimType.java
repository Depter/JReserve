package org.jreserve.basedata;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimType implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name="org.jreserve.basedata.ClaimType", 
                    table="SEQUENCE_STORE",
                    schema="RESERVE",
                    pkColumnName="SEQUENCE_NAME", 
                    pkColumnValue="RESERVE.CLAIM_TYPE.ID", 
                    valueColumnName="SEQUENCE_VALUE", initialValue = 1, allocationSize = 1 )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.basedata.ClaimType")
    private long id;
    
    @Column(name = "NAME", nullable = false)
    @Size(min=1, max=64, message="invalid.name")
    private String name;
    
    @JoinColumn(name = "LOB_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private LoB lob;
    
    public ClaimType() {
    }
    
    public ClaimType(String name) {
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
    
    void setLoB(LoB lob) {
        this.lob = lob;
    }
    
    public LoB getLoB() {
        return lob;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ClaimType))
            return false;
        ClaimType ct = (ClaimType) o;
        return id == ct.id;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("ClaimType [%s; %d]", name, id);
    }
}
