package org.jreserve.triangle.entities;

import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.util.ProjectData;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.comment.TriangleCommentContainer;
import org.jreserve.triangle.comment.TriangleCommentListener;
import org.jreserve.triangle.data.TriangleBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="TRIANGLE", schema="JRESERVE")
public class Triangle extends AbstractPersistentObject implements ProjectData, ModifiableTriangle, CommentableTriangle, TriangularData.Provider {
    
    public final static String GEOMETRY_PROPERTY = "TRIANGLE_GEOMETRY_PROPERTY";
    public final static int TRIANGLE_POSITION = 100;
    public final static int VECTOR_POSITION = 200;
    private final static int NAME_SIZE = 64;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false)
    private Project project;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DATA_TYPE_ID", referencedColumnName="ID", nullable=false)
    private ProjectDataType dataType;
    
    @Column(name="NAME", nullable=false, length=NAME_SIZE)
    private String name;
    
    @Column(name="DESCRIPTION")
    @Type(type="org.hibernate.type.TextType")
    private String description;
    
    @Column(name="IS_TRIANGLE", nullable=false)
    private boolean isTriangle;
    
    @Embedded
    private TriangleGeometry geometry;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="TRIANGLE_MODIFICATION_LINK", schema="JRESERVE",
        joinColumns=@JoinColumn(name="TRIANGLE_ID"),
        inverseJoinColumns=@JoinColumn(name="MODIFICATION_ID")
    )
    private Set<TriangleModification> modifications = new TreeSet<TriangleModification>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="TRIANGLE_COMMENT_LINK", schema="JRESERVE",
        joinColumns=@JoinColumn(name="TRIANGLE_ID"),
        inverseJoinColumns=@JoinColumn(name="COMMENT_ID")
    )
    private Set<TriangleComment> comments = new TreeSet<TriangleComment>();
    
    @Transient
    private List<TriangleListener> listeners = new ArrayList<TriangleListener>();
    @Transient
    private TriangleModificationContainer modificationContainer = new TriangleModificationContainer(this, modifications);
    @Transient
    private TriangleCommentContainer commentContainer = new TriangleCommentContainer(this, comments);
    
    protected Triangle() {
    }
    
    public Triangle(Project project, ProjectDataType dataType, String name, boolean isTriangle) {
        initDataType(dataType);
        initName(name);
        initProject(project);
        this.isTriangle = isTriangle;
    }
    
    private void initDataType(ProjectDataType dataType) {
        if(dataType == null)
            throw new NullPointerException("DataType is null!");
        this.dataType = dataType;
    }
    
    private void initName(String name) {
        PersistenceUtil.checkVarchar(name, NAME_SIZE);
        this.name = name;
    }
    
    private void initProject(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        this.project = project;
    }

    @Override
    public int getPosition() {
        return isTriangle? TRIANGLE_POSITION : VECTOR_POSITION;
    }

    @Override
    public Project getProject() {
        return project;
    }

    public ProjectDataType getDataType() {
        return dataType;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        initName(name);
        fireNameChanged();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        fireDescriptionChanged();
    }
    
    public boolean isTriangle() {
        return isTriangle;
    }
    
    public TriangleGeometry getGeometry() {
        return geometry==null? null : geometry.copy();
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        checkGeometry(geometry);
        this.geometry = geometry.copy();
        fireGeometryChanged();
    }
    
    private void checkGeometry(TriangleGeometry geometry) {
        if(geometry == null)
            throw new NullPointerException("Geometry can not be null!");
        if(!isTriangle && geometry.getDevelopmentPeriods()>1) {
            String msg = "Can not set geometry with more than 1 development periods (%d) to a vector!";
            throw new IllegalArgumentException(String.format(msg, geometry.getDevelopmentPeriods()));
        }
    }
    
    @Override
    public void addModification(TriangleModification modification) {
        modificationContainer.addModification(modification);
    }
    
    @Override
    public void removeModification(TriangleModification modification) {
        modificationContainer.removeModification(modification);
    }
    
    public void setModifications(Collection<TriangleModification> modifications) {
        modificationContainer.setModifications(modifications);
    }

    @Override
    public int getMaxModificationOrder() {
        return modificationContainer.getMaxModificationOrder();
    }
    
    @Override
    public List<TriangleModification> getModifications() {
        return modificationContainer.getModifications();
    }
    
    @Override
    public void addComment(TriangleComment comment) {
        commentContainer.addComment(comment);
    }
    
    @Override
    public void removeComment(TriangleComment comment) {
        commentContainer.removeComment(comment);
    }
    
    @Override
    public List<TriangleComment> getComments() {
        return commentContainer.getComments();
    }
    
    public void setComments(Collection<TriangleComment> comments) {
        commentContainer.setComments(comments);
    }

    public void addTriangleListener(TriangleListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
        addTriangleModificationListener(listener);
        addTriangleCommentListener(listener);
    }

    public void removeTriangleListener(TriangleListener listener) {
        listeners.remove(listener);
        removeTriangleModificationListener(listener);
        removeTriangleCommentListener(listener);
    }
    
    public void addTriangleModificationListener(TriangleModificationListener listener) {
        modificationContainer.addTriangleModificationListener(listener);
    }
    
    public void removeTriangleModificationListener(TriangleModificationListener listener) {
        modificationContainer.removeTriangleModificationListener(listener);
    }
    
    public void addTriangleCommentListener(TriangleCommentListener listener) {
        commentContainer.addTriangleCommentListener(listener);
    }
    
    public void removeTriangleCommentListener(TriangleCommentListener listener) {
        commentContainer.removeTriangleCommentListener(listener);
    }
    
    private void fireNameChanged() {
        for(TriangleListener listener : new ArrayList<TriangleListener>(listeners))
            listener.nameChanged(this);
    }

    private void fireDescriptionChanged() {
        for(TriangleListener listener : new ArrayList<TriangleListener>(listeners))
            listener.descriptionChanged(this);
    }

    private void fireGeometryChanged() {
        for(TriangleListener listener : new ArrayList<TriangleListener>(listeners))
            listener.geometryChanged(this);
    }

    @Override
    public TriangularData getTriangularData() {
        return new TriangleBundle(this);
    }
    
    @Override
    public String toString() {
        String msg = isTriangle? "Triangle [%s]" : "Vector [%s]";
        return String.format(msg, name);
    }
}
