package org.jreserve.audit;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.envers.AuditReader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractAuditedEntityFactory<T> implements AuditedEntityFactory {

    @Override
    public List<AuditedEntity> getAuditedEntities(AuditReader reader, Object value) {
        List<AuditedEntity> auditedEntities = new ArrayList<AuditedEntity>();
        for(T entity : getEntities(reader, value))
            auditedEntities.add(createAuditedEntity(entity));
        return auditedEntities;
    }
    
    protected abstract List<T> getEntities(AuditReader reader, Object value);
    
    protected abstract AuditedEntity<T> createAuditedEntity(T entity);
}
