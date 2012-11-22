package org.jreserve.triangle.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.project.entities.Project;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;

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
    public final static String CORRECTION_PROPERTY = "VECTOR_CORRECTION_PROPERTY";
    public final static String COMMENT_PROPERTY = "VECTOR_COMMENT_PROPERTY";
    public final static String SMOOTHING_PROPERTY = "VECTOR_SMOOTHING_PROPERTY";

    public final static int POSITION = 200;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Embedded
    private MetaData meta;
    
    @Embedded
    private VectorGeometry geometry;
    

    @NotAudited
    @OneToMany(cascade= CascadeType.ALL)
    @JoinTable(
        name="VECTOR_CORRECTIONS",
        schema="JRESERVE",
        joinColumns=@JoinColumn(name="VECTOR_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF),
        inverseJoinColumns=@JoinColumn(name="CORRECTION_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    )
    private Set<TriangleCorrection> corrections = new HashSet<TriangleCorrection>();
    
    @NotAudited
    @OneToMany(cascade= CascadeType.ALL)
    @JoinTable(
        name="VECTOR_SMOOTHING",
        schema="JRESERVE",
        joinColumns=@JoinColumn(name="VECTOR_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF),
        inverseJoinColumns=@JoinColumn(name="SMOOTHING_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    )
    private Set<Smoothing> smoothings = new HashSet<Smoothing>();

    @NotAudited
    @OneToMany(cascade= CascadeType.ALL)
    @JoinTable(
        name="VECTOR_COMMENTS",
        schema="JRESERVE",
        joinColumns=@JoinColumn(name="TRIANGLE_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF),
        inverseJoinColumns=@JoinColumn(name="COMMENT_ID", columnDefinition=AbstractPersistentObject.COLUMN_DEF)
    )
    private Set<TriangleComment> comments = new HashSet<TriangleComment>();
    
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
    
    public List<TriangleCorrection> getCorrections() {
        return new ArrayList<TriangleCorrection>(corrections);
    }
    
    public void setCorrections(List<TriangleCorrection> corrections) {
        this.corrections.clear();
        if(corrections != null)
            this.corrections.addAll(corrections);
    }
    
    public List<TriangleComment> getComments() {
        return new ArrayList<TriangleComment>(comments);
    }
    
    public void setComments(List<TriangleComment> comments) {
        this.comments.clear();
        if(comments != null)
            this.comments.addAll(comments);
    }
    
    public List<Smoothing> getSmoothings() {
        return new ArrayList<Smoothing>(smoothings);
    }
    
    public void setSmoothings(List<Smoothing> smoothings) {
        this.smoothings.clear();
        if(smoothings != null)
            this.smoothings.addAll(smoothings);
    }
    
    public void addSmoothing(Smoothing smoothing) {
        this.smoothings.add(smoothing);
    }
    
    public int getMaxSmoothingOrder() {
        int order = 0;
        for(Smoothing smoothing : smoothings)
            if(smoothing.getOrder() > order)
                order = smoothing.getOrder();
        return order;
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
