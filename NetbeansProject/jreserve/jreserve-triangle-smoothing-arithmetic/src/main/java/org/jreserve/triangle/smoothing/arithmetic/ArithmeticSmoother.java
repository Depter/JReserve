package org.jreserve.triangle.smoothing.arithmetic;

import java.util.List;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.value.TriangleCoordiante;
import org.jreserve.triangle.smoothing.Smoother;
import org.jreserve.triangle.smoothing.Smoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Smoother.Registration(
    position = 100,
    displayName = "#LBL.ArithmeticSmoother.DisplayName"
)
@Messages({
    "LBL.ArithmeticSmoother.DisplayName=Arithmetic smoothing"
})
public class ArithmeticSmoother implements Smoother {

    @Override
    public Smoothing createSmoothing(ModifiableTriangularData triangle, List<TriangleCoordiante> cells, int visibleDigits) {
        ArithmeticSmoothingFactory factory = new ArithmeticSmoothingFactory(triangle, cells, visibleDigits);
        return factory.createSmoothing();
    }
}
