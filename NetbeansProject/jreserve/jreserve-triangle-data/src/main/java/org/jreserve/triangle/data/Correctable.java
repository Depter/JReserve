package org.jreserve.triangle.data;

import java.util.List;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Correctable {

    public final static String CORRECTION_PROPERTY = "PROP_CORRECTION";

    public PersistentObject getOwner();
    
    public List<TriangleCorrection> getCorrections();
    
    public void setCorrections(List<TriangleCorrection> corrections);
    
    public void addCorrection(TriangleCorrection correction);
    
    public void removeCorrection(TriangleCorrection correction);
}
