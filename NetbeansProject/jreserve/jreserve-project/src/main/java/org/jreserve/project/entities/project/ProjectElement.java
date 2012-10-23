package org.jreserve.project.entities.project;

import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.entities.project.editor.ProjectEditor;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.netbeans.api.actions.Openable;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - the old name",
    "# {1} - the new name",
    "LOG.ProjectElement.rename=Project renamed from \"{0}\" to \"{1}\".",
    "LOG.ProjectElement.description.change=Project description changed."
})
public class ProjectElement extends org.jreserve.project.system.ProjectElement<Project> {

    public ProjectElement(Project project) {
        super(project);
        super.setProperty(NAME_PROPERTY, project.getName());
        initLookupContent();
    }
    
    private void initLookupContent() {
        addToLookup(new ProjectDeletable());
        addToLookup(new ProjectOpenable());
        addToLookup(new RenameableProjectElement(this));
        addToLookup(new AuditableProjectElement(this));
        new ProjectSavable();
    }
    
    @Override
    public Node createNodeDelegate() {
        return new ProjectNode(this);
    }
    
    @Override
    public void setProperty(String property, Object value) {
        if(NAME_PROPERTY.equals(property))
            setName((String) value);
        else if(DESCRIPTION_PROPERTY.equals(property))
            setDescription((String) value);
        super.setProperty(property, value);
    }
    
    private void setName(String newName) {
        getValue().setName(newName);
    }
    
    private void setDescription(String newDescription) {
        getValue().setDescription(newDescription);
    }
    
    private class ProjectDeletable extends PersistentDeletable {

        private Project project;
        
        private ProjectDeletable() {
            super(ProjectElement.this);
            this.project = getValue();
        }
        
        @Override
        protected void cleanUpAfterEntity(Session session) {
            closeEditor();
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
    }
    
    private class ProjectSavable extends PersistentSavable<Project> {

        public ProjectSavable() {
            super(ProjectElement.this);
        }
        
        @Override
        protected void initOriginalProperties() {
            Project project = element.getValue();
            originalProperties.put(NAME_PROPERTY, project.getName());
            originalProperties.put(DESCRIPTION_PROPERTY, project.getDescription());
        }
    }
    
    private class ProjectOpenable implements Openable {
        
        private TopComponent editor;
        
        @Override
        public void open() {
            if(editor == null)
                editor = ProjectEditor.createTopComponent(ProjectElement.this);
            if(!editor.isOpened())
                editor.open();
            editor.requestActive();
        }
    }
}
