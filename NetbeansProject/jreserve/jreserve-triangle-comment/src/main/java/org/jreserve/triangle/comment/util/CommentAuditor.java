package org.jreserve.triangle.comment.util;

import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.comment.TriangleComment;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CommentAuditor.TypeName=Comment",
    "# {0} - user",
    "# {1} - accident",
    "# {2} - development",
    "# {3} - date",
    "# {4} - message",
    "MSG.CommentAuditor.Added=ADDED [{0}; {1}/{2}] {3, date, long}: {4}",
    "# {0} - user",
    "# {1} - accident",
    "# {2} - development",
    "# {3} - date",
    "# {4} - message",
    "MSG.CommentAuditor.Deleted=DELETED [{0}; {1}/{2}] {3, date, long}: {4}"
})
@Auditor.Registration(200)
public class CommentAuditor extends AbstractAuditor<TriangleComment> {

    public CommentAuditor() {
        factory.setType(Bundle.LBL_CommentAuditor_TypeName());
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
        int accident = current.getAccidentPeriod() + 1;
        int development = current.getDevelopmentPeriod() + 1;
        Date date = current.getCreationDate();
        String user = current.getUserName();
        String msg = current.getCommentText();
        return Bundle.MSG_CommentAuditor_Added(user, accident, development, date, msg);
    }

    @Override
    protected String getChange(TriangleComment previous, TriangleComment current) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getDeleteChange(TriangleComment current) {
        int accident = current.getAccidentPeriod() + 1;
        int development = current.getDevelopmentPeriod() + 1;
        Date date = current.getCreationDate();
        String user = current.getUserName();
        String msg = current.getCommentText();
        return Bundle.MSG_CommentAuditor_Deleted(user, accident, development, date, msg);
    }
}
