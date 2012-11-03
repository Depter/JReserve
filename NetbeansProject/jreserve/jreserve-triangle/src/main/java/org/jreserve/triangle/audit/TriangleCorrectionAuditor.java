package org.jreserve.triangle.audit;

import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleCorrection;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.TriangleCorrectionAuditor.TypeName=Correction",
    "# {0} - correction",
    "# {1} - accident",
    "# {2} - development",
    "MSG.TriangleCorrectionAuditor.Created=Created correction \"{0}\" for [{1, date, yyyy-MM-dd}]/[{2, date, yyyy-MM-dd}].",
    "# {0} - correction",
    "# {1} - accident",
    "# {2} - development",
    "MSG.TriangleCorrectionAuditor.Deleted=Deleted correction \"{0}\" from [{1, date, yyyy-MM-dd}]/[{2, date, yyyy-MM-dd}].",
    "# {0} - old correction",
    "# {1} - new correction",
    "# {2} - accident",
    "# {3} - development",
    "MSG.TriangleCorrectionAuditor.Change=Changed correction \"{0} => {1}\" from [{2, date, yyyy-MM-dd}]/[{3, date, yyyy-MM-dd}]."
})
@Auditor.Registration(100)
public class TriangleCorrectionAuditor extends AbstractAuditor<TriangleCorrection> {

    public TriangleCorrectionAuditor() {
        factory.setType(Bundle.MSG_TriangleCorrectionAuditor_TypeName());
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Triangle);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        Triangle triangle = (Triangle) value;
        return reader.createQuery()
              .forRevisionsOfEntity(TriangleCorrection.class, false, true)
              .add(AuditEntity.relatedId("triangle").eq(triangle.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(TriangleCorrection current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        double value = current.getCorrection();
        return Bundle.MSG_TriangleCorrectionAuditor_Created(value, accident, development);
    }

    @Override
    protected String getDeleteChange(TriangleCorrection current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        double value = current.getCorrection();
        return Bundle.MSG_TriangleCorrectionAuditor_Deleted(value, accident, development);
    }

    @Override
    protected String getChange(TriangleCorrection previous, TriangleCorrection current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        double value = current.getCorrection();
        double old = previous.getCorrection();
        return Bundle.MSG_TriangleCorrectionAuditor_Change(value, old, accident, development);
    }
}
