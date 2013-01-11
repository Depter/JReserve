package org.jreserve.triangle.smoothing.exponential;

import java.util.List;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangleCoordiante;
import org.jreserve.triangle.smoothing.Smoother;
import org.jreserve.triangle.smoothing.Smoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Smoother.Registration(
    position = 300,
    displayName = "#LBL.ExponentialSmoother.DisplayName"
)
@Messages({
    "LBL.ExponentialSmoother.DisplayName=Exponential smoothing"
})
public class ExponentialSmoother implements Smoother {

    @Override
    public Smoothing createSmoothing(ModifiableTriangle triangle, List<TriangleCoordiante> cells, int visibleDigits) {
        ExponentialSmoothingFactory factory = new ExponentialSmoothingFactory(triangle, cells, visibleDigits);
        return factory.createSmoothing();
    }
}
