package org.jreserve.project.entities.lob;

import java.awt.Image;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.project.entities.LoB;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RootClaimTypeAuditor extends AbstractAuditor<LoB> {

    private final static Image IMG = ImageUtilities.loadImage("resources/lob.png", false);
    
    @Override
    public boolean isInterested(Object value) {
        return (value == null);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        return reader.createQuery()
               .forRevisionsOfEntity(LoB.class, false, true)
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(LoB current) {
        return "Created lob: "+current.getName();
    }

    @Override
    protected String getDeleteChange(LoB current) {
        return "Deleted lob: "+current.getName();
    }

    @Override
    protected String getChange(LoB previous, LoB current) {
        String msg = "Name changed \"%s\" => \"%s\".";
        return String.format(msg, previous.getName(), current.getName());
    }

    @Override
    protected List<LoB> getEntities(AuditReader reader, Object value) {
        return reader.createQuery()
                .forRevisionsOfEntity(LoB.class, true, true)
                .add(AuditEntity.revisionType().eq(RevisionType.DEL))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();
    }
    
    @Override
    protected AuditedEntity<LoB> createAuditedEntity(LoB entity) {
        String name = entity.getName();
        return new AuditedEntity<LoB>(entity, name, IMG);
    }
}
