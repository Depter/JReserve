package org.jreserve.data.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.jreserve.data.DataType;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="DATA_TYPE", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.data.entities.ProjectDataType",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    pkColumnValue="org.jreserve.data.entities.ProjectDataType"
)
public class ProjectDataType implements Serializable {
    private final static long serialVersionUID = 1L;
    public final static int MAX_NAME_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.data.entities.ProjectDataType")
    @Column(name="ID", nullable=false)
    private long id;
    
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Column(name="DB_ID", nullable=false)
    private int dbId;
    
    @Column(name="NAME", nullable=false, length=MAX_NAME_LENGTH)
    private String name;
    
    @Column(name="IS_TRIANGLE", nullable=false)
    private boolean isTriangle;
    
    protected ProjectDataType() {
    }
    
    public ProjectDataType(Project project, DataType dt) {
        this.project = project;
        this.dbId = dt.getDbId();
        this.name = dt.getName();
        this.isTriangle = dt.isTriangle();
    }
    
    public ProjectDataType(Project project, int dbId, String name, boolean isTriangle) {
        setProject(project);
        this.dbId = dbId;
        checkName(name);
        this.name = name;
        this.isTriangle = isTriangle;
    }
    
    public long getId() {
        return id;
    }
    
    private void setProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    public Project getProject() {
        return project;
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
            return equals((ProjectDataType)o);
        return false;
    }
    
    public boolean equals(ProjectDataType dt) {
        return project.equals(dt.project) &&
               dbId == dt.dbId;
    }
    
    @Override
    public int hashCode() {
        return (int) id;
    }
    
    @Override
    public String toString() {
        return String.format("ProjectDataType [%d; %s; %s]", dbId, name, project);
    }
    
    public String getPath() {
        if(project == null)
            return toString();
        return String.format("%s/%s", project.getPath(), this);
    }
}
