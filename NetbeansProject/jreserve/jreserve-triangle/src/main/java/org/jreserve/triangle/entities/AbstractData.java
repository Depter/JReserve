package org.jreserve.triangle.entities;

import javax.persistence.*;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractData {
    
    private final static int NAME_SIZE = 64;
    
    @ManyToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Column(name="DATA_TYPE_ID", nullable=false)
    private int dataTypeId;
    
    @Column(name="NAME", nullable=false, length=64)
    private String name;
    
    protected AbstractData() {
    }
    
    public AbstractData(Project project,
            DataType dataType, String name) {
        initProject(project);
        initDataType(dataType);
        initName(name);
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    private void initDataType(DataType dataType) {
        if(dataType == null)
            throw new NullPointerException("DataType is null!");
        this.dataTypeId = dataType.getDbId();
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_SIZE);
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public DataType getDataType() {
        return DataTypeUtil.parse(dataTypeId);
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
