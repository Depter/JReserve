package org.jreserve.triangle.correction;

import org.jreserve.audit.Auditor;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.jreserve.triangle.util.TriangleModificationAuditor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.CorrectionAuditor.TypeName=Correction",
    "# {0} - accident",
    "# {1} - development",
    "# {2} - value",
    "MSG.CorrectionAuditor.Added=Added [{0}; {1}] = {2}",
    "# {0} - accident",
    "# {1} - development",
    "# {2} - value",
    "MSG.CorrectionAuditor.Deleted=Deleted [{0}; {1}] = {2}"
})
@Auditor.Registration(300)
public class CorrectionAuditor extends TriangleModificationAuditor<TriangleCorrection> {

    public CorrectionAuditor() {
        super(TriangleCorrection.class, Bundle.LBL_CorrectionAuditor_TypeName());
    }

    @Override
    protected String getAddChange(TriangleCorrection current) {
        int accident = current.getAccident() + 1;
        int development = current.getDevelopment() + 1;
        double value = current.getCorrigatedValue();
        return Bundle.MSG_CorrectionAuditor_Added(accident, development, value);
    }

    @Override
    protected String getDeleteChange(TriangleCorrection current) {
        int accident = current.getAccident() + 1;
        int development = current.getDevelopment() + 1;
        double value = current.getCorrigatedValue();
        return Bundle.MSG_CorrectionAuditor_Deleted(accident, development, value);
    }
}
