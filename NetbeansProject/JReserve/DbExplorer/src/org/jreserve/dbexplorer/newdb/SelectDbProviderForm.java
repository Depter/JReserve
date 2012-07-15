/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.dbexplorer.newdb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jreserve.database.api.DatabaseProvider;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 *
 * @author Peti
 */
@Messages({
    "CTL_Title=Select provider",
    "CTL_Label=Provider:"
})
public class SelectDbProviderForm extends javax.swing.JPanel {
    
    public static DatabaseProvider getDatabaseProvider() {
        SelectDbProviderForm form = new SelectDbProviderForm();
        DialogDisplayer.getDefault().notify(form.dd);
        return form.getProvider();
    }
    
    private DialogDescriptor dd;
    
    private ActionListener comboListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isValid = providerCombo.getSelectedItem() != null;
            dd.setValid(isValid);
        }
    };
    
    /**
     * Creates new form SelectDbProviderForm
     */
    private SelectDbProviderForm() {
        initComponents();
        initDialogDescriptor();
        providerCombo.addActionListener(comboListener);
        setSelectedProvider();
    }
    
    private void initDialogDescriptor() {
        dd = new DialogDescriptor(this, Bundle.CTL_Title(), true, 
             DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, null);
        dd.setValid(false);
    }
    
    private void setSelectedProvider() {
        DatabaseProvider provider = Utilities.actionsGlobalContext().lookup(DatabaseProvider.class);
        providerCombo.setSelectedItem(provider);
    }
    
    private DatabaseProvider getProvider() {
        if(dd.getValue() == DialogDescriptor.OK_OPTION)
            return (DatabaseProvider) providerCombo.getSelectedItem();
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dbComboLabel = new javax.swing.JLabel();
        providerCombo = new javax.swing.JComboBox<DatabaseProvider>();

        dbComboLabel.setText(Bundle.CTL_Label());

        providerCombo.setModel(new DbProviderModel());
        providerCombo.setRenderer(new DbProviderRenderer());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dbComboLabel)
                .addGap(18, 18, 18)
                .addComponent(providerCombo, 0, 296, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dbComboLabel)
                    .addComponent(providerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dbComboLabel;
    private javax.swing.JComboBox<DatabaseProvider> providerCombo;
    // End of variables declaration//GEN-END:variables
}
