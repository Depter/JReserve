package org.jreserve.triangle.entities;

import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.comment.CommentableTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleAdapter implements TriangleListener {
    @Override 
    public void nameChanged(Triangle triangle) {
    }
    
    @Override
    public void descriptionChanged(Triangle triangle) {
    }
    
    @Override
    public void geometryChanged(Triangle triangle) {
    }
    
    @Override 
    public void modificationsChanged(ModifiableTriangle triangle) {
    }
    
    @Override 
    public void commentsChanged(CommentableTriangle triangle) {
    }
}
