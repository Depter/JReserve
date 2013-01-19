package org.jreserve.triangle.smoothing.arithmetic;

import java.util.List;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.smoothing.Smoother;
import org.jreserve.triangle.smoothing.Smoothing;
import org.openide.util.Lookup;
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
    public Smoothing createSmoothing(Lookup lookup) {
        ArithmeticSmoothingFactory factory = new ArithmeticSmoothingFactory(lookup);
        return factory.createSmoothing();
    }
}
