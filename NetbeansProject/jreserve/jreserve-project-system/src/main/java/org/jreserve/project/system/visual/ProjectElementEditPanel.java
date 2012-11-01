package org.jreserve.project.system.visual;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.jreserve.project.system.ProjectElement;
import org.openide.awt.UndoRedo;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectElementEditPanel extends JPanel implements Lookup.Provider, UndoRedo.Provider {
    
    private final static String ERR_IMG = "org/netbeans/modules/dialogs/error.gif";

    protected ProjectElement element;
    
    private JLabel pathText;
    private JTextField nameText;
    
    private JLabel descriptionLabel;
    private JTextArea descriptionText;
    private JScrollPane descriptionScroll;
    
    private JLabel msgLabel;
    private boolean userEdit = true;
    private boolean panelEdit = false;
    
    private Validator validator;
    private PropertyChangeListener elementListener;
    
    public ProjectElementEditPanel(ProjectElement element, Validator validator, boolean hasDescription) {
        this.element = element;
        this.validator = validator;
        this.validator.setPanel(this);
        initComponents();
        setDescriptionVisible(hasDescription);
        addListeners();
        validatePanel();
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel pathLabel = new JLabel(NbBundle.getMessage(ProjectElementEditPanel.class, "LBL.ProjectElementEditPanel.Path"));
        GridBagConstraints gc = new java.awt.GridBagConstraints();
        gc.gridx = 0; gc.gridy=0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 5, 5);
        add(pathLabel, gc);
        
        JLabel nameLabel = new JLabel(NbBundle.getMessage(ProjectElementEditPanel.class, "LBL.ProjectElementEditPanel.Name"));
        gc.gridy=1;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 15, 5);
        add(nameLabel, gc);
        
        descriptionLabel = new JLabel(NbBundle.getMessage(ProjectElementEditPanel.class, "LBL.ProjectElementEditPanel.Description"));
        gc.gridy=2;
        gc.insets = new java.awt.Insets(0, 0, 0, 5);
        add(descriptionLabel, gc);

        pathText = new JLabel(element.getNamePath());
        gc.gridx=1; gc.gridy=0;
        gc.insets = new java.awt.Insets(0, 0, 5, 0);
        add(pathText, gc);

        nameText = new JTextField((String) element.getProperty(ProjectElement.NAME_PROPERTY));
        nameText.setPreferredSize(new java.awt.Dimension(200, 20));
        gc.gridy=1;
        gc.insets = new java.awt.Insets(0, 0, 15, 0);
        add(nameText, gc);

        descriptionText = new JTextArea((String) element.getProperty(ProjectElement.DESCRIPTION_PROPERTY));
        descriptionText.setColumns(20);
        descriptionText.setRows(5);
        gc.gridx=0; gc.gridy = 3;
        gc.gridwidth=3;
        gc.fill=GridBagConstraints.BOTH;
        gc.weightx = 1.0; gc.weighty = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 15, 0);
        descriptionScroll = new JScrollPane(descriptionText);
        add(descriptionScroll, gc);
        
        msgLabel = new JLabel();
        msgLabel.setText(null);
        Image img = ImageUtilities.loadImage(ERR_IMG, false);
        msgLabel.setIcon(ImageUtilities.image2Icon(img));
        msgLabel.setVisible(false);
        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 3;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.BASELINE_LEADING;
        gc.weightx=1d; gc.weighty=0d;
        add(msgLabel, gc);
    }
    
    private void setDescriptionVisible(boolean visible) {
        descriptionLabel.setVisible(visible);
        descriptionScroll.setVisible(visible);
        descriptionText.setVisible(visible);
    }
    
    private void addListeners() {
        EditListener editListener = new EditListener();
        nameText.getDocument().addDocumentListener(editListener);
        descriptionText.getDocument().addDocumentListener(editListener);
        
        elementListener = WeakListeners.propertyChange(new ElementListener(), element);
        element.addPropertyChangeListener(elementListener);
    }
    
    public void close() {
        element.removePropertyChangeListener(elementListener);
    }
    
    public String getElementName() {
        return getText(nameText);
    }
    
    private String getText(JTextComponent text) {
        String str = text.getText();
        if(str == null || str.trim().length()==0)
            return null;
        return str;
    }
    
    public String getElementDescription() {
        if(descriptionText.isVisible())
            return getText(descriptionText);
        return null;
    }
    
    public void setElementName(String name) {
        setText(nameText, name);
    }
    
    private void setText(JTextComponent text, String str) {
        userEdit = false;
        text.setText(str);
        userEdit = true;
    }
    
    public void setElementDescription(String description) {
        if(!descriptionText.isVisible())
            return;
        setText(descriptionText, description);
    }
    
    public void showError(String msg) {
        msgLabel.setText(msg);
        msgLabel.setVisible(msg != null);
    }
        
    private void validatePanel() {
        if(validator.isNameValid(getElementName()));
            validator.isDescriptionValid(getElementDescription());
    }

    @Override
    public Lookup getLookup() {
        return element.getLookup();
    }

    @Override
    public UndoRedo getUndoRedo() {
        UndoRedo ur = element.getLookup().lookup(UndoRedo.class);
        return ur!=null? ur : UndoRedo.NONE;
    }
    
    public ProjectElement getElement() {
        return element;
    }
    
    private class EditListener implements DocumentListener {

        @Override public void changedUpdate(DocumentEvent e) {}

        @Override
        public void insertUpdate(DocumentEvent e) {
            change(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            change(e);
        }
        
        private void change(DocumentEvent e) {
            validatePanel();
            if(userEdit) {
                panelEdit = true;
                if(isNameChange(e)) {
                    nameChanged();
                } else {
                    descriptionChanged();
                }
                panelEdit = false;
            }
        }
        
        private boolean isNameChange(DocumentEvent e) {
            return nameText.getDocument() == e.getDocument();
        }
        
        private void nameChanged() {
            String name = getText(nameText);
            if(validator.isNameValid(name))
                element.setProperty(ProjectElement.NAME_PROPERTY, name);
        }
        
        private void descriptionChanged() {
            String description = getText(descriptionText);
            if(validator.isDescriptionValid(description))
                element.setProperty(ProjectElement.DESCRIPTION_PROPERTY, description);
        }
    }
    
    private class ElementListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(panelEdit)
                return;
            String property = evt.getPropertyName();
            if(ProjectElement.NAME_PROPERTY.equals(property)) {
                setElementName((String) evt.getNewValue());
            } else if(ProjectElement.DESCRIPTION_PROPERTY.equals(property)) {
                setElementDescription((String) evt.getNewValue());
            }
        }
    
    
    }
    
    public static interface Validator {
        
        public void setPanel(ProjectElementEditPanel panel);
        
        public boolean isNameValid(String name);
        
        public boolean isDescriptionValid(String description);
    }
}
