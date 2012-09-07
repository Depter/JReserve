package org.jreserve.project.entities.project;

import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentRenameable;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - the new name",
    "# {1} - the name of the parent ClaimType",
    "MSG.ProjectElement.nameexists=Name \"{0}\" in claim type \"{1}\" already exists!"
})
class ProjectElement extends org.jreserve.project.system.ProjectElement<Project> {

    public ProjectElement(Project project) {
        super(project);
        super.setProperty(NAME_PROPERTY, project.getName());
        addToLookup(new ProjectDeletable());
        addToLookup(new ProjectRenameable());
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ProjectNode(this);
    }
    
    private class ProjectDeletable extends PersistentDeletable {

        private ProjectDeletable() {
            super(ProjectElement.this);
        }
        
        @Override
        protected void cleanUpEntity() {
            Project project = getValue();
            ClaimType ct = project.getClaimType();
            ct.removeProject(project);
        }
        
        @Override
        public Node getNode() {
            return createNodeDelegate();
        }
    }
    
    private class ProjectRenameable extends PersistentRenameable {

        @Override
        protected String getEntityName() {
            return getValue().getName();
        }

        @Override
        protected boolean checkNotExists(String name) {
            ClaimType ct = getValue().getClaimType();
            for(Project project : ct.getProjects())
                if(project.getName().equalsIgnoreCase(name)) {
                    showNameExistsError(name, ct);
                    return false;
                }
            return true;
        }
        
        private void showNameExistsError(String name, ClaimType ct) {
            String ctName = ct.getName();
            String msg = Bundle.MSG_ProjectElement_nameexists(name, ctName);
            showError(msg);
        }

        @Override
        protected void setEntityName(String newName) {
            getValue().setName(newName);
            setProperty(NAME_PROPERTY, newName);
        }

        @Override
        protected Object getEntity() {
            return getValue();
        }
    }
}
