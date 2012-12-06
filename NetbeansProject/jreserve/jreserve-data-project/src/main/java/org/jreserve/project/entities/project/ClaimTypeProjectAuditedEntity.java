package org.jreserve.project.entities.project;

import java.awt.Image;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditedEntityFactory;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.AuditedEntityFactory;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
@AuditedEntityFactory.Registration(200)
public class ClaimTypeProjectAuditedEntity extends AbstractAuditedEntityFactory<Project> {
    
    private final static Image IMG = ImageUtilities.loadImage("resources/project.png", false);

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }

    @Override
    protected List<Project> getEntities(AuditReader reader, Object value) {
        ClaimType ct = (ClaimType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(Project.class, true, true)
              .add(AuditEntity.revisionType().eq(RevisionType.ADD))
              .add(AuditEntity.relatedId("claimType").eq(ct.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected AuditedEntity<Project> createAuditedEntity(Project entity) {
        String name = entity.getName();
        return new AuditedEntity<Project>(entity, name, IMG);
    }
}
