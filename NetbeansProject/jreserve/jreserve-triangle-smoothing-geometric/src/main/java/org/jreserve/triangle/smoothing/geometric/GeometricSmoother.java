package org.jreserve.triangle.smoothing.geometric;

import java.util.List;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.value.TriangleCoordiante;
import org.jreserve.triangle.smoothing.Smoother;
import org.jreserve.triangle.smoothing.Smoothing;
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
    public Smoothing createSmoothing(ModifiableTriangularData triangle, List<TriangleCoordiante> cells, int visibleDigits) {
        GeometricSmoothingFactory factory = new GeometricSmoothingFactory(triangle, cells, visibleDigits);
        return factory.createSmoothing();
    }
}
