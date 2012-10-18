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
@Table(name="TRIANGLE", schema="JRESERVE")
public class Triangle extends AbstractData implements Serializable {

    public final static int POSITION = 100;
    
    @Embedded
    private TriangleGeometry geometry;
    
    protected Triangle() {
    }
    
    public Triangle(Project project, ProjectDataType dataType, String name) {
        super(project, dataType, name);
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
}
