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
import org.jreserve.triangle.entities.Triangle;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@AuditedEntityFactory.Registration(100)
public class ProjectTriangleAuditedEntityFactory extends AbstractAuditedEntityFactory<Triangle> {
    
    private final static Image TRIANGLE_IMG = ImageUtilities.loadImage("resources/triangle.png", false);
    private final static Image VECTOR_IMG = ImageUtilities.loadImage("resources/vector.png", false);
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    protected List<Triangle> getEntities(AuditReader reader, Object value) {
        Project project = (Project) value;
        return reader.createQuery()
              .forRevisionsOfEntity(Triangle.class, true, true)
              .add(AuditEntity.revisionType().eq(RevisionType.ADD))
              .add(AuditEntity.relatedId("project").eq(project.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected AuditedEntity<Triangle> createAuditedEntity(Triangle entity) {
        String name = entity.getName();
        Image img = entity.isTriangle()? TRIANGLE_IMG : VECTOR_IMG;
        return new AuditedEntity<Triangle>(entity, name, img);
    }
}
