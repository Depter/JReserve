package org.jreserve.triangle.util;

import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangleModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleStackQuery<R> {
    
    public R query(ModifiableTriangle triangle) {
        initQuery();
        processLayers(triangle);
        return getResult();
    }
    
    protected abstract void initQuery();
    
    private void processLayers(ModifiableTriangle triangle) {
        for(TriangleModification modification : triangle.getModifications())
            if(accepts(modification))
                process(modification);
    }
    
    protected abstract boolean accepts(TriangleModification modification);
    
    protected abstract void process(TriangleModification modification);
    
    protected abstract R getResult();
}
