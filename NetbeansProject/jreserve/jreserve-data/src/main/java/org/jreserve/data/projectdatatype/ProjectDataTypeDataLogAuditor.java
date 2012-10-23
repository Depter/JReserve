package org.jreserve.data.projectdatatype;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.DataLog;

/**
 *
 * @author Peter Decsi
 */
@Auditor.Registration(100)
public class ProjectDataTypeDataLogAuditor extends AbstractAuditor<DataLog> {

    public ProjectDataTypeDataLogAuditor() {
        factory.setType("Data log");
    }

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ProjectDataType);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        ProjectDataType dt = (ProjectDataType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(DataLog.class, false, true)
              .add(AuditEntity.relatedId("dataType").eq(dt.getId()))
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
