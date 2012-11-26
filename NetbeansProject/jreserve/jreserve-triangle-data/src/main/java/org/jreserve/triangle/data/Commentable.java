package org.jreserve.triangle.data;

import java.util.List;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Commentable {

    public final static String COMMENT_PROPERTY = "PROP_COMMENTS";

    public PersistentObject getOwner();
    
    public List<TriangleComment> getComments();
    
    public void setComments(List<TriangleComment> comments);
    
    public void addComment(TriangleComment comment);
    
    public void removeComment(TriangleComment comment);
    
}
