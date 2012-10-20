package org.jreserve.project.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Audited
@EntityRegistration
@Entity
@Table(name="CLAIM_TYPE", schema="JRESERVE")
public class ClaimType extends AbstractPersistentObject {
    private final static long serialVersionUID = 1L;
    private final static int NAME_LENGTH = 64;
    
    @Column(name="CLAIM_TYPE_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="LOB_ID", referencedColumnName="ID", nullable=false)
    private LoB lob;
    
    @NotAudited
    @OneToMany(mappedBy="claimType", orphanRemoval=true)
    private List<Project> projects = new ArrayList<Project>();
    
    protected ClaimType() {
    }
    
    public ClaimType(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
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
    public String toString() {
        return String.format("ClaimType [%s]", name);
    }
    
    public String getPath() {
        if(lob == null)
            return toString();
        return String.format("%s/%s", lob, this);
    }
}
