package org.jreserve.project.entities.project;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - name",
    "MSG.ClaimTypeProjectAuditor.Created=Created project \"{0}\"",
    "# {0} - name",
    "MSG.ClaimTypeProjectAuditor.Deleted=Deleted project \"{0}\""
})
@Auditor.Registration(200)
public class ClaimTypeProjectAuditor extends ProjectAuditor {
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        ClaimType ct = (ClaimType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(Project.class, false, true)
              .add(AuditEntity.relatedId("claimType").eq(ct.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(Project current) {
        String name = current.getName();
        return Bundle.MSG_ClaimTypeProjectAuditor_Created(name);
    }

    @Override
    protected String getDeleteChange(Project current) {
        String name = current.getName();
        return Bundle.MSG_ClaimTypeProjectAuditor_Deleted(name);
    }

}
