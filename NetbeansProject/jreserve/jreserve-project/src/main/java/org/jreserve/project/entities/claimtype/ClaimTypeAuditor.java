package org.jreserve.project.entities.claimtype;

import java.util.Collections;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.ClaimType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.ClaimTypeAuditor.TypeName=Claim type",
    "MSG.ClaimTypeAuditor.Created=Created",
    "MSG.ClaimTypeAuditor.Deleted=Deleted",
    "# {0} - old name",
    "# {1} - new name",
    "MSG.ClaimTypeAuditor.NameChange=Name changed \"{0}\" => \"{1}\"."
})
@Auditor.Registration(100)
public class ClaimTypeAuditor extends AbstractAuditor<ClaimType> {

    public ClaimTypeAuditor() {
        factory.setType(Bundle.MSG_ClaimTypeAuditor_TypeName());
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        ClaimType ct = (ClaimType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ClaimType.class, false, true)
              .add(AuditEntity.id().eq(ct.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(ClaimType current) {
        return Bundle.MSG_ClaimTypeAuditor_Created();
    }

    @Override
    protected String getDeleteChange(ClaimType current) {
        return Bundle.MSG_ClaimTypeAuditor_Deleted();
    }

    @Override
    protected String getChange(ClaimType previous, ClaimType current) {
        String oldName = previous.getName();
        String newName = current.getName();
        return Bundle.MSG_ClaimTypeAuditor_NameChange(oldName, newName);
    }
}
