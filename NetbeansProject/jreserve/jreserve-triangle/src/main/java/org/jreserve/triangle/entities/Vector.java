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
@Table(name="VECTOR", schema="JRESERVE")
public class Vector extends AbstractPersistentObject implements Serializable, DataStructure {

    public final static int POSITION = 200;
    
    @Embedded
    private MetaData meta;
    
    @Embedded
    private VectorGeometry geometry;
    
    @NotAudited
    @OneToMany(fetch=FetchType.EAGER, mappedBy="vector", cascade=CascadeType.ALL)
    private Set<VectorCorrection> corrections = new HashSet<VectorCorrection>();
    
    protected Vector() {
    }
    
    public Vector(Project project, ProjectDataType dataType, String name) {
        meta = new MetaData(project, dataType, name);
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
        return meta.getProject();
    }

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
