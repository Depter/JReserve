package org.jreserve.triangle.smoothing.exponential;

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
    position = 300,
    displayName = "#LBL.ExponentialSmoother.DisplayName"
)
@Messages({
    "LBL.ExponentialSmoother.DisplayName=Exponential smoothing"
})
public class ExponentialSmoother implements Smoother {

    @Override
    public Smoothing createSmoothing(Lookup lookup) {
        ExponentialSmoothingFactory factory = new ExponentialSmoothingFactory(lookup);
        return factory.createSmoothing();
    }
}
