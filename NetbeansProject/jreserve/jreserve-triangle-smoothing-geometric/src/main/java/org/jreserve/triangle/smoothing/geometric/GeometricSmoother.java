package org.jreserve.triangle.smoothing.geometric;

import org.jreserve.triangle.smoothing.Smoother;
import org.jreserve.triangle.smoothing.Smoothing;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Smoother.Registration(
    position = 200,
    displayName = "#LBL.GeometricSmoother.DisplayName"
)
@Messages({
    "LBL.GeometricSmoother.DisplayName=Geometric smoothing"
})
public class GeometricSmoother implements Smoother {

    @Override
    public Smoothing createSmoothing(Lookup lookup) {
        GeometricSmoothingFactory factory = new GeometricSmoothingFactory(lookup);
        return factory.createSmoothing();
    }
}
