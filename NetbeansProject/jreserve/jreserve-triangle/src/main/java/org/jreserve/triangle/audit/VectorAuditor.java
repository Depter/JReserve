package org.jreserve.triangle.audit;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.VectorAuditor.Created=Vector created",
    "MSG.VectorAuditor.Deleted=Vector deleted",
    "MSG.VectorAuditor.Saved=Saved. ",
    "# {0} - old name",
    "# {1} - new name",
    "MSG.VectorAuditor.NameChange=Vector name changed \"{0}\" => \"{1}\".",
    "MSG.VectorAuditor.DescriptionChange=Vector description changed",
    "# {0} - old geometry",
    "# {1} - new geometry",
    "MSG.VectorAuditor.GeometryChange=Vector geometry changed \"{0}\" => \"{1}\".",
    "MSG.VectorAuditor.TypeName=Vector"
})
@Auditor.Registration(200)
public class VectorAuditor extends AbstractAuditor<Vector>{

    private final static String GEOMETRY_FORMAT = "[%tF, %d, %d]";
    
    public VectorAuditor() {
        factory.setType(Bundle.MSG_VectorAuditor_TypeName());
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Vector);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        Vector vector = (Vector) value;
        return reader.createQuery()
              .forRevisionsOfEntity(Vector.class, false, true)
              .add(AuditEntity.id().eq(vector.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(Vector current) {
        return Bundle.MSG_VectorAuditor_Created();
    }

    @Override
    protected String getDeleteChange(Vector current) {
        return Bundle.MSG_VectorAuditor_Deleted();
    }

    @Override
    protected String getChange(Vector previous, Vector current) {
        StringBuilder sb = new StringBuilder(Bundle.MSG_VectorAuditor_Saved());
        appendChange(sb, getNameChange(previous, current));
        appendChange(sb, getDescriptionChange(previous, current));
        appendChange(sb, getGeometryChange(previous, current));
        return sb.toString();
    }
    
    private String getNameChange(Vector previous, Vector current) {
        String n1 = previous.getName();
        String n2 = current.getName();
        if(equals(n1, n2))
            return null;
        return Bundle.MSG_VectorAuditor_NameChange(n1, n2);
    }
    
    private boolean equals(String n1, String n2) {
        if(n1 == null)
            return n2 == null;
        return n1.equals(n2);
    }
    
    private String getDescriptionChange(Vector previous, Vector current) {
        if(equals(previous.getDescription(), current.getDescription()))
            return null;
        return Bundle.MSG_VectorAuditor_DescriptionChange();
    }
    
    private String getGeometryChange(Vector previous, Vector current) {
        VectorGeometry g1 = previous.getGeometry();
        VectorGeometry g2 = current.getGeometry();
        if(g1.isEqualGeometry(g2))
            return null;
        return Bundle.MSG_VectorAuditor_GeometryChange(toString(g1), toString(g2));
    }
    
    private String toString(VectorGeometry g) {
        return String.format(GEOMETRY_FORMAT, 
              g.getStartDate(), g.getAccidentPeriods(), g.getAccidentMonths());
    }
    
    private void appendChange(StringBuilder sb, String change) {
        if(change == null)
           return;
        if(sb.length() > 0)
            sb.append("; ");
        sb.append(change);
    }
}

