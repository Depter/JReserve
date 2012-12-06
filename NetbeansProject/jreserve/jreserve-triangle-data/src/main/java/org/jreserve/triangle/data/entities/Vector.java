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
@Table(name="VECTOR", schema="JRESERVE")
public class Vector extends AbstractPersistentObject implements Serializable, DataStructure {
    
    public final static String GEOMETRY_PROPERTY = "VECTOR_GEOMETRY_PROPERTY";

    public final static int POSITION = 200;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Embedded
    private MetaData meta;
    
    @Embedded
    private VectorGeometry geometry;
    
    protected Vector() {
    }
    
    public Vector(Project project, ProjectDataType dataType, String name) {
        this.meta = new MetaData(dataType, name);
        initProject(project);
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    public VectorGeometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(VectorGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry can not be null!");
        this.geometry = geometry;
    }
    
    @Override
    public String toString() {
        return String.format("Vector [%s]", getName());
    }

    @Override
    public int getPosition() {
        return POSITION;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public ProjectDataType getDataType() {
        return meta.getDataType();
    }

    @Override
    public String getName() {
        return meta.getName();
    }

    public void setName(String name) {
        meta.setName(name);
    }

    public String getDescription() {
        return meta.getDescription();
    }

    public void setDescription(String description) {
        meta.setDescription(description);
    }
}
