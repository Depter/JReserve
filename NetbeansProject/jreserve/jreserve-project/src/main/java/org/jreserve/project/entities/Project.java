package org.jreserve.project.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="PROJECT", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.project.entities.Project",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    pkColumnValue="org.jreserve.project.entities.Project"
)
public class Project implements Serializable {
    private final static long serialVersionUID = 1L;

    private final static int NAME_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.project.entities.Project")
    @Column(name="ID")
    private long id;
    
    @ManyToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name="CLAIM_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ClaimType claimType;
    
    @Column(name="PROJECT_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @Column(name="PROJECT_DESCRIPTION")
    @Type(type="org.hibernate.type.TextType")
    private String description;
    
    @OneToMany(mappedBy="project", orphanRemoval=true, cascade=CascadeType.ALL)
    private List<ChangeLog> changes = new ArrayList<ChangeLog>();
    
    protected Project() {
    }
    
    public Project(ClaimType claimType, String name) {
        initClaimType(claimType);
        initName(name);
    }
    
    private void initClaimType(ClaimType claimType) {
        if(claimType == null)
            throw new NullPointerException("ClaimType is null!");
        this.claimType = claimType;
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }
    
    public long getId() {
        return id;
    }
    
    public ClaimType getClaimType() {
        return claimType;
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
    
    public List<ChangeLog> getChanges() {
        return new ArrayList<ChangeLog>(changes);
    }
    
    public void addChange(ChangeLog change) {
        change.setProject(this);
        changes.add(change);
    } 
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Project)
            return equals((Project) o);
        return false;
    }
    
    private boolean equals(Project o) {
        return claimType.equals(o.claimType) &&
               name.equalsIgnoreCase(o.name);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + claimType.hashCode();
        return 17 * hash + name.toLowerCase().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Project [%s; %s]", name, claimType.getName());
    }
}