package org.jreserve.triangle.audit;

import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorComment;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.VectorCommentAuditor.TypeName=Comment",
    "# {0} - accident",
    "MSG.VectorCommentAuditor.Created=Created for {0, date, yyyy-MM-dd}.",
    "# {0} - accident",
    "MSG.VectorCommentAuditor.Deleted=Deleted from {0, date, yyyy-MM-dd}.",
    "MSG.VectorCommentAuditor.Change=Changed."
})
@Auditor.Registration(200)
public class VectorCommentAuditor extends AbstractAuditor<VectorComment> {

    public VectorCommentAuditor() {
        factory.setType(Bundle.MSG_VectorCommentAuditor_TypeName());
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Vector);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        Vector vector = (Vector) value;
        return reader.createQuery()
              .forRevisionsOfEntity(VectorComment.class, false, true)
              .add(AuditEntity.relatedId("vector").eq(vector.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(VectorComment current) {
        Date accident = current.getAccidentDate();
        return Bundle.MSG_VectorCommentAuditor_Created(accident);
    }

    @Override
    protected String getDeleteChange(VectorComment current) {
        Date accident = current.getAccidentDate();
        return Bundle.MSG_VectorCommentAuditor_Deleted(accident);
    }

    @Override
    protected String getChange(VectorComment previous, VectorComment current) {
        return Bundle.MSG_VectorCommentAuditor_Change();
    }
}
