package org.jreserve.project.entities.lob;

import java.util.Collections;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.LoB;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.LoBAuditor.TypeName=LoB",
    "MSG.LoBAuditor.Created=Created",
    "MSG.LoBAuditor.Deleted=Deleted",
    "# {0} - old name",
    "# {1} - new name",
    "MSG.LoBAuditor.NameChange=Name changed \"{0}\" => \"{1}\"."
})
@Auditor.Registration(100)
public class LoBAuditor extends AbstractAuditor<LoB> {

    public LoBAuditor() {
        factory.setType(Bundle.MSG_LoBAuditor_TypeName());
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
        return Bundle.MSG_LoBAuditor_Created();
    }

    @Override
    protected String getDeleteChange(LoB current) {
        return Bundle.MSG_LoBAuditor_Deleted();
    }

    @Override
    protected String getChange(LoB previous, LoB current) {
        String oldName = previous.getName();
        String newName = current.getName();
        return Bundle.MSG_LoBAuditor_NameChange(oldName, newName);
    }
}
