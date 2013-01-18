package org.jreserve.triangle.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.util.ProjectData;
import org.jreserve.triangle.ChangeableTriangularData;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangleModification;
import org.jreserve.triangle.comment.CommentableTriangle;
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
public class Triangle extends AbstractPersistentObject implements ProjectData, ModifiableTriangle, CommentableTriangle, ChangeableTriangularData.Provider {
    
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
    private String description;
    
    @Column(name="IS_TRIANGLE", nullable=false)
    private boolean isTriangle;
    
    @Embedded
    private TriangleGeometry geometry;
    
    @NotAudited
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="TRIANGLE_MODIFICATION_LINK", schema="JRESERVE",
        joinColumns=@JoinColumn(name="TRIANGLE_ID"),
        inverseJoinColumns=@JoinColumn(name="MODIFICATION_ID")
    )
    private Set<TriangleModification> modifications = new TreeSet<TriangleModification>();
    
    @NotAudited
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="TRIANGLE_COMMENT_LINK", schema="JRESERVE",
        joinColumns=@JoinColumn(name="TRIANGLE_ID"),
        inverseJoinColumns=@JoinColumn(name="COMMENT_ID")
    )
    private Set<TriangleComment> comments = new TreeSet<TriangleComment>();
    
    @Transient
    private List<TriangleListener> listeners = new ArrayList<TriangleListener>();
    
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
        checkModification(modification);
        modifications.add(modification);
        fireModificationsChanged();
    }
    
    private void checkModification(TriangleModification modification) {
        if(modification == null) 
            throw new NullPointerException("Modification is null!");
        checkModificationOrder(modification);
    }
    
    private void checkModificationOrder(TriangleModification modification) {
        int order = modification.getOrder();
        if(containsOrder(order)) {
            String msg = "Can not add modification %s to triangle %s! Order %d already exists!";
            msg = String.format(msg, modification, this, order);
            throw new IllegalArgumentException(msg);
        }
    }
    
    private boolean containsOrder(int order) {
        for(TriangleModification mod : modifications)
            if(mod.getOrder() == order)
                return true;
        return false;
    }
    
    @Override
    public void removeModification(TriangleModification modification) {
        modifications.remove(modification);
        fireModificationsChanged();
    }

    @Override
    public int getMaxModificationOrder() {
        int max = 0;
        for(TriangleModification mod : modifications)
            if(max < mod.getOrder())
                max = mod.getOrder();
        return max;
    }
    
    @Override
    public List<TriangleModification> getModifications() {
        return new ArrayList<TriangleModification>(modifications);
    }
    
    @Override
    public TriangleComment createComment(int accident, int development, String user, String comment) {
        TriangleComment tc = new TriangleComment(this, accident, development, user, comment);
        comments.add(tc);
        fireCommentsChanged();
        return tc;
    }
    
    @Override
    public void removeComment(TriangleComment comment) {
        comments.remove(comment);
        fireCommentsChanged();
    }
    
    @Override
    public List<TriangleComment> getComments() {
        return new ArrayList<TriangleComment>(comments);
    }

    public void addTriangleListener(TriangleListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeTriangleListener(TriangleListener listener) {
        listeners.remove(listener);
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

    private void fireModificationsChanged() {
        for(TriangleListener listener : new ArrayList<TriangleListener>(listeners))
            listener.modificationsChanged(this);
    }

    private void fireCommentsChanged() {
        for(TriangleListener listener : new ArrayList<TriangleListener>(listeners))
            listener.commentsChanged(this);
    }

    @Override
    public ChangeableTriangularData getTriangularData() {
        return new TriangleBundle(this);
    }
}
