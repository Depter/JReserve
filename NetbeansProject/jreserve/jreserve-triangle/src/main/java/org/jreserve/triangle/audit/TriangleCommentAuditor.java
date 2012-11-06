package org.jreserve.triangle.audit;

import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleComment;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.TriangleCommentAuditor.TypeName=Comment",
    "# {0} - accident",
    "# {1} - development",
    "MSG.TriangleCommentAuditor.Created=Created for {0, date, yyyy-MM-dd} / {1, date, yyyy-MM-dd}.",
    "# {0} - accident",
    "# {1} - development",
    "MSG.TriangleCommentAuditor.Deleted=Deleted from {0, date, yyyy-MM-dd} / {1, date, yyyy-MM-dd}.",
    "MSG.TriangleCommentAuditor.Change=Changed."
})
@Auditor.Registration(200)
public class TriangleCommentAuditor extends AbstractAuditor<TriangleComment> {

    public TriangleCommentAuditor() {
        factory.setType(Bundle.MSG_TriangleCommentAuditor_TypeName());
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Triangle);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        Triangle triangle = (Triangle) value;
        return reader.createQuery()
              .forRevisionsOfEntity(TriangleComment.class, false, true)
              .add(AuditEntity.relatedId("triangle").eq(triangle.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(TriangleComment current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        return Bundle.MSG_TriangleCommentAuditor_Created(accident, development);
    }

    @Override
    protected String getDeleteChange(TriangleComment current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        return Bundle.MSG_TriangleCommentAuditor_Deleted(accident, development);
    }

    @Override
    protected String getChange(TriangleComment previous, TriangleComment current) {
        return Bundle.MSG_TriangleCommentAuditor_Change();
    }
}
