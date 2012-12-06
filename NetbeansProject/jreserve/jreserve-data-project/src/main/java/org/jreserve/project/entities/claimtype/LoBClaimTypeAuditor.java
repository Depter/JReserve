package org.jreserve.project.entities.claimtype;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name",
    "MSG.LoBClaimTypeAuditor.Created=Created \"{0}\"",
    "# {0} - name",
    "MSG.LoBClaimTypeAuditor.Deleted=Deleted \"{0}\""
})
@Auditor.Registration(200)
public class LoBClaimTypeAuditor extends ClaimTypeAuditor {
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof LoB);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        LoB lob = (LoB) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ClaimType.class, false, true)
              .add(AuditEntity.relatedId("lob").eq(lob.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(ClaimType current) {
        String name = current.getName();
        return Bundle.MSG_LoBClaimTypeAuditor_Created(name);
    }

    @Override
    protected String getDeleteChange(ClaimType current) {
        String name = current.getName();
        return Bundle.MSG_LoBClaimTypeAuditor_Deleted(name);
    }
}
