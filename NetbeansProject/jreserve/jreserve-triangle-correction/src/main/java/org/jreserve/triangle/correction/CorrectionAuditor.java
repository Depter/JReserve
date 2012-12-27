package org.jreserve.triangle.correction;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
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
public class CorrectionAuditor extends AbstractAuditor<TriangleCorrection> {

    public CorrectionAuditor() {
        factory.setType(Bundle.LBL_CorrectionAuditor_TypeName());
    }

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof PersistentObject);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        PersistentObject owner = (PersistentObject) value;
        return reader.createQuery()
              .forRevisionsOfEntity(TriangleCorrection.class, false, true)
              .add(AuditEntity.property("ownerId").eq(owner.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(TriangleCorrection current) {
        int accident = current.getAccident() + 1;
        int development = current.getDevelopment() + 1;
        double value = current.getCorrigatedValue();
        return Bundle.MSG_CorrectionAuditor_Added(accident, development, value);
    }

    @Override
    protected String getChange(TriangleCorrection previous, TriangleCorrection current) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getDeleteChange(TriangleCorrection current) {
        int accident = current.getAccident() + 1;
        int development = current.getDevelopment() + 1;
        double value = current.getCorrigatedValue();
        return Bundle.MSG_CorrectionAuditor_Deleted(accident, development, value);
    }
}
