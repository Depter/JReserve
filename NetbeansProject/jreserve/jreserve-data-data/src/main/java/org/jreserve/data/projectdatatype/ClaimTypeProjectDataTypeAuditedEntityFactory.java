package org.jreserve.data.projectdatatype;

import java.awt.Image;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditedEntityFactory;
import org.jreserve.audit.AuditedEntity;
import org.jreserve.audit.AuditedEntityFactory;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.resources.images.TransparentImage;

/**
 *
 * @author Peter Decsi
 */
@AuditedEntityFactory.Registration(100)
public class ClaimTypeProjectDataTypeAuditedEntityFactory extends AbstractAuditedEntityFactory<ProjectDataType> {

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }

    @Override
    protected List<ProjectDataType> getEntities(AuditReader reader, Object value) {
        ClaimType ct = (ClaimType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ProjectDataType.class, true, true)
              .add(AuditEntity.revisionType().eq(RevisionType.ADD))
              .add(AuditEntity.relatedId("claimType").eq(ct.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected AuditedEntity<ProjectDataType> createAuditedEntity(ProjectDataType entity) {
        String name = entity.getName();
        return new AuditedEntity<ProjectDataType>(entity, name, null);
    }
}
