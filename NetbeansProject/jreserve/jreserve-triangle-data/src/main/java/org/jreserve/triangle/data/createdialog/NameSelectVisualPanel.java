package org.jreserve.triangle.data.createdialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.data.ProjectDataType;
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
    "LBL.NameSelectVisualPanel.LoB=LoB:",
    "LBL.NameSelectVisualPanel.ClaimType=Claim type:",
    "LBL.NameSelectVisualPanel.DataType=Data type:",
    "LBL.NameSelectVisualPanel.Project=Project:",
    "LBL.NameSelectVisualPanel.Name=Name:",
    "LBL.NameSelectVisualPanel.Description=Description:"
})
class NameSelectVisualPanel extends JPanel implements DocumentListener {
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean isTriangle;
    private boolean nameFilled = false;
    
    private ProjectElementComboBox claimTypeCombo;
    private ProjectElementComboBox dataTypeCombo;
    private ProjectElementComboBox lobCombo;
    private ProjectElementComboBox projectCombo;
    private JTextArea descriptionText;
    private JTextField nameText;
    private JProgressBar pBar;
    private JPanel pBarPanel;
    
    
    public NameSelectVisualPanel(boolean isTriangle) {
        initComponents();
        setName(Bundle.LBL_NameSelectVisualPanel_PanelName());
        this.isTriangle = isTriangle;
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
    
    void setDataType(ProjectDataType dataType) {
        dataTypeCombo.setSelectedItem(dataType);
    }
    
    ProjectDataType getDataType() {
        return dataTypeCombo.getSelectedItem(ProjectDataType.class);
    }
    
    void setDataName(String name) {
        nameText.setText(name);
    }
    
    String getDataName() {
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
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        if(nameText.getDocument() == e.getDocument())
            nameFilled = true;
        fireChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if(nameText.getDocument() == e.getDocument())
            nameFilled = true;
        fireChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    void showProgressBar() {
        setWorkState(true);
    }
    
    private void setWorkState(boolean working) {
        lobCombo.setEnabled(!working);
        claimTypeCombo.setEnabled(!working);
        projectCombo.setEnabled(!working);
        dataTypeCombo.setEnabled(!working);
        nameText.setEnabled(!working);
        pBar.setIndeterminate(working);
        pBar.setVisible(working);
    }
    
    void hideProgressBar() {
        setWorkState(false);
    }
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lobCombo = new org.jreserve.project.system.visual.ProjectElementComboBox(RootElement.getDefault().getChildren(LoB.class));
        claimTypeCombo = new org.jreserve.project.system.visual.ProjectElementComboBox();
        projectCombo = new org.jreserve.project.system.visual.ProjectElementComboBox();
        dataTypeCombo = new org.jreserve.project.system.visual.ProjectElementComboBox();
        nameText = new javax.swing.JTextField();
        pBarPanel = new javax.swing.JPanel();
        pBar = new javax.swing.JProgressBar();
        descriptionText = new javax.swing.JTextArea();
        
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_LoB()), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_ClaimType()), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_Project()), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_DataType()), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_Name()), gridBagConstraints);

        lobCombo.addLookupListener(LoB.class, new LoBListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(lobCombo, gridBagConstraints);

        claimTypeCombo.addLookupListener(ClaimType.class, new ClaimTypeListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(claimTypeCombo, gridBagConstraints);

        projectCombo.addLookupListener(Project.class, new ProjectListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(projectCombo, gridBagConstraints);

        dataTypeCombo.addLookupListener(ProjectDataType.class, new DataTypeListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(dataTypeCombo, gridBagConstraints);

        nameText.setText(null);
        nameText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(nameText, gridBagConstraints);

        pBarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pBar.setVisible(false);
        pBarPanel.add(pBar);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(pBarPanel, gridBagConstraints);

        descriptionText.setColumns(20);
        descriptionText.setRows(5);
        descriptionText.getDocument().addDocumentListener(this);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(new JScrollPane(descriptionText), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(new JLabel(Bundle.LBL_NameSelectVisualPanel_Description()), gridBagConstraints);
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
    
    private class ClaimTypeListener implements LookupListener, Comparator<ProjectElement> {

        @Override
        public void resultChanged(LookupEvent le) {
            ProjectElement element = claimTypeCombo.getSelectedItem(ProjectElement.class);
            setProjects(element);
            setDataTypes(element);
            fireChange();
        }
    
        private void setProjects(ProjectElement element) {
            if(element == null)
                projectCombo.setElements(new ArrayList<ProjectElement>());
            else
                projectCombo.setElements(element.getChildren(Project.class));
        }
        
        private void setDataTypes(ProjectElement element) {
            if(element == null)
                setDataTypes(Collections.EMPTY_LIST);
            else
                setDataTypes(element.getChildren(ProjectDataType.class));
        }
        
        private void setDataTypes(List<ProjectElement<ProjectDataType>> elements) {
            boolean nf = nameFilled;
            String name = nameText.getText();
            dataTypeCombo.setElements(filteredElements(elements));
            dataTypeCombo.setSelectedItem(null);
            nameText.setText(name);
            nameFilled = nf;
        }
        
        private List<ProjectElement> filteredElements(List<ProjectElement<ProjectDataType>> elements) { 
            List<ProjectElement> result = new ArrayList<ProjectElement>();
            for(ProjectElement<ProjectDataType> element : elements)
                if(shouldAddelement(element))
                    result.add(element);
            Collections.sort(result, this);
            return result;
        }
        
        private boolean shouldAddelement(ProjectElement<ProjectDataType> element) {
            ProjectDataType dt = element.getValue();
            if(isTriangle)
                return dt.isTriangle();
            return !dt.isTriangle();
        }

        @Override
        public int compare(ProjectElement o1, ProjectElement o2) {
            ProjectDataType d1 = (ProjectDataType) o1.getValue();
            ProjectDataType d2 = (ProjectDataType) o2.getValue();
            return d1.compareTo(d2);
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
    
    private class DataTypeListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent le) {
            ProjectDataType dt = dataTypeCombo.getSelectedItem(ProjectDataType.class);
            setDefaultName(dt);
            fireChange();
        }

        private void setDefaultName(ProjectDataType dt) {
            if(nameFilled)
                return;
            nameText.setText(dt==null? null : dt.getName());
        }
    }
}
