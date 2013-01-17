package org.jreserve.triangle.smoothing;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.value.ModificationLoader;
import org.jreserve.triangle.TriangularDataModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSmoothingLoader implements ModificationLoader {

    @Override
    public List<TriangularDataModification> loadModifications(PersistentObject owner) {
        List<TriangularDataModification> mods = new ArrayList<TriangularDataModification>();
        for(Smoothing smoothing : loadSmoothings(owner))
            mods.add(new TriangleSmoothing(smoothing));
        return mods;
    }
    
    protected abstract List<Smoothing> loadSmoothings(PersistentObject owner);

}
