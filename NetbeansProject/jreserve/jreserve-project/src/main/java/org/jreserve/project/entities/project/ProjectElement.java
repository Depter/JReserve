package org.jreserve.project.entities.project;

import javax.swing.SwingUtilities;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.entities.project.editor.ProjectEditor;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentRenameable;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Closable;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

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
public class ProjectElement extends org.jreserve.project.system.ProjectElement<Project> {

    public ProjectElement(Project project) {
        super(project);
        super.setProperty(NAME_PROPERTY, project.getName());
        initLookupContent();
    }
    
    private void initLookupContent() {
        addToLookup(getValue().getChanges());
        addToLookup(new ProjectDeletable());
        addToLookup(new ProjectRenameable());
        addToLookup(new ProjectOpenable());
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
            closeEditor();
            Project project = getValue();
            ClaimType ct = project.getClaimType();
            ct.removeProject(project);
        }
        
        private void closeEditor() {
            ProjectOpenable openable = getLookup().lookup(ProjectOpenable.class);
            if(openable != null && openable.editor != null)
                closeEditor(openable.editor);
        }
        
        private void closeEditor(final TopComponent editor) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    editor.close();
                }
            });
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
    
    private class ProjectOpenable implements Openable {
        
        private TopComponent editor;
        
        @Override
        public void open() {
            if(editor == null) {
                editor = ProjectEditor.createTopComponent(ProjectElement.this);
                editor.open();
            }
            editor.requestActive();
        }
    }
}
