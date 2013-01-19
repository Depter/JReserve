package org.jreserve.triangle;

import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiableTriangle {
    
    public final static String MODIFICATIONS_PROPERTY = "TRIANGLE_MODIFICATIONS";
    
    public void addModification(TriangleModification modification);
    
    public void removeModification(TriangleModification modification);
    
    public int getMaxModificationOrder();
    
    public List<TriangleModification> getModifications();

}
