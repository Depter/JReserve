package org.jreserve.triangle.entities;

import java.util.EventListener;
import org.jreserve.triangle.ModifiableTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModificationListener extends EventListener {
    
    public void modificationsChanged(ModifiableTriangle triangle);

}
