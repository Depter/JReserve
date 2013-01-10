package org.jreserve.triangle.smoothing.arithmetic;

import org.jreserve.audit.Auditor;
import org.jreserve.triangle.smoothing.SmoothingAuditor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.ArithmeticSmoothingAuditor.TypeName=Arithmetic smoothing"
})
@Auditor.Registration(400)
public class ArithmeticSmoothingAuditor extends SmoothingAuditor<ArithmeticSmoothing> {
    
    public ArithmeticSmoothingAuditor() {
        super(ArithmeticSmoothing.class, Bundle.MSG_ArithmeticSmoothingAuditor_TypeName());
    }
}
