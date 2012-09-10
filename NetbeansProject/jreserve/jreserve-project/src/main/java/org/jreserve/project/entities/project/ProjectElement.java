package org.jreserve.project.entities.project;

import javax.swing.SwingUtilities;
import org.jreserve.project.entities.ChangeLog.Type;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.entities.project.editor.ProjectEditor;
import org.jreserve.project.system.management.PersistentDeletable;
import org.jreserve.project.system.management.PersistentSavable;
import org.jreserve.project.system.management.RenameableProjectElement;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
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
    "LOG.ProjectElement.rename=Renamed from \"{0}\" to \"{1}\".",
    "LOG.ProjectElement.description=Description edited."
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
        addToLookup(new ProjectRenamable());
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
            setDescription((String) property);
        super.setProperty(property, value);
    }
    
    private void setName(String newName) {
        getValue().setName(newName);
    }
    
    private void setDescription(String newDescription) {
        getValue().setDescription(newDescription);
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
    
    private class ProjectRenamable extends RenameableProjectElement {
        
        private ProjectRenamable() {
            super(ProjectElement.this);
        }

        @Override
        protected void setNewName(String newName) {
            String oldName = getValue().getName();
            super.setNewName(newName);
            makeLog(oldName, newName);
            addSavableToLookup();
        }
        
        private void makeLog(String oldName, String newName) {
            String msg = Bundle.LOG_ProjectElement_rename(oldName, newName);
            ChangeLogUtil util = ChangeLogUtil.getDefault();
            util.addChange(getValue(), Type.PROJECT, msg);
            util.saveLogs(getValue());
        }
    
        private void addSavableToLookup() {
            Savable s = getLookup().lookup(Savable.class);
            if(s == null)
                addToLookup(new PersistentSavable(ProjectElement.this));
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
