package org.jreserve.project.entities.lob;

import java.awt.Image;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditedEntityFactory;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.AuditedEntityFactory;
import org.jreserve.project.entities.LoB;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@AuditedEntityFactory.Registration(100)
public class RootLoBAuditedEntityFactory extends AbstractAuditedEntityFactory<LoB> {

    private final static Image IMG = ImageUtilities.loadImage("resources/lob.png", false);
    
    @Override
    public boolean isInterested(Object value) {
        return (value == null);
    }

    @Override
    protected List<LoB> getEntities(AuditReader reader, Object value) {
        return reader.createQuery()
                .forRevisionsOfEntity(LoB.class, true, true)
                .add(AuditEntity.revisionType().eq(RevisionType.ADD))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();
    }
    
    @Override
    protected AuditedEntity<LoB> createAuditedEntity(LoB entity) {
        String name = entity.getName();
        return new AuditedEntity<LoB>(entity, name, IMG);
    }
}
