package org.jreserve.triangle.util;

import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.entities.TriangleModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClassCounterTriangleStackQuery extends AbstractTriangleStackQuery<Integer> {
    
    public static int getCount(Class<?> clazz, ModifiableTriangle triangle) {
        return new ClassCounterTriangleStackQuery(clazz).query(triangle);
    }
    
    private final Class<?> clazz;
    private int count = 0;
    
    public ClassCounterTriangleStackQuery(Class<?> clazz) {
        this.clazz = clazz;
    }
    
    @Override protected void initQuery() {}

    @Override
    protected boolean accepts(TriangleModification modification) {
        return clazz.isAssignableFrom(modification.getClass());
    }

    @Override
    protected void process(TriangleModification modification) {
        count++;
    }

    @Override
    protected Integer getResult() {
        return count;
    }
}
