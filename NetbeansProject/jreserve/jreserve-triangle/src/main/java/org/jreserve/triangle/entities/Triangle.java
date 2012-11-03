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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="TRIANGLE", schema="JRESERVE")
public class Triangle extends AbstractPersistentObject implements Serializable, DataStructure {

    public final static int POSITION = 100;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @Embedded
    private MetaData meta;
    
    @Embedded
    private TriangleGeometry geometry;

    @NotAudited
    @OneToMany(fetch=FetchType.EAGER, mappedBy="triangle", orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<TriangleCorrection> corrections = new HashSet<TriangleCorrection>();
    
    @NotAudited
    @OneToMany(fetch=FetchType.EAGER, mappedBy="triangle", orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<TriangleComment> comments = new HashSet<TriangleComment>();
    
    protected Triangle() {
    }
    
    public Triangle(Project project, ProjectDataType dataType, String name) {
        this.meta = new MetaData(dataType, name);
        initProject(project);
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }
    
    public TriangleGeometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry can not be null!");
        this.geometry = geometry;
    }
    
    public List<TriangleCorrection> getCorrections() {
        return new ArrayList<TriangleCorrection>(corrections);
    }
    
    public void setCorrections(List<TriangleCorrection> corrections) {
        if(corrections != null)
            checkMyCorrections(corrections);
        this.corrections.clear();
        if(corrections != null)
            this.corrections.addAll(corrections);
    }
    
    private void checkMyCorrections(List<TriangleCorrection> corrections) {
        for(TriangleCorrection correction : corrections)
            if(!equals(correction.getTriangle()))
                throwOtherTriangleException(correction);
    }
    
    private void throwOtherTriangleException(TriangleCorrection correction) {
        String msg = "Correction belongs to another triangle '%s' instead of '%s'!";
        msg = String.format(msg, this, correction.getTriangle());
        throw new IllegalArgumentException(msg);
    }
    
    public List<TriangleComment> getComments() {
        return new ArrayList<TriangleComment>(comments);
    }
    
    public void setComments(List<TriangleComment> comments) {
        if(comments != null)
            checkMyComments(comments);
        this.comments.clear();
        if(comments != null)
            this.comments.addAll(comments);
    }
    
    private void checkMyComments(List<TriangleComment> comments) {
        for(TriangleComment comment : comments)
            if(!equals(comment.getTriangle()))
                throwOtherTriangleException(comment);
    }
    
    private void throwOtherTriangleException(TriangleComment comment) {
        String msg = "Comment belongs to another triangle '%s' instead of '%s'!";
        msg = String.format(msg, this, comment.getTriangle());
        throw new IllegalArgumentException(msg);
    }
    
    public void addComment(TriangleComment comment) {
        if(!equals(comment.getTriangle()))
            throwOtherTriangleException(comment);
        this.comments.add(comment);
    }
    
    @Override
    public String toString() {
        return String.format("Triangle [%s;]", getName());
    }

    @Override
    public int getPosition() {
        return POSITION;
    }

    @Override
    public String getName() {
        return meta.getName();
    }

    public void setName(String name) {
        meta.setName(name);
    }

    public void setDescription(String description) {
        meta.setDescription(description);
    }

    public String getDescription() {
        return meta.getDescription();
    }

    @Override
    public ProjectDataType getDataType() {
        return meta.getDataType();
    }

    @Override
    public Project getProject() {
        return project;
    }
}
