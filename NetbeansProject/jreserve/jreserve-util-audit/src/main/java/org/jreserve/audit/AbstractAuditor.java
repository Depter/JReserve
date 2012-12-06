package org.jreserve.audit;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractAuditor<T> implements Auditor {

    protected AuditElementFactory factory = new AuditElementFactory();

    
    @Override
    public List<AuditElement> getAudits(AuditReader reader, Object value) {
        List<AuditElement> elements = new ArrayList<AuditElement>();
        T previous = null;
        for(Object[] revision : getRevisions(reader, value)) {
            elements.add(createAuditElement(previous, revision));
            previous = getEntity(revision);
        }
        return elements;
    }

    protected abstract List<Object[]> getRevisions(AuditReader reader, Object value);

    protected AuditElement createAuditElement(T previous, Object[] revision) {
        factory.setEntity(getRevisionEntity(revision));
        String change = getChange(previous, getEntity(revision), getRevisionType(revision));
        factory.setChange(change);
        return factory.build();
    }
    
    private JReserveRevisionEntity getRevisionEntity(Object[] revision) {
        return (JReserveRevisionEntity) revision[1];
    }
    
    private T getEntity(Object[] revision) {
        return (T) revision[0];
    }
    
    private RevisionType getRevisionType(Object[] revision) {
        return (RevisionType) revision[2];
    }
    
    protected String getChange(T previous, T current, RevisionType type) {
        switch(type) {
            case ADD:
                return getAddChange(current);
            case DEL:
                return getDeleteChange(current);
            case MOD:
                return getChange(previous, current);
            default:
                throw new IllegalArgumentException("Unknown RevisionType: "+type);
        }
    }
    
    protected abstract String getAddChange(T current);
    
    protected abstract String getDeleteChange(T current);
    
    protected abstract String getChange(T previous, T current);
}
