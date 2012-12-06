package org.jreserve.triangle.data.audit;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.triangle.data.entities.Triangle;
import org.jreserve.triangle.data.entities.TriangleGeometry;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.TriangleAuditor.Created=Created",
    "MSG.TriangleAuditor.Deleted=Deleted",
    "MSG.TriangleAuditor.Saved=Saved. ",
    "# {0} - old name",
    "# {1} - new name",
    "MSG.TriangleAuditor.NameChange=Name changed \"{0}\" => \"{1}\".",
    "MSG.TriangleAuditor.DescriptionChange=Description changed",
    "# {0} - old geometry",
    "# {1} - new geometry",
    "MSG.TriangleAuditor.GeometryChange=Geometry changed \"{0}\" => \"{1}\".",
    "MSG.TriangleAuditor.TypeName=Triangle"
})
@Auditor.Registration(50)
public class TriangleAuditor extends AbstractAuditor<Triangle>{

    private final static String GEOMETRY_FORMAT = "[%tF] [%d, %d] / [%d, %d]";
    
    public TriangleAuditor() {
        factory.setType(Bundle.MSG_TriangleAuditor_TypeName());
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Triangle);
    }

    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        Triangle triangle = (Triangle) value;
        return reader.createQuery()
              .forRevisionsOfEntity(Triangle.class, false, true)
              .add(AuditEntity.id().eq(triangle.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(Triangle current) {
        return Bundle.MSG_TriangleAuditor_Created();
    }

    @Override
    protected String getDeleteChange(Triangle current) {
        return Bundle.MSG_TriangleAuditor_Deleted();
    }

    @Override
    protected String getChange(Triangle previous, Triangle current) {
        StringBuilder sb = new StringBuilder(Bundle.MSG_TriangleAuditor_Saved());
        appendChange(sb, getNameChange(previous, current));
        appendChange(sb, getDescriptionChange(previous, current));
        appendChange(sb, getGeometryChange(previous, current));
        return sb.toString();
    }
    
    private String getNameChange(Triangle previous, Triangle current) {
        String n1 = previous.getName();
        String n2 = current.getName();
        if(equals(n1, n2))
            return null;
        return Bundle.MSG_TriangleAuditor_NameChange(n1, n2);
    }
    
    private boolean equals(String n1, String n2) {
        if(n1 == null)
            return n2 == null;
        return n1.equals(n2);
    }
    
    private String getDescriptionChange(Triangle previous, Triangle current) {
        if(equals(previous.getDescription(), current.getDescription()))
            return null;
        return Bundle.MSG_TriangleAuditor_DescriptionChange();
    }
    
    private String getGeometryChange(Triangle previous, Triangle current) {
        TriangleGeometry g1 = previous.getGeometry();
        TriangleGeometry g2 = current.getGeometry();
        if(g1.isEqualGeometry(g2))
            return null;
        return Bundle.MSG_TriangleAuditor_GeometryChange(toString(g1), toString(g2));
    }
    
    private String toString(TriangleGeometry g) {
        return String.format(GEOMETRY_FORMAT, 
              g.getStartDate(), g.getAccidentPeriods(), g.getAccidentMonths(),
              g.getDevelopmentPeriods(), g.getDevelopmentMonths());
    }
    
    private void appendChange(StringBuilder sb, String change) {
        if(change == null)
           return;
        if(sb.length() > 0)
            sb.append("; ");
        sb.append(change);
    }
}
