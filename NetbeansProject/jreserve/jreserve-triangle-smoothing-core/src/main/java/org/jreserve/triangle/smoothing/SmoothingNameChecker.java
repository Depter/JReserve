package org.jreserve.triangle.smoothing;

import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.entities.TriangleModification;
import org.jreserve.triangle.util.AbstractTriangleStackQuery;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingNameChecker extends AbstractTriangleStackQuery<Boolean> {

    public static boolean isValidName(ModifiableTriangle triangle, String name) {
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
    protected boolean accepts(TriangleModification modification) {
        return validName &&
               (modification instanceof Smoothing);
    }

    @Override
    protected void process(TriangleModification modification) {
        Smoothing smoothing = (Smoothing) modification;
        String name = smoothing.getName();
        if(newName.equalsIgnoreCase(name))
            validName = false;
    }

    @Override
    protected Boolean getResult() {
        return validName;
    }
}