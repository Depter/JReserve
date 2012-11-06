package org.jreserve.smoothing.exponential;

import org.jreserve.audit.Auditor;
import org.jreserve.smoothing.audit.SmoothingAuditor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ExponentialSmoothingAuditor.Type=Exponential smoothing"
})
@Auditor.Registration(300)
public class ExponentialSmoothingAuditor extends SmoothingAuditor<ExponentialSmoothing> {

    public ExponentialSmoothingAuditor() {
        super(ExponentialSmoothing.class);
        factory.setType(Bundle.MSG_ExponentialSmoothingAuditor_Type());
    }
}
