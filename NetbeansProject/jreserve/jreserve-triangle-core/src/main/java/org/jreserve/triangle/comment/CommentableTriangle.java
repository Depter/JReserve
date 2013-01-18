package org.jreserve.triangle.comment;

import java.util.List;
import org.jreserve.triangle.entities.TriangleComment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface CommentableTriangle {
    
    public TriangleComment createComment(int accident, int development, String user, String comment);
    
    public void removeComment(TriangleComment comment);
    
    public List<TriangleComment> getComments();

}
