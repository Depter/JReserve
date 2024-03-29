package org.jreserve.triangle.entities;

import java.util.EventListener;
import org.jreserve.triangle.comment.TriangleCommentListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleListener extends EventListener, TriangleModificationListener, TriangleCommentListener {
    
    public void nameChanged(Triangle triangle);
    
    public void descriptionChanged(Triangle triangle);
    
    public void geometryChanged(Triangle triangle);
}
