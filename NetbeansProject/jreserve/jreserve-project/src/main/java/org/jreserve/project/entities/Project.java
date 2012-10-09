package org.jreserve.project.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Type;
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
@Table(name="PROJECT", schema="JRESERVE")
public class Project extends AbstractPersistentObject {
    private final static long serialVersionUID = 1L;

    private final static int NAME_LENGTH = 64;
    
    @ManyToOne(fetch= FetchType.LAZY, optional=false)
    @JoinColumn(name="CLAIM_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ClaimType claimType;
    
    @Column(name="PROJECT_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @Column(name="PROJECT_DESCRIPTION")
    @Type(type="org.hibernate.type.TextType")
    private String description;
    
    protected Project() {
    }
    
    public Project(String name) {
        initName(name);
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public ClaimType getClaimType() {
        return claimType;
    }
    
    void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        initName(name);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("Project [%s]", name);
    }
    
    public String getPath() {
        if(claimType == null)
            return toString();
        return String.format("%s/%s", claimType.getPath(), this);
    }
}
