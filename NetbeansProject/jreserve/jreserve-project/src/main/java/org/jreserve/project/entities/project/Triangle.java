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
@EntityRegistration(entityClass=Triangle.class)
@Entity
@Table(name="TRIANGLE", schema="JRESERVE")
public class Triangle extends AbstractData implements Serializable {
    
    @Embedded
    private TriangleGeometry geometry;
    
    @OneToMany(mappedBy="triangle")
    private List<TriangleComment> comments = new ArrayList<TriangleComment>();
    
    @OneToMany(mappedBy="triangle")
    private List<TriangleCorrection> corrections = new ArrayList<TriangleCorrection>();
    
    protected Triangle() {
    }
    
    public Triangle(Project project, ClaimType claimType, DataType dataType, String name) {
        super(project, claimType, dataType, name);
    }
    
    public TriangleGeometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry can not be null!");
        this.geometry = geometry;
    }
    
    public List<TriangleComment> getComments() {
        return new ArrayList<TriangleComment>(comments);
    }
    
    public void addComment(TriangleComment comment) {
        if(comments.contains(comment))
            return;
        comments.add(comment);
        comment.setTriangle(this);
    }
    
    public void removeComment(TriangleComment comment) {
        if(comments.remove(comment))
            comment.setTriangle(null);
    }
    
    public List<TriangleCorrection> getCorrections() {
        return new ArrayList<TriangleCorrection>(corrections);
    }
    
    public void addCorrection(TriangleCorrection correction) {
        if(corrections.contains(correction))
            return;
        corrections.add(correction);
        correction.setTriangle(this);
    }
    
    public void removeCorrection(TriangleCorrection correction) {
        if(corrections.remove(correction))
            correction.setTriangle(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Triangle)
            return super.equals(o);
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("Triangle [%s; %s;]",
                getName(), getProject().getName());
    }
}
