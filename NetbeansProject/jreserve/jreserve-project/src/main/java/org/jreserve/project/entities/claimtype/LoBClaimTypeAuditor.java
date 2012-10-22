package org.jreserve.project.entities.claimtype;

import java.awt.Image;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
@Auditor.Registration(200)
public class LoBClaimTypeAuditor extends AbstractAuditor<ClaimType> {
    
    private final static Image IMG = ImageUtilities.loadImage("resources/claim_type.png", false);

    public LoBClaimTypeAuditor() {
        factory.setType("Claim type");
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof LoB);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        LoB lob = (LoB) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ClaimType.class, false, true)
              .add(AuditEntity.relatedId("lob").eq(lob.getId()))
              .addOrder(AuditEntity.property("id").asc())
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(ClaimType current) {
        return "Created claim type: "+current.getName();
    }

    @Override
    protected String getDeleteChange(ClaimType current) {
        return "Deleted claim type: "+current.getName();
    }

    @Override
    protected String getChange(ClaimType previous, ClaimType current) {
        String msg = "Name changed \"%s\" => \"%s\".";
        return String.format(msg, previous.getName(), current.getName());
    }

    @Override
    protected List<ClaimType> getEntities(AuditReader reader, Object value) {
        LoB lob = (LoB) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ClaimType.class, false, true)
              .add(AuditEntity.revisionType().eq(RevisionType.DEL))
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
