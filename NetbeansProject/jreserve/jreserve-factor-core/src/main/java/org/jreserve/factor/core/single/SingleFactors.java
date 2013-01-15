package org.jreserve.factor.core.single;

import java.util.List;
import org.jreserve.factor.core.CummulatedTriangularData;
import org.jreserve.factor.core.SimpleTriangularFactors;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.ModifiedTriangularData;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SingleFactors implements ModifiableTriangle {
    
    private TriangularData inputData;
    private TriangularData factors;
    
    public SingleFactors(TriangularData inputData) {
        factors = new SimpleTriangularFactors(new CummulatedTriangularData(factors));
    }
    
    @Override
    public PersistentObject getOwner() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TriangularData getBaseData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMaxModificationOrder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ModifiedTriangularData> getModifications() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addModification(ModifiedTriangularData modification) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeModification(ModifiedTriangularData modification) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TriangularData getTriangularData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
