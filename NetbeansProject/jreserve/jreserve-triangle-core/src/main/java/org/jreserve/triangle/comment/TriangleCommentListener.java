package org.jreserve.triangle.comment;

import java.util.EventListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleCommentListener extends EventListener {
    
    public void commentsChanged(CommentableTriangle commentable);    

}
