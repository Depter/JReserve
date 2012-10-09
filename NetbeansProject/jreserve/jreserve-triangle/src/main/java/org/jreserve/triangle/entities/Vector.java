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
@EntityRegistration
@Entity
@Table(name="VECTOR", schema="JRESERVE")
public class Vector extends AbstractData implements Serializable {
    
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
    public String toString() {
        return String.format("Vector [%s]", getName());
    }
}
