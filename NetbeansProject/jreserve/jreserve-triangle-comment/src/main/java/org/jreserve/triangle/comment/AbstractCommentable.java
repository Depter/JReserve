package org.jreserve.triangle.comment;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractCommentable implements Commentable, PropertyChangeListener {

    private final static String WRONG_OWNER = "Comment '%s' belongs to another owner than '%s'! OwnerId = '%s'.";
    
    protected ProjectElement element;
    protected PersistentObject owner;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public AbstractCommentable(ProjectElement element, PersistentObject owner) {
        this.element = element;
        this.element.addPropertyChangeListener(this);
        this.owner = owner;
    }
    
    @Override
    public PersistentObject getOwner() {
        return owner;
    }

    @Override
    public List<TriangleComment> getComments() {
        return new ArrayList<TriangleComment>((List<TriangleComment>) element.getProperty(COMMENT_PROPERTY));
    }

    @Override
    public void setComments(List<TriangleComment> comments) {
        checkOwnerId(comments);
        element.setProperty(COMMENT_PROPERTY, comments);
    }
    
    private void checkOwnerId(List<TriangleComment> comments) {
        for(TriangleComment comment : comments)
            checkOwnerId(comment);
    }

    private void checkOwnerId(TriangleComment comment) {
        if(!comment.getOwnerId().equals(owner.getId())) {
            String msg = String.format(WRONG_OWNER, comment, owner, comment.getOwnerId());
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public void addComment(TriangleComment comment) {
        checkOwnerId(comment);
        List<TriangleComment> comments = new ArrayList<TriangleComment>(getComments());
        if(!comments.contains(comment))
            comments.add(comment);
        element.setProperty(COMMENT_PROPERTY, comments);
    }

    @Override
    public void removeComment(TriangleComment comment) {
        List<TriangleComment> comments = new ArrayList<TriangleComment>(getComments());
        comments.remove(comment);
        element.setProperty(COMMENT_PROPERTY, comments);
    }
   
    @Override
    public boolean equals(Object o) {
        if(o instanceof Commentable)
            return owner.equals(((Commentable)o).getOwner());
        return false;
    }
    
    @Override
    public int hashCode() {
        return owner.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("AbstractCommentable [%s; %s]", element, owner);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(isCommentProperty(evt))
            fireChange();
    }
    
    private boolean isCommentProperty(PropertyChangeEvent evt) {
        return evt.getSource() == element &&
               evt.getPropertyName().equals(Commentable.COMMENT_PROPERTY);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : listeners)
            listener.stateChanged(evt);
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
}