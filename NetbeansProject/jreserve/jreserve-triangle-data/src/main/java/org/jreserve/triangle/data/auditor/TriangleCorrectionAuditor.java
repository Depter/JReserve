package org.jreserve.triangle.data.auditor;

import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.data.TriangleCorrection;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.TriangleCorrectionAuditor.Type=Correction",
    "# {0} - accident",
    "# {1} - development",
    "# {2} - correction",
    "LBL.TriangleCorrectionAuditor.Created=Created [{0, date, yyyy-MM-dd}] / [{1, date, yyyy-MM-dd}]: {2}.",
    "# {0} - accident",
    "# {1} - development",
    "# {2} - correction",
    "LBL.TriangleCorrectionAuditor.Deleted=Deleted [{0, date, yyyy-MM-dd}] / [{1, date, yyyy-MM-dd}]: {2}."
})
@Auditor.Registration(100)
public class TriangleCorrectionAuditor extends AbstractAuditor<TriangleCorrection>{

    public TriangleCorrectionAuditor() {
        factory.setType(Bundle.LBL_TriangleCommentAuditor_Type());
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
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        double correction = current.getCorrection();
        return Bundle.LBL_TriangleCorrectionAuditor_Created(accident, development, correction);
    }

    @Override
    protected String getDeleteChange(TriangleCorrection current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        double correction = current.getCorrection();
        return Bundle.LBL_TriangleCorrectionAuditor_Deleted(accident, development, correction);
    }

    @Override
    protected String getChange(TriangleCorrection previous, TriangleCorrection current) {
        throw new UnsupportedOperationException("Not supposed to be called!");
    }
}
