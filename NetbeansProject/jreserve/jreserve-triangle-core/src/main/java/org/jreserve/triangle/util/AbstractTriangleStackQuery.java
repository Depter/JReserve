package org.jreserve.triangle.util;

import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleStackQuery<R> {
    
    public R query(ModifiableTriangle triangle) {
        initQuery();
        processModifications(triangle);
        processBase(triangle);
        return getResult();
    }
    
    protected abstract void initQuery();
    
    private void processModifications(ModifiableTriangle triangle) {
        for(TriangularData data : triangle.getModifications())
            if(acceptsData(data))
                processData(data);
    }
    
    private void processBase(ModifiableTriangle triangle) {
        TriangularData base = triangle.getBaseData();
        if(acceptsData(base))
            processData(base);
    }
    
    protected abstract boolean acceptsData(TriangularData data);
    
    protected abstract void processData(TriangularData data);
    
    protected abstract R getResult();
}
