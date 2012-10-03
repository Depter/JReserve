package org.jreserve.data;

import java.io.Serializable;
import javax.persistence.*;
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
            return compareTo((ProjectDataType)o) == 0;
        return false;
    }
    
    public int compareTo(ProjectDataType o) {
        if(o == null) return -1;
        int dif = compareProject(project);
        if(dif != 0) return dif;
        return dbId - o.dbId;
    }
    
    private int compareProject(Project o) {
        if(project == null)
            return o==null? 0 : 1;
        return o==null? -1 : (int) (project.getId() - o.getId());
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + (project==null? 0 : project.hashCode());
        return 17 * hash + dbId;
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
