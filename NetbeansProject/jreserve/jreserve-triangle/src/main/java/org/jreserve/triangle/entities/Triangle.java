package org.jreserve.triangle.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.jreserve.data.DataType;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration(generateId=true)
@Entity
@Table(name="TRIANGLE", schema="JRESERVE")
@TableGenerator(
    name="org.jreserve.triangle.entities.Triangle",
    catalog=EntityRegistration.CATALOG,
    schema=EntityRegistration.SCHEMA,
    table=EntityRegistration.TABLE,
    pkColumnName=EntityRegistration.ID_COLUMN,
    valueColumnName=EntityRegistration.VALUE_COLUMN,
    initialValue=EntityRegistration.INITIAL_VALUE,
    allocationSize=EntityRegistration.ALLOCATION_SIZE,
    pkColumnValue="org.jreserve.triangle.entities.Triangle"
)
public class Triangle extends AbstractData implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="org.jreserve.triangle.entities.Triangle")
    @Column(name="ID")
    private long id;
    
    @Embedded
    private TriangleGeometry geometry;
    
    @OneToMany(mappedBy="triangle")
    private List<TriangleComment> comments = new ArrayList<TriangleComment>();
    
    @OneToMany(mappedBy="triangle")
    private List<TriangleCorrection> corrections = new ArrayList<TriangleCorrection>();
    
    protected Triangle() {
    }
    
    public Triangle(Project project, DataType dataType, String name) {
        super(project, dataType, name);
    }

    public long getId() {
        return id;
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
