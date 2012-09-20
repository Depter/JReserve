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
@Table(name="CLAIM_TYPE", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.project.entities.ClaimType",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    allocationSize=EntityRegistration.ALLOCATION_SIZE,
    initialValue=EntityRegistration.INITIAL_VALUE,
    pkColumnValue="org.jreserve.project.entities.ClaimType"
)
public class ClaimType implements Serializable {
    private final static long serialVersionUID = 1L;
    private final static int NAME_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.project.entities.ClaimType")
    @Column(name="ID", nullable=false)
    private long id;
    
    @Column(name="CLAIM_TYPE_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="LOB_ID", referencedColumnName="ID", nullable=false)
    private LoB lob;
    
    @OneToMany(mappedBy="claimType", orphanRemoval=true)
    private List<Project> projects = new ArrayList<Project>();
    
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
    
    public List<Project> getProjects() {
        return new ArrayList<Project>(projects);
    }
    
    public boolean addProject(Project project) {
        if(containsProject(project.getName()))
            return false;
        project.setClaimType(this);
        projects.add(project);
        return true;
    }
    
    boolean containsProject(String projectName) {
        for(Project p : projects)
            if(p.getName().equalsIgnoreCase(projectName))
                return true;
        return false;
    }
    
    public boolean removeProject(Project project) {
        if(!projects.contains(project) || !this.equals(project.getClaimType()))
            return false;
        projects.remove(project);
        project.setClaimType(null);
        return true;
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
        return String.format("ClaimType [%d; %s]", id, name);
    }
    
    public String getPath() {
        if(lob == null)
            return toString();
        return String.format("%s/%s", lob, this);
    }
}
