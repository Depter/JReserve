package org.jreserve.project.entities.claimtype;

import java.awt.Image;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditedEntityFactory;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.AuditedEntityFactory;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
@AuditedEntityFactory.Registration(100)
public class LoBClaimTypeAuditedEntityFactory extends AbstractAuditedEntityFactory<ClaimType> {
    
    private final static Image IMG = ImageUtilities.loadImage("resources/claim_type.png", false);
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof LoB);
    }

    @Override
    protected List<ClaimType> getEntities(AuditReader reader, Object value) {
        LoB lob = (LoB) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ClaimType.class, true, true)
              .add(AuditEntity.revisionType().eq(RevisionType.ADD))
              .add(AuditEntity.relatedId("lob").eq(lob.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected AuditedEntity<ClaimType> createAuditedEntity(ClaimType entity) {
        String name = entity.getName();
        return new AuditedEntity<ClaimType>(entity, name, IMG);
    }
}
