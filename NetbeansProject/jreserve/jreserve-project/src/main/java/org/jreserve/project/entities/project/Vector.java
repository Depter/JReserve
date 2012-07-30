package org.jreserve.project.entities.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.input.ClaimType;
import org.jreserve.project.entities.input.DataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(entityClass=Vector.class)
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
    
    public Vector(Project project, ClaimType claimType, DataType dataType, String name) {
        super(project, claimType, dataType, name);
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
