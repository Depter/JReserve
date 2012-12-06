package org.jreserve.triangle.smoothing.audit;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.smoothing.Smoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.SmoothingAuditor.TypeName=Smoothing",
    "# {0} - name",
    "MSG.SmoothingAuditor.Created=Created \"{0}\".",
    "# {0} - name",
    "MSG.SmoothingAuditor.Deleted=Deleted \"{0}\".",
    "MSG.SmoothingAuditor.Change=Changed."
})
public abstract class SmoothingAuditor<T extends Smoothing> extends AbstractAuditor<T> {
    private Class<T> clazz;
    
    protected SmoothingAuditor(Class<T> clazz) {
        this.clazz = clazz;
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
    protected String getAddChange(Smoothing current) {
        String name = current.getName();
        return Bundle.MSG_SmoothingAuditor_Created(name);
    }
    
    @Override
    protected String getDeleteChange(Smoothing current) {
        String name = current.getName();
        return Bundle.MSG_SmoothingAuditor_Deleted(name);
    }

    @Override
    protected String getChange(Smoothing previous, Smoothing current) {
        return Bundle.MSG_SmoothingAuditor_Change();
    }
}
