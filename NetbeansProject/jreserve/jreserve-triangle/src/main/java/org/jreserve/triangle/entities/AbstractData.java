package org.jreserve.triangle.entities;

import javax.persistence.*;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractData extends AbstractPersistentObject {
    
    private final static int NAME_SIZE = 64;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DATA_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ProjectDataType dataType;
    
    @Column(name="NAME", nullable=false, length=64)
    private String name;
    
    protected AbstractData() {
    }
    
    public AbstractData(Project project,
            ProjectDataType dataType, String name) {
        initProject(project);
        initDataType(dataType);
        initName(name);
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    private void initDataType(ProjectDataType dataType) {
        if(dataType == null)
            throw new NullPointerException("DataType is null!");
        this.dataType = dataType;
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_SIZE);
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public ProjectDataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        initName(name);
    }
}
