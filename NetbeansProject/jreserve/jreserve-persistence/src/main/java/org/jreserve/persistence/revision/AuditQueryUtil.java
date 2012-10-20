package org.jreserve.persistence.revision;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.persistence.AbstractPersistentObject;
import org.jreserve.persistence.EntityAuditor;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.connection.HibernatePersistenceUnit;

/**
 *
 * @author Peter Decsi
 */
public class AuditQueryUtil {
    
    private Session session;
    private AuditReader reader;
    
    public AuditQueryUtil() {
    }
    
    public void open() {
        checkNotOpened();
        openSession();
        reader = AuditReaderFactory.get(session);
    }
    
    private void checkNotOpened() {
        if(reader != null)
            throw new IllegalStateException("AuditQueryUtil is already opened!");
    }
    
    private void openSession() {
        HibernatePersistenceUnit pu = PersistenceUtil.getLookup().lookup(HibernatePersistenceUnit.class);
        if(pu == null)
            throw new IllegalStateException("There is no appropriate persistence unit!");
        session = pu.openHibernateSession();
    }
    
    public <T extends AbstractPersistentObject> List<T> getEntityStates(T entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        String id = entity.getId();
        return getEntityStates(clazz, id);
    }
    
    public <T> List<T> getEntityStates(Class<T> clazz, Object id) {
        checkState();
        return reader.createQuery()
                .forRevisionsOfEntity(clazz, true, false)
                .add(AuditEntity.id().eq(id))
                .addOrder(AuditEntity.revisionProperty("revisionTimeStamp").asc())
                .getResultList();
    } 
        
    private void checkState() {
        if(reader == null)
            throw new IllegalStateException("AuditQueryUtil must be opened first!");
        if(session == null || !session.isOpen())
            throw new IllegalStateException("Session is not avaiable!");
    }
    
    public <T> List<T> getEntityStates(EntityAuditor<T> auditor) {
        Object id = auditor.getId();
        Class<T> clazz = (Class<T>) auditor.getEntity().getClass();
        return getEntityStates(clazz, id);
    }
    
    public <T> List<AuditElement> getChanges(EntityAuditor<T> auditor) {
        List<AuditElement> changes = new ArrayList<AuditElement>();
        T previous = null;
        for(Object[] revision : getRevisions(auditor)) {
            T current = (T) revision[0];
            JReserveRevisionEntity jre = (JReserveRevisionEntity) revision[1];
            changes.add(createChange(previous, current, jre, auditor));
            previous = current;
        }
        return changes;
    }
    
    private <T> AuditElement createChange(T previous, T current, JReserveRevisionEntity jre, EntityAuditor<T> auditor) {
        String msg = auditor.getChange(previous, current);
        Date date = jre.getRevisionDate();
        String user = jre.getUserName();
        return new AuditElement(date, user, msg);
    }
    
    public <T> List<Object[]> getRevisions(Class<T> clazz, Object id) {
        checkState();
        return reader.createQuery()
                .forRevisionsOfEntity(clazz, false, false)
                .add(AuditEntity.id().eq(id))
                .addOrder(AuditEntity.revisionProperty("revisionTimeStamp").asc())
                .getResultList();
    } 
    
    public <T> List<Object[]> getRevisions(EntityAuditor<T> auditor) {
        Object id = auditor.getId();
        Class<T> clazz = (Class<T>) auditor.getEntity().getClass();
        return getRevisions(clazz, id);
    }
    
    public <T extends AbstractPersistentObject> List<Object[]> getRevisions(T entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        String id = entity.getId();
        return getRevisions(clazz, id);
    }
    
    
    public void close() {
        if(reader != null) {
            session.close();
            session = null;
            reader = null;
        }
    }
}
