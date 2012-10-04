package org.jreserve.triangle.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="VECTOR", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.triangle.entities.Vector",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    initialValue=EntityRegistration.INITIAL_VALUE,
    allocationSize=EntityRegistration.ALLOCATION_SIZE,
    pkColumnValue="org.jreserve.triangle.entities.Vector"
)
public class Vector extends AbstractData implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.triangle.entities.Vector")
    @Column(name="ID")
    private long id;
    
    @Embedded
    private VectorGeometry geometry;
    
    @OneToMany(mappedBy="vector")
    private List<VectorComment> comments = new ArrayList<VectorComment>();
    
    @OneToMany(mappedBy="vector")
    private List<VectorCorrection> corrections = new ArrayList<VectorCorrection>();
    
    protected Vector() {
    }
    
    public Vector(Project project, ProjectDataType dataType, String name) {
        super(project, dataType, name);
    }

    public long getId() {
        return id;
    }
    
    public VectorGeometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(VectorGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry can not be null!");
        this.geometry = geometry;
    }
    
    public List<VectorComment> getComments() {
        return new ArrayList<VectorComment>(comments);
    }
    
    public void addComment(VectorComment comment) {
        if(comments.contains(comment))
            return;
        comments.add(comment);
        comment.setVector(this);
    }
    
    public void removeComment(VectorComment comment) {
        if(comments.remove(comment))
            comment.setVector(null);
    }
    
    public List<VectorCorrection> getCorrections() {
        return new ArrayList<VectorCorrection>(corrections);
    }
    
    public void addCorrection(VectorCorrection correction) {
        if(corrections.contains(correction))
            return;
        corrections.add(correction);
        correction.setVector(this);
    }
    
    public void removeCorrection(VectorCorrection correction) {
        if(corrections.remove(correction))
            correction.setVector(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Vector)
            return super.equals(o);
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("Vector [%s; %s;]",
                getName(), getProject().getName());
    }
}
