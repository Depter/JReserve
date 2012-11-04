package org.jreserve.smoothing.geometric;

import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.Smoothing;
import org.jreserve.smoothing.SmoothingMethod;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@SmoothingMethod.Registration(
    id=GeometricSmoothingMethod.ID,
    displayName="#LBL.GeometricSmoothingMethod.DisplayName"
)
@Messages({
    "LBL.GeometricSmoothingMethod.DisplayName=Geometric smoothing"
})
public class GeometricSmoothingMethod implements SmoothingMethod {
    
    public final static int ID = 100;
    
    @Override
    public Smoothing createSmoothing(PersistentObject owner, TriangleWidget widget, TriangleCell[] cells) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Smoothing> getSmoothings(PersistentObject owner) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
