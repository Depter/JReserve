package org.jreserve.triangle.smoothing;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.ModificationLoader;
import org.jreserve.triangle.ModifiedTriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSmoothingLoader implements ModificationLoader {

    @Override
    public List<ModifiedTriangularData> loadModifications(PersistentObject owner) {
        List<ModifiedTriangularData> mods = new ArrayList<ModifiedTriangularData>();
        for(Smoothing smoothing : loadSmoothings(owner))
            mods.add(new TriangleSmoothing(smoothing));
        return mods;
    }
    
    protected abstract List<Smoothing> loadSmoothings(PersistentObject owner);

}
