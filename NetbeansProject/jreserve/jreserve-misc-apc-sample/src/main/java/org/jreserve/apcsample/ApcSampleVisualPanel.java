package org.jreserve.apcsample;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ApcSampleVisualPanel.PanelName=Set name",
    "LBL.ApcSampleVisualPanel.LoB.Name=Motor"
})
public class ApcSampleVisualPanel extends javax.swing.JPanel implements DocumentListener {

    private final static String NAME_PROPERTY = "APC.SAMPLE.NAME";
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public ApcSampleVisualPanel() {
        setName(Bundle.LBL_ApcSampleVisualPanel_PanelName());
        initComponents();
        progressPanel.setVisible(false);
    }

    void readSettings(WizardDescriptor wizard) {
        String name = (String) wizard.getProperty(NAME_PROPERTY);
        if(name != null)
            nameText.setText(name);
    }
    
    void storeSettings(WizardDescriptor wizard) {
        wizard.putProperty(NAME_PROPERTY, getSampleName());
    }
    
    String getSampleName() {
        String name = nameText.getText();
        if(name==null || name.trim().length()==0)
            return null;
        return name;
    }
    
    void startWorking() {
        pBar.setIndeterminate(true);
        progressPanel.setVisible(true);
    }
    
    void stopWorking() {
        progressPanel.setVisible(false);
        pBar.setIndeterminate(false);
    }
    
    void setProgressMsg(String msg) {
        if(msg == null)
            msg = NbBundle.getMessage(ApcSampleVisualPanel.class, "LBL.ApcSampleVisualPanel.Progress");
        progressLabel.setText(msg);
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

        nameLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        progressPanel = new javax.swing.JPanel();
        progressLabel = new javax.swing.JLabel();
        pBar = new javax.swing.JProgressBar();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.GridBagLayout());

        nameLabel.setText(org.openide.util.NbBundle.getMessage(ApcSampleVisualPanel.class, "LBL.ApcSampleVisualPanel.Name")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(nameLabel, gridBagConstraints);

        nameText.setText(Bundle.LBL_ApcSampleVisualPanel_LoB_Name());
        nameText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(nameText, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);

        progressPanel.setLayout(new java.awt.BorderLayout(0, 5));

        progressLabel.setText(org.openide.util.NbBundle.getMessage(ApcSampleVisualPanel.class, "LBL.ApcSampleVisualPanel.Progress")); // NOI18N
        progressPanel.add(progressLabel, java.awt.BorderLayout.NORTH);
        progressPanel.add(pBar, java.awt.BorderLayout.LINE_START);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(progressPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JPanel progressPanel;
    // End of variables declaration//GEN-END:variables

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

    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
}
