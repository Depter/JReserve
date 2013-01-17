package org.jreserve.factor.core.single;

import java.util.List;
import org.jreserve.factor.core.CummulatedTriangularData;
import org.jreserve.factor.core.SimpleTriangularFactors;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.TriangularDataModification;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SingleFactors implements ModifiableTriangularData {
    
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
    public List<TriangularDataModification> getModifications() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addModification(TriangularDataModification modification) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeModification(TriangularDataModification modification) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TriangularData getTriangularData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}