package org.jreserve.project.entities.input;

import java.io.Serializable;
import javax.persistence.*;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(entityClass=ClaimType.class)
@Entity
@Table(name="CLAIM_TYPE", schema="JRESERVE")
public class ClaimType implements Serializable {
    private final static long serialVersionUID = 1L;
    private final static int NAME_LENGTH = 64;
    
    @Id
    @Column(name="ID", nullable=false)
    private long id;
    
    @Column(name="LOB_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="LOB_ID")
    private LoB lob;
    
    protected ClaimType() {
    }
    
    public ClaimType(String name, LoB lob) {
        setName(name);
        setLoB(lob);
    }
    
    public void setName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public LoB getLoB() {
        return lob;
    }
    
    void setLoB(LoB lob) {
        this.lob = lob;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof ClaimType)
            return id == ((ClaimType)o).id;
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("ClaimType [%d; %s; %s]", id, name, lob);
    }
}
