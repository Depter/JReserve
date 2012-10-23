package org.jreserve.data.projectdatatype;

import java.util.List;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.jreserve.audit.AbstractAuditor;
import org.jreserve.audit.Auditor;
import org.jreserve.data.ProjectDataType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.ProjectDataTypeAuditor.TypeName=Data type",
    "MSG.ProjectDataTypeAuditor.Created=Created",
    "MSG.ProjectDataTypeAuditor.Deleted=Deleted",
    "# {0} - old name",
    "# {1} - new name",
    "MSG.ProjectDataTypeAuditor.NameChange=Name changed \"{0}\" => \"{1}\"."
})
@Auditor.Registration(100)
public class ProjectDataTypeAuditor extends AbstractAuditor<ProjectDataType> {

    public ProjectDataTypeAuditor() {
        factory.setType(Bundle.MSG_ProjectDataTypeAuditor_TypeName());
    }

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ProjectDataType);
    }
    
    @Override
    protected List<Object[]> getRevisions(AuditReader reader, Object value) {
        ProjectDataType dataType = (ProjectDataType) value;
        return reader.createQuery()
              .forRevisionsOfEntity(ProjectDataType.class, false, true)
              .add(AuditEntity.id().eq(dataType.getId()))
              .addOrder(AuditEntity.revisionNumber().asc())
              .getResultList();
    }

    @Override
    protected String getAddChange(ProjectDataType current) {
        return Bundle.MSG_ProjectDataTypeAuditor_Created();
    }

    @Override
    protected String getDeleteChange(ProjectDataType current) {
        return Bundle.MSG_ProjectDataTypeAuditor_Deleted();
    }

    @Override
    protected String getChange(ProjectDataType previous, ProjectDataType current) {
        String oldName = previous.getName();
        String newName = current.getName();
        return Bundle.MSG_ProjectDataTypeAuditor_NameChange(oldName, newName);
    }
}
