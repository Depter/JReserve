package org.jreserve.triangle.smoothing.exponential;

import org.jreserve.audit.Auditor;
import org.jreserve.triangle.smoothing.SmoothingAuditor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ExponentialSmoothingAuditor.TypeName=Exponential smoothing"
})
@Auditor.Registration(600)
public class ExponentialSmoothingAuditor extends SmoothingAuditor<ExponentialSmoothing> {
    
    public ExponentialSmoothingAuditor() {
        super(ExponentialSmoothing.class, Bundle.MSG_ExponentialSmoothingAuditor_TypeName());
    }
}
