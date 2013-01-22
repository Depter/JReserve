package org.jreserve.triangle.comment;

import java.util.*;
import org.jreserve.triangle.entities.TriangleComment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCommentContainer {
    
    private Set<TriangleComment> comments;
    private CommentableTriangle triangle;
    private List<TriangleCommentListener> listeners = new ArrayList<TriangleCommentListener>();
    
    public TriangleCommentContainer(CommentableTriangle triangle, Set<TriangleComment> comments) {
        this.triangle = triangle;
        this.comments = comments;
    }
    
    public void addComment(TriangleComment comment) {
        comments.add(comment);
        fireCommentsChanged();
    }
    
    public void removeComment(TriangleComment comment) {
        comments.remove(comment);
        fireCommentsChanged();
    }
    
    public List<TriangleComment> getComments() {
        return new ArrayList<TriangleComment>(comments);
    }
    
    public void setComments(Collection<TriangleComment> comments) {
        Set<TriangleComment> newComments = new TreeSet<TriangleComment>();
        for(TriangleComment comment : comments)
            newComments.add(comment);
        this.comments = newComments;
        fireCommentsChanged();
    }

    public void addTriangleCommentListener(TriangleCommentListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeTriangleCommentListener(TriangleCommentListener listener) {
        listeners.remove(listener);
    }

    public void fireCommentsChanged() {
        for(TriangleCommentListener listener : new ArrayList<TriangleCommentListener>(listeners))
            listener.commentsChanged(triangle);
    }
}
