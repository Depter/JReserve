package org.jreserve.data.projectdatatype;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.data.entities.DataLog;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 */
@Auditor.Registration(150)
public class ClaimTypeDataLogAuditor extends AbstractAuditor<DataLog> {

    public ClaimTypeDataLogAuditor() {
        factory.setType("Data log");
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        ClaimType ct = (ClaimType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(DataLog.class, false, true)
              .add(AuditEntity.relatedId("claimType").eq(ct.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(DataLog current) {
        return current.getLog();
    }

    @Override
    protected String getDeleteChange(DataLog current) {
        return current.getLog();
    }

    @Override
    protected String getChange(DataLog previous, DataLog current) {
        return current.getLog();
    }
}
