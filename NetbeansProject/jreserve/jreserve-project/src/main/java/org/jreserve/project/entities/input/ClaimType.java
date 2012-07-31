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
@EntityRegistration(generateId=true)
@Entity
@Table(name="CLAIM_TYPE", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.project.entities.input.ClaimType",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    pkColumnValue="org.jreserve.project.entities.input.ClaimType"
)
public class ClaimType implements Serializable {
    private final static long serialVersionUID = 1L;
    private final static int NAME_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.project.entities.input.ClaimType")
    @Column(name="ID", nullable=false)
    private long id;
    
    @Column(name="LOB_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="LOB_ID")
    private LoB lob;
    
    protected ClaimType() {
    }
    
    public ClaimType(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public long getId() {
        return id;
    }
    
    public void setName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        checkNameInLob(name);
        this.name = name;
    }
    
    private void checkNameInLob(String name) {
        if(lob == null || !lob.containsClaimType(name)) return;
        String msg = "Name '%s' is already used in LoB '%s'";
        msg = String.format(msg, name, lob.getName());
        throw new IllegalArgumentException(msg);
    }
    
    private boolean nameUsedInLoB(String name) {
        for(ClaimType ct : lob.getRealClaimTypes())
            if(ct.getName().equalsIgnoreCase(name))
                return true;
        return false;
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
            return equals((ClaimType)o);
        return false;
    }
    
    private boolean equals(ClaimType o) {
        return equals(o.lob) &&
               name.equalsIgnoreCase(o.name);
    }
    
    private boolean equals(LoB otherLoB) {
        if(lob == null)
            return otherLoB == null;
        return lob.equals(otherLoB);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ClaimType [%d; %s; %s]", id, name, lob);
    }
}