package org.jreserve.triangle.util;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class TriangleModificationAuditor<E> extends AbstractAuditor<E> {

    private final Class<E> clazz;
    
    public TriangleModificationAuditor(Class<E> clazz, String typeName) {
        this.clazz = clazz;
        factory.setType(typeName);
    }

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof PersistentObject);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        PersistentObject owner = (PersistentObject) value;
        return reader.createQuery()
              .forRevisionsOfEntity(clazz, false, true)
              .add(AuditEntity.property("ownerId").eq(owner.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getChange(E previous, E current) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
