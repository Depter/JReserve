package org.jreserve.data;

import javax.persistence.*;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(name="DATA_TYPE", schema="JRESERVE")
public class ProjectDataType extends AbstractPersistentObject {
    private final static long serialVersionUID = 1L;
    public final static int MAX_NAME_LENGTH = 64;
    
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="CLAIM_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ClaimType claimType;
    
    @Column(name="DB_ID", nullable=false)
    private int dbId;
    
    @Column(name="NAME", nullable=false, length=MAX_NAME_LENGTH)
    private String name;
    
    @Column(name="IS_TRIANGLE", nullable=false)
    private boolean isTriangle;
    
    protected ProjectDataType() {
    }
    
    public ProjectDataType(ClaimType claimType, DataType dt) {
        this.claimType = claimType;
        this.dbId = dt.getDbId();
        this.name = dt.getName();
        this.isTriangle = dt.isTriangle();
    }
    
    public ProjectDataType(ClaimType claimType, int dbId, String name, boolean isTriangle) {
        setClaimType(claimType);
        this.dbId = dbId;
        checkName(name);
        this.name = name;
        this.isTriangle = isTriangle;
    }
    
    private void setClaimType(ClaimType claimType) {
        if(claimType == null)
            throw new NullPointerException("ClaimType is null!");
        this.claimType = claimType;
    }
    
    public ClaimType getClaimType() {
        return claimType;
    }
    
    public int getDbId() {
        return dbId;
    }
    
    public void setName(String name) {
        checkName(name);
        this.name = name;
    }
    
    private void checkName(String name) {
        if(name == null)
            throw new NullPointerException("Name is null!");
        if(name.trim().length() == 0)
            throw new IllegalArgumentException("Name is empty!");
        if(name.length() > MAX_NAME_LENGTH)
            throw new IllegalArgumentException("Name is longer than: "+MAX_NAME_LENGTH);
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isTriangle() {
        return isTriangle;
    }
    
    public void setTriangle(boolean isTriangle) {
        this.isTriangle = isTriangle;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof ProjectDataType)
            return compareTo((ProjectDataType)o) == 0;
        return false;
    }
    
    public int compareTo(ProjectDataType o) {
        if(o == null) return -1;
        return dbId - o.dbId;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + (claimType==null? 0 : claimType.hashCode());
        return 17 * hash + dbId;
    }
    
    @Override
    public String toString() {
        return String.format("ProjectDataType [%d; %s; %s]", dbId, name, claimType);
    }
    
    public String getPath() {
        if(claimType == null)
            return toString();
        return String.format("%s/%s", claimType.getPath(), this);
    }
}
