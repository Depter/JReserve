package org.jreserve.data;

import java.io.Serializable;
import javax.persistence.*;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="DATA_TYPE", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.data.ProjectDataType",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    initialValue=EntityRegistration.INITIAL_VALUE,
    allocationSize=EntityRegistration.ALLOCATION_SIZE,
    pkColumnValue="org.jreserve.data.ProjectDataType"
)
public class ProjectDataType implements Serializable {
    private final static long serialVersionUID = 1L;
    public final static int MAX_NAME_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.ProjectDataType")
    @Column(name="ID", nullable=false)
    private long id;
    
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
    
    public long getId() {
        return id;
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
