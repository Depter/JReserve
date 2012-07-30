package org.jreserve.project.entities.project;

import javax.persistence.*;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.input.ClaimType;
import org.jreserve.project.entities.input.DataType;
import org.jreserve.project.entities.input.LoB;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractData {
    
    private final static String ERR_CLAIM_TYPE = 
         "Project '%s' belongs to LoB '%s', but the given claim type "
       + "belongs to LoB '%s'!";
    
    private final static int NAME_SIZE = 64;
    
    @Id
    @Column(name="ID")
    private long id;
    
    @ManyToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @ManyToOne
    @JoinColumn(name="CLAIM_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ClaimType claimType;
    
    @ManyToOne
    @JoinColumn(name="DATA_TYPE_ID", referencedColumnName="ID", nullable=false)
    private DataType dataType;
    
    @Column(name="NAME", nullable=false, length=64)
    private String name;
    
    protected AbstractData() {
    }
    
    public AbstractData(Project project, ClaimType claimType, 
            DataType dataType, String name) {
        initProject(project);
        initClaimType(claimType);
        initDataType(dataType);
        initName(name);
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    private void initClaimType(ClaimType claimType) {
        checkClaimType(claimType);
        this.claimType = claimType;
    }
    
    private void checkClaimType(ClaimType claimType) {
        if(claimType == null)
            throw new NullPointerException("ClaimType is null!");
        checkSameLoB(claimType);
    }
    
    private void checkSameLoB(ClaimType claimType) {
        LoB myLoB = project.getLoB();
        LoB claimLoB = claimType.getLoB();
        if(!myLoB.equals(claimLoB))
            String.format(ERR_CLAIM_TYPE, 
                project.getName(), myLoB.getName(), claimLoB.getName());
    }
    
    private void initDataType(DataType dataType) {
        if(dataType == null)
            throw new NullPointerException("DataType is null!");
        this.dataType = dataType;
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_SIZE);
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        initName(name);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof AbstractData)
            return equals((AbstractData) o);
        return false;
    }
    
    private boolean equals(AbstractData o) {
        return project.equals(o.project) &&
               name.equalsIgnoreCase(o.name);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + project.hashCode();
        return 17 * hash + name.toLowerCase().hashCode();
    }
}
