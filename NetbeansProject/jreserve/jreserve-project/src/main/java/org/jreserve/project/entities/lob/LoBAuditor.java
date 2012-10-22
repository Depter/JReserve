package org.jreserve.project.entities.lob;

import java.util.Collections;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.LoB;

/**
 *
 * @author Peter Decsi
 */
@Auditor.Registration(100)
public class LoBAuditor extends AbstractAuditor<LoB> {

    public LoBAuditor() {
        factory.setType("LoB");
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof LoB);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        LoB lob = (LoB) value;
        return reader.createQuery()
              .forRevisionsOfEntity(LoB.class, false, true)
              .add(AuditEntity.id().eq(lob.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(LoB current) {
        return "Created";
    }

    @Override
    protected String getDeleteChange(LoB current) {
        return "Deleted";
    }

    @Override
    protected String getChange(LoB previous, LoB current) {
        String msg = "Name changed \"%s\" => \"%s\".";
        return String.format(msg, previous.getName(), current.getName());
    }

    @Override
    public List<AuditedEntity> getAuditedEntities(AuditReader reader, Object value) {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected List<LoB> getEntities(AuditReader reader, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected AuditedEntity<LoB> createAuditedEntity(LoB entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
