package org.jreserve.triangle.data;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;
import static org.jreserve.triangle.data.Commentable.COMMENT_PROPERTY;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractCommentable<T> implements Commentable {

    private final static String WRONG_OWNER = "Comment '%s' belongs to another owner than '%s'! OwnerId = '%s'.";
    
    protected ProjectElement<T> element;
    protected PersistentObject owner;

    public AbstractCommentable(ProjectElement<T> element, PersistentObject owner) {
        this.element = element;
        this.owner = owner;
    }
    
    @Override
    public PersistentObject getOwner() {
        return owner;
    }

    @Override
    public List<TriangleComment> getComments() {
        return (List<TriangleComment>) element.getProperty(COMMENT_PROPERTY);
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
}