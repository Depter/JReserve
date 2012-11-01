package org.jreserve.triangle.editor;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.visual.ProjectElementEditPanel;
import org.netbeans.api.actions.Savable;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EditorView implements MultiViewElement {

    private JToolBar toolbar = new JToolBar();
    private ProjectElementEditPanel editPanel;
    private MultiViewElementCallback callBack;
    
    private Lookup.Result<Savable> savableResult;
    
    
    EditorView(ProjectElement element) {
        editPanel = new ProjectElementEditPanel(element, new Validator(), true);
        savableResult = element.getLookup().lookupResult(Savable.class);
        savableResult.addLookupListener(new SavableListener());
    }
    
    @Override
    public JComponent getVisualRepresentation() {
        return editPanel;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        if(callBack == null)
            return new Action[0];
        return callBack.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        return editPanel.getLookup();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        editPanel.close();
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return editPanel.getUndoRedo();
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        this.callBack = mvec;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return MultiViewFactory.createUnsafeCloseState("not-saved", null, null);
        //return CloseOperationState.STATE_OK;
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
            panel.showError("Name is empty!");
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
                panel.showError("Name already used!");
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
    
    private class SavableListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            boolean changed = !savableResult.allClasses().isEmpty();
            setHtmlDisplayName(changed);
        }
    
        private void setHtmlDisplayName(boolean isChanged) {
            if(callBack == null)
                return;
            TopComponent tc = callBack.getTopComponent();
            if(isChanged) 
                tc.setHtmlDisplayName(getChangedHtmlDisplayName());
            else
                tc.setHtmlDisplayName(getUnchangedHtmlDisplayName());
        }
    
        private String getChangedHtmlDisplayName() {
            return getHtmlDisplayName("<b>%s</b>");
        }
    
        private String getHtmlDisplayName(String format) {
            String name = (String) editPanel.getElement().getProperty(ProjectElement.NAME_PROPERTY);
            format = "<html>"+format+"</html>";
            return String.format(format, name);
        }
    
        private String getUnchangedHtmlDisplayName() {
            return getHtmlDisplayName("%s");
        }
    }
}
