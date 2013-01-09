package org.jreserve.triangle.smoothing.geometric;

import org.jreserve.audit.Auditor;
import org.jreserve.triangle.smoothing.SmoothingAuditor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.GeometricSmoothingAuditor.TypeName=Geometric smoothing"
})
@Auditor.Registration(400)
public class GeometricSmoothingAuditor extends SmoothingAuditor<GeometricSmoothing> {
    
    public GeometricSmoothingAuditor() {
        super(GeometricSmoothing.class, Bundle.MSG_GeometricSmoothingAuditor_TypeName());
    }
}
