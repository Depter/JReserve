package org.jreserve.triangle.data.auditor;

import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.data.TriangleComment;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.TriangleCommentAuditor.Type=Comment",
    "# {0} - accident",
    "# {1} - development",
    "# {2} - user",
    "# {3} - comment",
    "LBL.TriangleCommentAuditor.Created=Created [{0, date, yyyy-MM-dd}] / [{1, date, yyyy-MM-dd}] / [{2}]: {3}.",
    "# {0} - accident",
    "# {1} - development",
    "# {2} - user",
    "LBL.TriangleCommentAuditor.Deleted=Deleted [{0, date, yyyy-MM-dd}] / [{1, date, yyyy-MM-dd}] / [{2}].",
    "# {0} - accident",
    "# {1} - development",
    "# {2} - user",
    "# {3} - old comment",
    "# {4} - new comment",
    "LBL.TriangleCommentAuditor.Changed=Changed [{0, date, yyyy-MM-dd}] / [{1, date, yyyy-MM-dd}] / [{2}]: {3} => {4}."
})
@Auditor.Registration(200)
public class TriangleCommentAuditor extends AbstractAuditor<TriangleComment>{

    public TriangleCommentAuditor() {
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
              .forRevisionsOfEntity(TriangleComment.class, false, true)
              .add(AuditEntity.property("ownerId").eq(owner.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(TriangleComment current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        String user = current.getUserName();
        String comment = current.getCommentText();
        return Bundle.LBL_TriangleCommentAuditor_Created(accident, development, user, comment);
    }

    @Override
    protected String getDeleteChange(TriangleComment current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        String user = current.getUserName();
        return Bundle.LBL_TriangleCommentAuditor_Deleted(accident, development, user);
    }

    @Override
    protected String getChange(TriangleComment previous, TriangleComment current) {
        Date accident = current.getAccidentDate();
        Date development = current.getDevelopmentDate();
        String user = current.getUserName();
        String oldComment = previous.getCommentText();
        String comment = current.getCommentText();
        return Bundle.LBL_TriangleCommentAuditor_Changed(accident, development, user, oldComment, comment);
    }

}
