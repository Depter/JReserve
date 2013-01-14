package org.jreserve.estimate.expectedratio;

import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.util.ProjectData;
import org.jreserve.triangle.entities.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Audited
@EntityRegistration
@Entity
@Table(name="EXPECTED_RATIO_ESTIMATE", schema="JRESERVE")
public class ExpectedRatioEstimate extends AbstractPersistentObject implements ProjectData  {
    private final static long serialVersionUID = 1L;
    
    public final static int POSITION = 100;
    private final static int NAME_LENGTH = 64;

    @Column(name="ESTIMATE_NAME", nullable=false, length=NAME_LENGTH)
    private String name;
    
    @Column(name="ESTIMATE_DESCRIPTION")
    @Type(type="org.hibernate.type.TextType")
    private String description;
    
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="TRIANGLE_ID", referencedColumnName="ID", nullable=false)
    private Triangle triangle;

    
    protected ExpectedRatioEstimate() {
    }
    
    public ExpectedRatioEstimate(Triangle triangle, String name) {
        initTriangle(triangle);
        initName(name);
    }
    
    private void initTriangle(Triangle triangle) {
        if(triangle == null)
            throw new NullPointerException("Triangle is null!");
        this.triangle = triangle;
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_LENGTH);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        initName(name);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Triangle getTriangle() {
        return triangle;
    }

    @Override
    public Project getProject() {
        return triangle.getProject();
    }

    @Override
    public int getPosition() {
        return POSITION;
    }
    
    @Override
    public String toString() {
        return String.format("ExpectedLinkEstimate [%s]", name);
    }
    
    public String getPath() {
        if(triangle == null)
            return toString();
        return String.format("%s/%s", triangle.getProject().getPath(), this);
    }
}
