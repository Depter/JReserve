package org.jreserve.estimate.core.visual;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.RootElement;
import org.jreserve.project.system.visual.ProjectElementComboBox;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.NameSelectVisualPanel.PanelName=Input",
    "LBL.NameSelectVisualPanel.LoB=Line of business:",
    "LBL.NameSelectVisualPanel.ClaimType=Claim type:",
    "LBL.NameSelectVisualPanel.Project=Project:",
    "LBL.NameSelectVisualPanel.Name=Name:",
    "LBL.NameSelectVisualPanel.Description=Description:"
})
class NameSelectVisualPanel extends JPanel {
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private ProjectElementComboBox claimTypeCombo;
    private ProjectElementComboBox lobCombo;
    private ProjectElementComboBox projectCombo;
    private JTextField nameText;
    private JTextArea descriptionText;
    
    public NameSelectVisualPanel() {
        initComponents();
        setName(Bundle.LBL_NameSelectVisualPanel_PanelName());
    }

    void setLoB(LoB lob) {
        lobCombo.setSelectedItem(lob);
    }
    
    LoB getLoB() {
        return lobCombo.getSelectedItem(LoB.class);
    }
    
    void setClaimType(ClaimType claimType) {
        setLoB(claimType.getLoB());
        claimTypeCombo.setSelectedItem(claimType);
    }
    
    ClaimType getClaimType() {
        return claimTypeCombo.getSelectedItem(ClaimType.class);
    }
    
    void setProject(Project project) {
        setClaimType(project.getClaimType());
        projectCombo.setSelectedItem(project);
    }
    
    Project getProject() {
        return projectCombo.getSelectedItem(Project.class);
    }
    
    ProjectElement<Project> getProjectElement() {
        return projectCombo.getSelectedItem();
    }
    
    void setEstimateName(String name) {
        nameText.setText(name);
    }
    
    String getEstimateName() {
        return nameText.getText();
    }
    
    String getDescription() {
        String str = descriptionText.getText();
        if(str == null || str.trim().length() == 0)
            return null;
        return str;
    }
    
    void setDescription(String description) {
        descriptionText.setText(description);
    }
    
    void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    void removeChangeListener(ChangeListener listenener) {
        listeners.remove(listenener);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }

    private void initComponents() {

        lobCombo = new ProjectElementComboBox(RootElement.getDefault().getChildren(LoB.class));
        lobCombo.addLookupListener(LoB.class, new LoBListener());
        
        claimTypeCombo = new ProjectElementComboBox();
        claimTypeCombo.addLookupListener(ClaimType.class, new ClaimTypeListener());
        
        projectCombo = new ProjectElementComboBox();
        projectCombo.addLookupListener(Project.class, new ProjectListener());
        
        nameText = new JTextField();
        nameText.setText(null);
        NameListener nameListener = new NameListener();
        nameText.getDocument().addDocumentListener(nameListener);
        
        descriptionText = new javax.swing.JTextArea();
        descriptionText.setColumns(20);
        descriptionText.setRows(5);
        descriptionText.getDocument().addDocumentListener(nameListener);

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());
        GridBagConstraints gc;

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_LoB()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_ClaimType()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 2;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_Project()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 3;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 15, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_Name()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 5, 0);
        add(lobCombo, gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 1;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 5, 0);
        add(claimTypeCombo, gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 2;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 5, 0);
        add(projectCombo, gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 3;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gc.weightx = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 15, 0);
        add(nameText, gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 4;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gc.insets = new java.awt.Insets(0, 0, 0, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_Description()), gc);

        gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 5;
        gc.gridwidth = 2;
        gc.fill = java.awt.GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.insets = new java.awt.Insets(0, 0, 15, 0);
        add(new JScrollPane(descriptionText), gc);
    }

    private class NameListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
    
    private class LoBListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            ProjectElement element = lobCombo.getSelectedItem(ProjectElement.class);
            setClaimTypes(element);
            clearProjects();
            fireChange();
        }
    
        private void setClaimTypes(ProjectElement element) {
            if(element == null)
                claimTypeCombo.setElements(new ArrayList<ProjectElement>());
            else
                claimTypeCombo.setElements(element.getChildren(ClaimType.class));
        }
        
        private void clearProjects() {
            projectCombo.setElements(new ArrayList<ProjectElement>());
        }
    }
    
    private class ClaimTypeListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            ProjectElement element = claimTypeCombo.getSelectedItem(ProjectElement.class);
            setProjects(element);
            fireChange();
        }
    
        private void setProjects(ProjectElement element) {
            if(element == null)
                projectCombo.setElements(new ArrayList<ProjectElement>());
            else
                projectCombo.setElements(element.getChildren(Project.class));
        }
    }
    
    private class ProjectListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            ProjectElement element = projectCombo.getSelectedItem(ProjectElement.class);
            Object value = element==null? null : element.getValue();
            putClientProperty(NameSelectWizardPanel.PROP_PROJECT, value);
            fireChange();
        }
    }
}
