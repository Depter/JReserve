package org.jreserve.smoothing.geometric;

import org.jreserve.audit.Auditor;
import org.jreserve.smoothing.audit.SmoothingAuditor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.GeometricSmoothingAuditor.Type=Geometric smoothing"
})
@Auditor.Registration(300)
public class GeometricSmoothingAuditor extends SmoothingAuditor<GeometricSmoothing> {

    public GeometricSmoothingAuditor() {
        super(GeometricSmoothing.class);
        factory.setType(Bundle.MSG_GeometricSmoothingAuditor_Type());
    }

}
