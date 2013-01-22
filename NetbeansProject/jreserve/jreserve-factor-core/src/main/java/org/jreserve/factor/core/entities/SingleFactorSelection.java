package org.jreserve.factor.core.entities;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.*;
import org.hibernate.envers.Audited;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityRegistration;
import org.jreserve.triangle.ChangeableTriangularData;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.comment.CommentableTriangle;
import org.jreserve.triangle.comment.TriangleCommentContainer;
import org.jreserve.triangle.comment.TriangleCommentListener;
import org.jreserve.triangle.entities.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@EntityRegistration
@Audited
@Entity
@Table(name="SINGLE_FACTOR_SELECTION", schema="JRESERVE")
public class SingleFactorSelection extends AbstractPersistentObject implements ModifiableTriangle, CommentableTriangle, ChangeableTriangularData.Provider {
    private final static long serialVersionUID = 1L;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TRIANGLE_ID", referencedColumnName="ID", nullable=false)
    private Triangle triangle;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="FACTOR_MODIFICATION_LINK", schema="JRESERVE",
        joinColumns=@JoinColumn(name="FACTOR_ID"),
        inverseJoinColumns=@JoinColumn(name="MODIFICATION_ID")
    )
    private Set<TriangleModification> modifications = new TreeSet<TriangleModification>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="FACTOR_COMMENT_LINK", schema="JRESERVE",
        joinColumns=@JoinColumn(name="FACTOR_ID"),
        inverseJoinColumns=@JoinColumn(name="COMMENT_ID")
    )
    private Set<TriangleComment> comments = new TreeSet<TriangleComment>();
    @Transient
    private TriangleModificationContainer modificationContainer = new TriangleModificationContainer(this, modifications);
    @Transient
    private TriangleCommentContainer commentContainer = new TriangleCommentContainer(this, comments);
     
    protected SingleFactorSelection() {
    }
    
    public SingleFactorSelection(Triangle triangle) {
        if(!triangle.isTriangle())
            throw new IllegalArgumentException("Triangle '"+triangle+"' is a vector!");
        this.triangle = triangle;
    }
    
    public Triangle getTriangle() {
        return triangle;
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

    @Override
    public ChangeableTriangularData getTriangularData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String toString() {
        String msg = "SingleFactorSelection [triangle = %s]";
        return String.format(msg, triangle.getName());
    }
}