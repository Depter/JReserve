package org.jreserve.triangle.entities;

import java.util.EventListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleListener extends EventListener {
    
    public void nameChanged(Triangle triangle);
    
    public void descriptionChanged(Triangle triangle);
    
    public void geometryChanged(Triangle triangle);
    
    public void modificationsChanged(Triangle triangle);
    
    public void commentsChanged(Triangle triangle);    
    
}
