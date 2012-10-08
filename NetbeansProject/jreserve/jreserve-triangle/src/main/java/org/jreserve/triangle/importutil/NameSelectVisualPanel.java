package org.jreserve.triangle.importutil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.NameSelectVisualPanel.PanelName=Input",
    "LBL.NameSelectVisualPanel.LoB=LoB:",
    "LBL.NameSelectVisualPanel.ClaimType=Claim type:",
    "LBL.NameSelectVisualPanel.Project=Project:",
    "LBL.NameSelectVisualPanel.DataType=Data type:",
    "LBL.NameSelectVisualPanel.Name=Name:"
})
public class NameSelectVisualPanel extends javax.swing.JPanel implements ActionListener, DocumentListener {
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean nameFilled = false;
    
    public NameSelectVisualPanel(boolean isTriangle) {
        initComponents();
        setName(Bundle.LBL_NameSelectVisualPanel_PanelName());
        if(isTriangle)
            dataTypeCombo.setShowVector(false);
        else
            dataTypeCombo.setShowTriangle(false);
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
        return (ProjectDataType) dataTypeCombo.getSelectedItem();
    }
    
    void setDataName(String name) {
        nameText.setText(name);
    }
    
    String getDataName() {
        return nameText.getText();
    }
    
    void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    void removeChangeListener(ChangeListener listenener) {
        listeners.remove(listenener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(dataTypeCombo == e.getSource())
            setDefaultName();
        fireChange();
    }

    private void setDefaultName() {
        if(nameFilled)
            return;
        ProjectDataType dt = dataTypeCombo.getDataType();
        nameText.setText(dt==null? null : dt.getName());
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        nameFilled = true;
        fireChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lobLabel = new javax.swing.JLabel();
        claimTypeLabel = new javax.swing.JLabel();
        projectLabel = new javax.swing.JLabel();
        dataTypeLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        lobCombo = new org.jreserve.project.system.visual.ProjectElementComboBox(RootElement.getDefault().getChildren(LoB.class));
        claimTypeCombo = new org.jreserve.project.system.visual.ProjectElementComboBox();
        projectCombo = new org.jreserve.project.system.visual.ProjectElementComboBox();
        dataTypeCombo = new org.jreserve.data.util.ProjectDataTypeComboBox();
        nameText = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        pBarPanel = new javax.swing.JPanel();
        pBar = new javax.swing.JProgressBar();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());

        lobLabel.setText(Bundle.LBL_NameSelectVisualPanel_LoB());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(lobLabel, gridBagConstraints);

        claimTypeLabel.setText(Bundle.LBL_NameSelectVisualPanel_ClaimType());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(claimTypeLabel, gridBagConstraints);

        projectLabel.setText(Bundle.LBL_NameSelectVisualPanel_Project());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(projectLabel, gridBagConstraints);

        dataTypeLabel.setText(Bundle.LBL_NameSelectVisualPanel_DataType());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(dataTypeLabel, gridBagConstraints);

        nameLabel.setText(Bundle.LBL_NameSelectVisualPanel_Name());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(nameLabel, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(projectCombo, gridBagConstraints);

        dataTypeCombo.addActionListener(this);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(nameText, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);

        pBarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pBar.setVisible(false);
        pBarPanel.add(pBar);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(pBarPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jreserve.project.system.visual.ProjectElementComboBox claimTypeCombo;
    private javax.swing.JLabel claimTypeLabel;
    private org.jreserve.data.util.ProjectDataTypeComboBox dataTypeCombo;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.Box.Filler filler1;
    private org.jreserve.project.system.visual.ProjectElementComboBox lobCombo;
    private javax.swing.JLabel lobLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JPanel pBarPanel;
    private org.jreserve.project.system.visual.ProjectElementComboBox projectCombo;
    private javax.swing.JLabel projectLabel;
    // End of variables declaration//GEN-END:variables

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
            List<ProjectDataType> dataTypes = element==null? Collections.EMPTY_LIST : element.getChildValues(ProjectDataType.class);
            boolean nf = nameFilled;
            String name = nameText.getText();
            dataTypeCombo.setProjectDataTypes(dataTypes);
            dataTypeCombo.setSelectedItem(null);
            nameText.setText(name);
            nameFilled = nf;
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
