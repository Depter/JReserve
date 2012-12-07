package org.jreserve.triangle.entities;

import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.util.ProjectData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="TRIANGLE", schema="JRESERVE")
public class Triangle extends AbstractPersistentObject implements ProjectData {
    
    public final static String GEOMETRY_PROPERTY = "TRIANGLE_GEOMETRY_PROPERTY";
    public final static int TRIANGLE_POSITION = 100;
    public final static int VECTOR_POSITION = 200;
    private final static int NAME_SIZE = 64;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DATA_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ProjectDataType dataType;
    
    @Column(name="NAME", nullable=false, length=NAME_SIZE)
    private String name;
    
    @Column(name="DESCRIPTION")
    private String description;
    
    @Column(name="IS_TRIANGLE", nullable=false)
    private boolean isTriangle;
    
    @Embedded
    private TriangleGeometry geometry;
    
    protected Triangle() {
    }
    
    public Triangle(Project project, ProjectDataType dataType, String name, boolean isTriangle) {
        initDataType(dataType);
        initName(name);
        initProject(project);
        this.isTriangle = isTriangle;
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
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }

    @Override
    public int getPosition() {
        return isTriangle? TRIANGLE_POSITION : VECTOR_POSITION;
    }

    @Override
    public Project getProject() {
        return project;
    }

    public ProjectDataType getDataType() {
        return dataType;
    }

    @Override
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
    
    public boolean isTriangle() {
        return isTriangle;
    }
    
    public TriangleGeometry getGeometry() {
        return geometry==null? null : geometry.copy();
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        checkGeometry(geometry);
        this.geometry = geometry.copy();
    }
    
    private void checkGeometry(TriangleGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry can not be null!");
        if(!isTriangle && geometry.getDevelopmentPeriods()>1) {
            String msg = "Can not set geometry with more than 1 development periods (%d) to a vector!";
            throw new IllegalArgumentException(String.format(msg, geometry.getDevelopmentPeriods()));
        }
    }
    
    @Override
    public String toString() {
        return String.format("Triangle [%s;]", getName());
    }
}
