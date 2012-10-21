package org.jreserve.project.entities.claimtype;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AuditElement;
import org.jreserve.audit.Auditor;
import org.jreserve.audit.JReserveRevisionEntity;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;

/**
 *
 * @author Peter Decsi
 */
@Auditor.Registration(200)
public class LoBClaimTypeAuditor implements Auditor {

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof LoB);
    }

    @Override
    public List<AuditElement> getAudits(AuditReader reader, Object value) {
        List<AuditElement> elements = new ArrayList<AuditElement>();
        ClaimType previous = null;
        for(Object[] revision : getRevisions(reader, (LoB) value)) {
            ClaimType current = (ClaimType) revision[0];
            JReserveRevisionEntity re = (JReserveRevisionEntity) revision[1];
            RevisionType type = (RevisionType) revision[2];
            elements.add(createAuditElement(previous, current, re, type));
            previous = current;
        }
        return elements;
    }
    
    private List<Object[]> getRevisions(AuditReader reader, LoB lob) {
        return reader.createQuery()
              .forRevisionsOfEntity(ClaimType.class, false, false)
              .add(AuditEntity.relatedId("lob").eq(lob.getId()))
              .addOrder(AuditEntity.property("id").asc())
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }
    
    private AuditElement createAuditElement(ClaimType previous, ClaimType current, JReserveRevisionEntity re, RevisionType type) {
        String change = getChange(previous, current, type);
        return new AuditElement(re.getRevisionDate(), re.getUserName(), change);
    }
    
    private String getChange(ClaimType previous, ClaimType current, RevisionType type) {
        switch(type) {
            case ADD:
                return "Created claim type: "+current.getName();
            case DEL:
                return "Deleted claim type: "+previous.getName();
            case MOD:
                String msg = "Name changed \"%s\" => \"%s\".";
                return String.format(msg, previous.getName(), current.getName());
            default:
                throw new IllegalArgumentException("Unknown RevisionType: "+type);
        }
    }
}
