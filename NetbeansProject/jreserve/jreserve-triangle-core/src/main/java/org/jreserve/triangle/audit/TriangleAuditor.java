package org.jreserve.triangle.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.triangle.entities.*;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TriangleAuditor.Created=Created",
    "MSG.TriangleAuditor.Deleted=Deleted",
    "MSG.TriangleAuditor.Saved=Saved. ",
    "# {0} - old name",
    "# {1} - new name",
    "MSG.TriangleAuditor.NameChange=Name changed \"{0}\" =&gt; \"{1}\".\n",
    "MSG.TriangleAuditor.DescriptionChange=Description changed.\n",
    "# {0} - old geometry",
    "# {1} - new geometry",
    "MSG.TriangleAuditor.GeometryChange=Geometry changed: \"{0}\" =&gt; \"{1}\".\n",
    "MSG.TriangleAuditor.TypeName.Triangle=Triangle",
    "MSG.TriangleAuditor.TypeName.Vector=Vector"
})
@Auditor.Registration(50)
public class TriangleAuditor extends AbstractAuditor<Triangle>{

    private final static String GEOMETRY_FORMAT = "[%tF] [%d, %d] / [%d, %d]";
    
    public TriangleAuditor() {
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
        setFactoryName(current);
        return Bundle.MSG_TriangleAuditor_Created();
    }
    
    protected void setFactoryName(Triangle current) {
        if(current==null || current.isTriangle())
            factory.setType(Bundle.MSG_TriangleAuditor_TypeName_Triangle());
        else
            factory.setType(Bundle.MSG_TriangleAuditor_TypeName_Vector());
    }

    @Override
    protected String getDeleteChange(Triangle current) {
        setFactoryName(current);
        return Bundle.MSG_TriangleAuditor_Deleted();
    }

    @Override
    protected String getChange(Triangle previous, Triangle current) {
        setFactoryName(current);
        //StringBuilder sb = new StringBuilder(Bundle.MSG_TriangleAuditor_Saved());
        StringBuilder sb = new StringBuilder("");
        appendChange(sb, getNameChange(previous, current));
        appendChange(sb, getDescriptionChange(previous, current));
        appendChange(sb, getGeometryChange(previous, current));
        appendChange(sb, getModifications(previous, current));
        appendChange(sb, getComments(previous, current));
        return sb.toString();
    }
    
    private String getNameChange(Triangle previous, Triangle current) {
        String n1 = previous.getName();
        String n2 = current.getName();
        if(equals(n1, n2))
            return "";
        return Bundle.MSG_TriangleAuditor_NameChange(n1, n2);
    }
    
    private boolean equals(String n1, String n2) {
        if(n1 == null)
            return n2 == null;
        return n1.equals(n2);
    }
    
    private String getDescriptionChange(Triangle previous, Triangle current) {
        if(equals(previous.getDescription(), current.getDescription()))
            return "";
        return Bundle.MSG_TriangleAuditor_DescriptionChange();
    }
    
    private String getGeometryChange(Triangle previous, Triangle current) {
        TriangleGeometry g1 = previous.getGeometry();
        TriangleGeometry g2 = current.getGeometry();
        if(g1.isEqualGeometry(g2))
            return "";
        return Bundle.MSG_TriangleAuditor_GeometryChange(toString(g1), toString(g2));
    }
    
    private String toString(TriangleGeometry g) {
        return String.format(GEOMETRY_FORMAT, 
              g.getStartDate(), g.getAccidentPeriods(), g.getAccidentMonths(),
              g.getDevelopmentPeriods(), g.getDevelopmentMonths());
    }
    
    private String getModifications(Triangle previous, Triangle current) {
        List<TriangleModification> prevs = getModifications(previous);
        List<TriangleModification> currents = getModifications(current);
        
        List<TriangleModification> deleted = getDifference(prevs, currents);
        List<TriangleModification> added = getDifference(currents, prevs);
        String result = "";
        if(!deleted.isEmpty() || !added.isEmpty()) {
            result += "Modifications:\n";
            for(TriangleModification mod : deleted)
                result += "\t- Deleted: "+mod.createAuditRepresentation()+".\n";
            for(TriangleModification mod : added)
                result += "\t- Added: "+mod.createAuditRepresentation()+".\n";
        }
        return result;
    }
    
    private List<TriangleModification> getModifications(Triangle triangle) {
        if(triangle == null)
            return Collections.EMPTY_LIST;
        return triangle.getModifications();
    }
    
    private <T> List<T> getDifference(List<T> prevs, List<T> currents) {
        List<T> deleted = new ArrayList<T>();
        for(T prev : prevs)
            if(!currents.contains(prev))
                deleted.add(prev);
        return deleted;
    }
    
    private String getComments(Triangle previous, Triangle current) {
        List<TriangleComment> prevs = getComments(previous);
        List<TriangleComment> currents = getComments(current);
        
        List<TriangleComment> deleted = getDifference(prevs, currents);
        List<TriangleComment> added = getDifference(currents, prevs);
        String result = "";
        if(!deleted.isEmpty() || !added.isEmpty()) {
            result += "Comments:\n";
            for(TriangleComment comment : deleted)
                result += "\t- Deleted: "+ getCommentText(comment) +".\n";
            for(TriangleComment comment : added)
                result += "\t- Added: "+ getCommentText(comment)+".\n";
        }
        return result;
    }
    
    private List<TriangleComment> getComments(Triangle triangle) {
        if(triangle == null)
            return Collections.EMPTY_LIST;
        return triangle.getComments();
    }
    
    private String getCommentText(TriangleComment comment) {
        String msg = "created by '%s' for cell [%d; %d] at '%tF'";
        TriangleCell cell = comment.getTriangleCell();
        return String.format(msg, comment.getUserName(), 
                cell.getAccident() + 1, cell.getDevelopment() + 1,
                comment.getCreationDate());
    }
    
    private void appendChange(StringBuilder sb, String change) {
        if(change == null)
           return;
        //if(sb.length() > 0)
        //    sb.append("\n");
        sb.append(change);
    }
}
