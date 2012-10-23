package org.jreserve.triangle.audit;

import java.awt.Image;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditedEntityFactory;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.AuditedEntityFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.triangle.entities.Vector;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
@AuditedEntityFactory.Registration(200)
public class ProjectVectorAuditedEntityFactory extends AbstractAuditedEntityFactory<Vector> {
    
    private final static Image IMG = ImageUtilities.loadImage("resources/vector.png", false);

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    protected List<Vector> getEntities(AuditReader reader, Object value) {
        Project project = (Project) value;
        return reader.createQuery()
              .forRevisionsOfEntity(Vector.class, true, true)
              .add(AuditEntity.revisionType().eq(RevisionType.ADD))
              .add(AuditEntity.relatedId("project").eq(project.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected AuditedEntity<Vector> createAuditedEntity(Vector entity) {
        String name = entity.getName();
        return new AuditedEntity<Vector>(entity, name, IMG);
    }
}
