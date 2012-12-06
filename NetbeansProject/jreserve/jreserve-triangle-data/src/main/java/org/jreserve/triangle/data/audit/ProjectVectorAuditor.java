package org.jreserve.triangle.data.audit;

import java.util.Collections;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.Project;
import org.jreserve.triangle.data.entities.Vector;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - name",
    "MSG.ProjectVectorAuditor.Created=Created vector \"{0}\"",
    "# {0} - name",
    "MSG.ProjectVectorAuditor.Deleted=Deleted vector \"{0}\"",
    "# {0} - name",
    "# {1} - change",
    "MSG.ProjectVectorAuditor.Change=Vector \"{0}\": {1}"
})
@Auditor.Registration(200)
public class ProjectVectorAuditor extends VectorAuditor {
    
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
              .forRevisionsOfEntity(Vector.class, false, true)
              .add(AuditEntity.relatedId("project").eq(project.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(Vector current) {
        String name = current.getName();
        return Bundle.MSG_ProjectVectorAuditor_Created(name);
    }

    @Override
    protected String getDeleteChange(Vector current) {
        String name = current.getName();
        return Bundle.MSG_ProjectVectorAuditor_Deleted(name);
    }
    

    @Override
    protected String getChange(Vector previous, Vector current) {
        String change = super.getChange(previous, current);
        String name = previous.getName();
        return Bundle.MSG_ProjectVectorAuditor_Change(name, change);
    }
}

