package org.jreserve.triangle.util;

import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.ModifiedTriangularData;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClassCounterTriangleStackQuery extends AbstractTriangleStackQuery<Integer> {
    
    public static int getMaxOrder(Class<?> clazz, ModifiableTriangle triangle) {
        return new ClassCounterTriangleStackQuery(clazz).query(triangle);
    }
    
    private final Class<?> clazz;
    private int maxOrder = 0;
    
    public ClassCounterTriangleStackQuery(Class<?> clazz) {
        this.clazz = clazz;
    }
    
    @Override protected void initQuery() {}

    @Override
    protected boolean acceptsData(TriangularData data) {
        return (data instanceof ModifiedTriangularData) &&
               clazz.isAssignableFrom(data.getClass());
    }

    @Override
    protected void processData(TriangularData data) {
        int order = ((ModifiedTriangularData) data).getOrder();
        if(maxOrder < order)
            maxOrder = order;
    }

    @Override
    protected Integer getResult() {
        return maxOrder;
    }
}
