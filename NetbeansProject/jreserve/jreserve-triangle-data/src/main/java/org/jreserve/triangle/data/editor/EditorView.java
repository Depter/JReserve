package org.jreserve.triangle.data.editor;

import org.jreserve.navigator.NavigablePanel;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.project.system.visual.ProjectElementEditPanel;
import org.jreserve.triangle.data.project.TriangleProjectElement;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.EditorView.Triangle=Triangle",
    "LBL.EditorView.Vector=Vector",
    "MSG.EditorView.Validator.Name.Empty=Field 'Name' is empty.",
    "MSG.EditorView.Validator.Name.Exists=Name already exists."
})
class EditorView extends NavigablePanel {
    
    private static String getTitle(TriangleProjectElement element) {
        if(element.getValue().isTriangle())
            return Bundle.LBL_EditorView_Triangle();
        return Bundle.LBL_EditorView_Vector();
    }
    
    private ProjectElementEditPanel editPanel;

    EditorView(TriangleProjectElement element) {
        super(getTitle(element), Editor.getImage(element));
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
            ProjectElementContainer container = getContainer();
            if(container.containsName(name)) {
                panel.showError(Bundle.MSG_EditorView_Validator_Name_Exists());
                return false;
            }
            return true;
        }
        
        private ProjectElementContainer getContainer() {
            ProjectElement parent = panel.getElement().getParent();
            return (ProjectElementContainer) parent.getValue();
        }

        @Override
        public boolean isDescriptionValid(String description) {
            return true;
        }
    }    
}