package org.jreserve.triangle.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.data.ProjectData;
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
    
    private MetaData meta;
    
    @Embedded
    private TriangleGeometry geometry;

    @NotAudited
    @OneToMany(fetch=FetchType.EAGER, mappedBy="triangle", orphanRemoval=true, cascade=CascadeType.ALL)
    private Set<TriangleCorrection> corrections = new HashSet<TriangleCorrection>();
    
    protected Triangle() {
    }
    
    public Triangle(Project project, ProjectDataType dataType, String name) {
        this.meta = new MetaData(project, dataType, name);
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
        return meta.getProject();
    }
    
}
