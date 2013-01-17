package org.jreserve.triangle.smoothing;

import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.util.AbstractTriangleStackQuery;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingNameChecker extends AbstractTriangleStackQuery<Boolean> {

    public static boolean isValidName(ModifiableTriangularData triangle, String name) {
        SmoothingNameChecker query = new SmoothingNameChecker(name);
        return query.query(triangle);
    }
    
    private boolean validName = true;
    private String newName;
    
    public SmoothingNameChecker(String name) {
        this.newName = name;
    }
    
    @Override protected void initQuery() {}

    @Override
    protected boolean acceptsData(TriangularData data) {
        return validName && 
               (data instanceof TriangleSmoothing);
    }

    @Override
    protected void processData(TriangularData data) {
        TriangleSmoothing mod = (TriangleSmoothing) data;
        String name = mod.getSmoothing().getName();
        if(newName.equalsIgnoreCase(name))
            validName = false;
    }

    @Override
    protected Boolean getResult() {
        return validName;
    }
}
