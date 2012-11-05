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

    public final static int POSITION = 200;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Embedded
    private MetaData meta;
    
    @Embedded
    private VectorGeometry geometry;
    
    @NotAudited
    @OneToMany(fetch=FetchType.EAGER, mappedBy="vector", orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<VectorCorrection> corrections = new HashSet<VectorCorrection>();
    
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
    @OneToMany(fetch=FetchType.EAGER, mappedBy="vector", orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<VectorComment> comments = new HashSet<VectorComment>();
    
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
    
    public List<VectorCorrection> getCorrections() {
        return new ArrayList<VectorCorrection>(corrections);
    }
    
    public void setCorrections(List<VectorCorrection> corrections) {
        if(corrections != null)
            checkMyCorrections(corrections);
        this.corrections.clear();
        if(corrections != null)
            this.corrections.addAll(corrections);
    }
    
    private void checkMyCorrections(List<VectorCorrection> corrections) {
        for(VectorCorrection correction : corrections)
            if(!equals(correction.getVector()))
                throwOtherTriangleException(correction);
    }
    
    private void throwOtherTriangleException(VectorCorrection correction) {
        String msg = "Correction belongs to another vector '%s' instead of '%s'!";
        msg = String.format(msg, this, correction.getVector());
        throw new IllegalArgumentException(msg);
    }
    
    public List<VectorComment> getComments() {
        return new ArrayList<VectorComment>(comments);
    }
    
    public void setComments(List<VectorComment> comments) {
        if(comments != null)
            checkMyComments(comments);
        this.comments.clear();
        if(comments != null)
            this.comments.addAll(comments);
    }
    
    private void checkMyComments(List<VectorComment> comments) {
        for(VectorComment comment : comments)
            if(!equals(comment.getVector()))
                throwOtherTriangleException(comment);
    }
    
    private void throwOtherTriangleException(VectorComment comments) {
        String msg = "Comment belongs to another vector '%s' instead of '%s'!";
        msg = String.format(msg, this, comments.getVector());
        throw new IllegalArgumentException(msg);
    }
    
    public List<Smoothing> getSmoothings() {
        return new ArrayList<Smoothing>(smoothings);
    }
    
    public void setSmoothings(List<Smoothing> smoothings) {
        if(smoothings != null)
            checkMySmoothings(smoothings);
        this.smoothings.clear();
        if(smoothings != null)
            this.smoothings.addAll(smoothings);
    }
    
    private void checkMySmoothings(List<Smoothing> smoothings) {
        for(Smoothing smoothing : smoothings)
            if(!getId().equals(smoothing.getOwner()))
                throwOtherVectorException(smoothing);
    }
    
    private void throwOtherVectorException(Smoothing smoothing) {
        String msg = "Smoothing belongs to another vector '%s' instead of '%s'!";
        msg = String.format(msg, this, smoothing.getOwner());
        throw new IllegalArgumentException(msg);
    }
    
    public void addSmoothing(Smoothing smoothing) {
        if(!getId().equals(smoothing.getOwner()))
            throwOtherVectorException(smoothing);
        this.smoothings.add(smoothing);
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
