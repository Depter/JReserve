package org.jreserve.project.entities.project.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.project.entities.Project;
import org.jreserve.project.entities.project.ProjectElement;
import org.jreserve.project.system.management.AbstractProjectElementSavable;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.UndoRedo;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;
        
/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ProjectEditorView.name=Name:",
    "LBL.ProjectEditorView.description=Description:"
})
class ProjectEditorView extends JPanel implements MultiViewElement, DocumentListener, PropertyChangeListener {
    
    private final static String ERR_IMG = "org/netbeans/modules/dialogs/error.gif";
    
    private JToolBar toolBar = new JToolBar();
    private MultiViewElementCallback callBack;
    private ProjectElement element;

    private JTextField nameText;
    private JTextArea descriptionText;
    private JLabel msgLabel;
    
    ProjectEditorView(ProjectElement element) {
        this.element = element;
        initComponents();
        addListeners();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        add(getNamePanel(), BorderLayout.PAGE_START);
        add(getDescriptionPanel(), BorderLayout.CENTER);
        add(getMsgLabel(), BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel getNamePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(Bundle.LBL_ProjectEditorView_name()), BorderLayout.LINE_START);
        
        nameText = new JTextField(element.getValue().getName());
        panel.add(nameText, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel getDescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(Bundle.LBL_ProjectEditorView_description()), BorderLayout.PAGE_START);
        
        descriptionText = new JTextArea(element.getValue().getDescription());
        JScrollPane scroll = new JScrollPane(descriptionText);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel getMsgLabel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        ImageIcon img = ImageUtilities.loadImageIcon(ERR_IMG, false);
        msgLabel = new JLabel(img);
        msgLabel.setVisible(false);
        panel.add(msgLabel, BorderLayout.LINE_START);
        panel.add(Box.createGlue(), BorderLayout.CENTER);
        return panel;
    }
    
    private void addListeners() {
        nameText.getDocument().addDocumentListener(this);
        descriptionText.getDocument().addDocumentListener(this);
        element.addPropertyChangeListener(WeakListeners.propertyChange(this, element));
    }
    
    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        callBack = mvec;
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        if(callBack == null)
            return new Action[0];
        return callBack.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        return element.getLookup();
    }

    @Override public void componentOpened() {}
    @Override public void componentClosed() {}
    @Override public void componentShowing() {}
    @Override public void componentHidden() {}
    @Override public void componentActivated() {}
    @Override public void componentDeactivated() {}

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public CloseOperationState canCloseElement() {
        //TODO check saveable
        return CloseOperationState.STATE_OK;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changed();
    }
        
    @Override
    public void removeUpdate(DocumentEvent e) {
            changed();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    private void changed() {
        showError(null);
        if(nameChanged()) {
            //savable.setName();
        } else {
            //savable.setClearName();
        }
        changeDescription();
    }
       
    private boolean nameChanged() {
        String name = nameText.getText();
        if(!isNameSaveable(name))
            return false;
        element.setProperty(ProjectElement.NAME_PROPERTY, name);
        return true;
    }
        
    private boolean isNameSaveable(String name) {
        return !isNameEmpty(name) && isNewName(name);
    }
        
    private boolean isNameEmpty(String name) {
        if(!isEmpty(name))
            return false;
        showError("Name is empty!");
        return true;
    }
    
    private boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    private void showError(String msg) {
        msgLabel.setVisible(msg != null);
        msgLabel.setText(msg);
    }
        
    private boolean isNewName(String name) {
        String elementName = getStringProperty(ProjectElement.NAME_PROPERTY);
        if(elementName.equals(name))
            return !elementName.equalsIgnoreCase(name);
        return isNewNameInClaimType(name);
    }
        
    private String getStringProperty(String property) {
        return (String) element.getProperty(property);
    }
        
    private boolean isNewNameInClaimType(String name) {
        for(Project p : getProjects()) {
            if(p.getName().equalsIgnoreCase(name)) {
                showError("Name already used!");
                return false;
            }
        }
        return true;
    }
        
    private List<Project> getProjects() {
        Project project =  element.getValue();
        return project.getClaimType().getProjects();
    }
        
    private void changeDescription() {
        String desc = descriptionText.getText();
        if(newDescription(desc))
           element.setProperty(ProjectElement.DESCRIPTION_PROPERTY, desc); 
    }
    
    private boolean newDescription(String desc) {
        String pDesc = element.getValue().getDescription();
        if(desc == null || desc.trim().length()==0)
            return pDesc!=null && pDesc.length()>0;
        return !desc.equals(pDesc);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if(ProjectElement.NAME_PROPERTY.equals(property)) {
            setNameText(getStringProperty(property));
        } else if (ProjectElement.DESCRIPTION_PROPERTY.equals(property)) {
            setDescriptionText(getStringProperty(property));
        }
    }
    
    private void setNameText(String newText) {
        if(equals(newText, nameText.getText()))
            return;
        nameText.setText(newText);
    }
    
    private boolean equals(String s1, String s2) {
        if(isEmpty(s1))
            return isEmpty(s2);
        return s1.equalsIgnoreCase(s2);
    }
    
    private void setDescriptionText(String newText) {
        if(equals(newText, descriptionText.getText()))
            return;
        descriptionText.setText(newText);
    }
    
    private class DynamicSavable extends AbstractProjectElementSavable {
        
        private DynamicSavable() {
            super(ProjectEditorView.this.element);
        }
        
        @Override
        protected void handleSave() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
