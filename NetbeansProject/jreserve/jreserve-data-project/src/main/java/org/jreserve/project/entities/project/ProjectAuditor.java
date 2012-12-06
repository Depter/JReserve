package org.jreserve.project.entities.project;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.project.entities.Project;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.ProjectAuditor.TypeName=Project",
    "MSG.ProjectAuditor.Created=Created",
    "MSG.ProjectAuditor.Deleted=Deleted",
    "# {0} - old name",
    "# {1} - new name",
    "MSG.ProjectAuditor.NameChange=Name changed \"{0}\" => \"{1}\"."
})
@Auditor.Registration(100)
public class ProjectAuditor extends AbstractAuditor<Project> {

    public ProjectAuditor() {
        factory.setType(Bundle.MSG_ProjectAuditor_TypeName());
    }
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        Project ct = (Project) value;
        return reader.createQuery()
              .forRevisionsOfEntity(Project.class, false, true)
              .add(AuditEntity.id().eq(ct.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(Project current) {
        return Bundle.MSG_ProjectAuditor_Created();
    }

    @Override
    protected String getDeleteChange(Project current) {
        return Bundle.MSG_ProjectAuditor_Deleted();
    }

    @Override
    protected String getChange(Project previous, Project current) {
        String oldName = previous.getName();
        String newName = current.getName();
        return Bundle.MSG_ProjectAuditor_NameChange(oldName, newName);
    }

}
