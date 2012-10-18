package org.jreserve.triangle.entities;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Entity
@Table(name="VECTOR", schema="JRESERVE")
public class Vector extends AbstractData implements Serializable {

    public final static int POSITION = 200;
    
    @Embedded
    private VectorGeometry geometry;
    
    protected Vector() {
    }
    
    public Vector(Project project, ProjectDataType dataType, String name) {
        super(project, dataType, name);
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
}
