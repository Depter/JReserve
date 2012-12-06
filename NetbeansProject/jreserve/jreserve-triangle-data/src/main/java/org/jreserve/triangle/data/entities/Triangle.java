package org.jreserve.triangle.data.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="TRIANGLE", schema="JRESERVE")
public class Triangle extends AbstractPersistentObject implements Serializable, DataStructure {
    
    public final static String GEOMETRY_PROPERTY = "TRIANGLE_GEOMETRY_PROPERTY";

    public final static int POSITION = 100;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Embedded
    private MetaData meta;
    
    @Embedded
    private TriangleGeometry geometry;
    
    protected Triangle() {
    }
    
    public Triangle(Project project, ProjectDataType dataType, String name) {
        this.meta = new MetaData(dataType, name);
        initProject(project);
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    public TriangleGeometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry can not be null!");
        this.geometry = geometry;
    }
    
    @Override
    public String toString() {
        return String.format("Triangle [%s;]", getName());
    }

    @Override
    public int getPosition() {
        return POSITION;
    }

    @Override
    public String getName() {
        return meta.getName();
    }

    public void setName(String name) {
        meta.setName(name);
    }

    public void setDescription(String description) {
        meta.setDescription(description);
    }

    public String getDescription() {
        return meta.getDescription();
    }

    @Override
    public ProjectDataType getDataType() {
        return meta.getDataType();
    }

    @Override
    public Project getProject() {
        return project;
    }
}
