package org.jreserve.triangle.audit;

import java.util.Collections;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.Project;
import org.jreserve.triangle.entities.Triangle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - name",
    "MSG.ProjectTriangleAuditor.Created=Created triangle \"{0}\"",
    "# {0} - name",
    "MSG.ProjectTriangleAuditor.Deleted=Deleted triangle \"{0}\"",
    "# {0} - name",
    "# {1} - change",
    "MSG.ProjectTriangleAuditor.Change=Triange \"{0}\": {1}"
})
@Auditor.Registration(100)
public class ProjectTriangleAuditor extends TriangleAuditor {
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        List<Object[]> data = queryData(reader, (Project) value);
        Collections.sort(data, DataComparator.COMPARATOR);
        return data;
    }
    
    private List<Object[]> queryData(AuditReader reader, Project project) {
        return reader.createQuery()
              .forRevisionsOfEntity(Triangle.class, false, true)
              .add(AuditEntity.relatedId("project").eq(project.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(Triangle current) {
        setFactoryName(current);
        String name = current.getName();
        return Bundle.MSG_ProjectTriangleAuditor_Created(name);
    }

    @Override
    protected String getDeleteChange(Triangle current) {
        setFactoryName(current);
        String name = current.getName();
        return Bundle.MSG_ProjectTriangleAuditor_Deleted(name);
    }
    

    @Override
    protected String getChange(Triangle previous, Triangle current) {
        setFactoryName(current);
        String change = super.getChange(previous, current);
        String name = previous.getName();
        return Bundle.MSG_ProjectTriangleAuditor_Change(name, change);
    }
}
