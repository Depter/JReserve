package org.jreserve.project.entities.project;

import java.awt.Image;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.audit.AuditableProjectElement;
import org.jreserve.persistence.visual.PersistentOpenable;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.entities.project.editor.ProjectEditor;
import org.jreserve.project.system.DefaultProjectNode;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.ProjectElementUndoRedo;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
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
    
    private final static Image PROJECT_ICON = ImageUtilities.loadImage("resources/project.png", false);
    private final static String ACTION_PATH = "JReserve/Popup/ProjectRoot-ProjectNode";

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
        addToLookup(new ProjectElementUndoRedo(this));
        new ProjectSavable();
    }
    
    @Override
    public Node createNodeDelegate() {
        return new DefaultProjectNode(this, PROJECT_ICON, ACTION_PATH);
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
    
    private class ProjectDeletable extends PersistentObjectDeletable<Project> {

        private Project project;
        private ClaimType claimType;
        
        private ProjectDeletable() {
            super(ProjectElement.this, "Project");
            this.project = getValue();
            claimType = project.getClaimType();
        }
        
        @Override
        protected void cleanUpAfterEntity(Session session) {
            closeEditor();
            claimType.removeProject(project);
        }
        
        private void closeEditor() {
            ProjectOpenable openable = getLookup().lookup(ProjectOpenable.class);
            if(openable != null)
                closeEditor(openable);
//            if(openable != null && openable.editor != null)
//                closeEditor(openable.editor);
        }
        
        private void closeEditor(final ProjectOpenable openable) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    openable.close();
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
    
    private class ProjectOpenable extends PersistentOpenable {
        
//        private TopComponent editor;
//        
//        @Override
//        public void open() {
//            if(editor == null)
//                editor = ProjectEditor.createTopComponent(ProjectElement.this);
//            if(!editor.isOpened())
//                editor.open();
//            editor.requestActive();
//        }

        @Override
        protected TopComponent createComponent() {
            return ProjectEditor.createTopComponent(ProjectElement.this);
        }
    }
}
