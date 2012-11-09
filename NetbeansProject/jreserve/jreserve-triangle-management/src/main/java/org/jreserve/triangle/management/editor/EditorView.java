package org.jreserve.triangle.management.editor;

import java.awt.Image;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.navigator.NavigablePanel;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.visual.ProjectElementEditPanel;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.EditorView.Validator.Name.Empty=Field 'Name' is empty.",
    "MSG.EditorView.Validator.Name.Exists=Name already exists."
})
class EditorView extends NavigablePanel {

    private ProjectElementEditPanel editPanel;

    EditorView(String title, Image img, ProjectElement element) {
        super(title, img);
        editPanel = new ProjectElementEditPanel(element, new Validator(), true);
        super.setContent(editPanel);
    }
    

    private static class Validator implements ProjectElementEditPanel.Validator {

        private ProjectElementEditPanel panel;
        
        @Override
        public void setPanel(ProjectElementEditPanel panel) {
            this.panel = panel;
        }
        
        @Override
        public boolean isNameValid(String name) {
            return nameNotEmpty(name) && newName(name);
        }
        
        private boolean nameNotEmpty(String name) {
            if(name!=null && name.trim().length()>0)
                return true;
            panel.showError(Bundle.MSG_EditorView_Validator_Name_Empty());
            return false;
        }
        
        private boolean newName(String name) {
            String elementName = (String) panel.getElement().getProperty(ProjectElement.NAME_PROPERTY);
            if(elementName.equals(name))
                return !elementName.equalsIgnoreCase(name);
            return isNewNameInContainer(name);
        }
        
        private boolean isNewNameInContainer(String name) {
            ProjectDataContainer container = getContainer();
            if(container.containsName(name)) {
                panel.showError(Bundle.MSG_EditorView_Validator_Name_Exists());
                return false;
            }
            return true;
        }
        
        private ProjectDataContainer getContainer() {
            ProjectElement parent = panel.getElement().getParent();
            return (ProjectDataContainer) parent.getValue();
        }

        @Override
        public boolean isDescriptionValid(String description) {
            return true;
        }
    }    
}
