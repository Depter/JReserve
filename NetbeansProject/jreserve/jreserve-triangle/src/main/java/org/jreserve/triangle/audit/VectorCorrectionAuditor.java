package org.jreserve.triangle.audit;

import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorCorrection;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.VectorCorrectionAuditor.TypeName=Correction",
    "# {0} - correction",
    "# {1} - accident",
    "MSG.VectorCorrectionAuditor.Created=Created correction \"{0}\" for [{1, date, yyyy-MM-dd}].",
    "# {0} - correction",
    "# {1} - accident",
    "MSG.VectorCorrectionAuditor.Deleted=Deleted correction \"{0}\" from [{1, date, yyyy-MM-dd}].",
    "# {0} - old correction",
    "# {1} - new correction",
    "# {2} - accident",
    "MSG.VectorCorrectionAuditor.Change=Changed correction \"{0} => {1}\" from [{2, date, yyyy-MM-dd}]."
})
@Auditor.Registration(100)
public class VectorCorrectionAuditor extends AbstractAuditor<VectorCorrection> {

    public VectorCorrectionAuditor() {
        factory.setType(Bundle.MSG_VectorCorrectionAuditor_TypeName());
    }

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Vector);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        Vector vector = (Vector) value;
        return reader.createQuery()
              .forRevisionsOfEntity(VectorCorrection.class, false, true)
              .add(AuditEntity.relatedId("vector").eq(vector.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(VectorCorrection current) {
        Date accident = current.getAccidentDate();
        double value = current.getCorrection();
        return Bundle.MSG_VectorCorrectionAuditor_Created(value, accident);
    }

    @Override
    protected String getDeleteChange(VectorCorrection current) {
        Date accident = current.getAccidentDate();
        double value = current.getCorrection();
        return Bundle.MSG_VectorCorrectionAuditor_Deleted(value, accident);
    }

    @Override
    protected String getChange(VectorCorrection previous, VectorCorrection current) {
        Date accident = current.getAccidentDate();
        double value = current.getCorrection();
        double old = previous.getCorrection();
        return Bundle.MSG_VectorCorrectionAuditor_Change(value, old, accident);
    }
}
