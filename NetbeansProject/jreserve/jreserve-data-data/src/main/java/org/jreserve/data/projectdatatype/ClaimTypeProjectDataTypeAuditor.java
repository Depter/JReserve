package org.jreserve.data.projectdatatype;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.ClaimType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - dbId",
    "# {1} - name",
    "MSG.ClaimTypeProjectDataTypeAuditor.Created=Created data type \"{0} - {1}\"",
    "# {0} - dbId",
    "# {1} - name",
    "MSG.ClaimTypeProjectDataTypeAuditor.Deleted=Deleted data type \"{0} - {1}\""
})
@Auditor.Registration(100)
public class ClaimTypeProjectDataTypeAuditor extends ProjectDataTypeAuditor {
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        ClaimType ct = (ClaimType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ProjectDataType.class, false, true)
              .add(AuditEntity.relatedId("claimType").eq(ct.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(ProjectDataType current) {
        int dbId = current.getDbId();
        String name = current.getName();
        return Bundle.MSG_ClaimTypeProjectDataTypeAuditor_Created(dbId, name);
    }

    @Override
    protected String getDeleteChange(ProjectDataType current) {
        int dbId = current.getDbId();
        String name = current.getName();
        return Bundle.MSG_ClaimTypeProjectDataTypeAuditor_Deleted(dbId, name);
    }
}
