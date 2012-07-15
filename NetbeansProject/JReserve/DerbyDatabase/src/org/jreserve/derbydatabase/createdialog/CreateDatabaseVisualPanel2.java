package org.jreserve.derbydatabase.createdialog;

import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.util.NbBundle;

/**
 * 
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "CTL_stepTwo=Create database",
    "CTL_userName=User name",
    "CTL_password=Password:",
    "CTL_helpMsg=Fill in these fields to protect the database,\n or leave them empty to create an unprotected database.",
    "CTL_createDbLabel=Creating database...",
})
final class CreateDatabaseVisualPanel2 extends JPanel {

    public CreateDatabaseVisualPanel2() {
        initComponents();
    }

    @Override
    public String getName() {
        return Bundle.CTL_stepTwo();
    }

    String getUserName() {
        return escapeTextField(userNameText);
    }
    
    private String escapeTextField(JTextField textField) {
        String str = textField.getText();
        if(str == null || str.trim().length()==0)
            return null;
        return str;
    }
    
    void setUserName(String userName) {
        userNameText.setText(userName);
    }
    
    String getPassword() {
        char[] c = passText.getPassword();
        String pass = (c==null || c.length==0)? null : new String(c);
        if(pass != null && pass.trim().length()==0)
            return null;
        return pass;
    }
    
    void startCreatingDb() {
        pBar.setIndeterminate(true);
        pBar.setVisible(true);
        createDbLabel.setVisible(true);
    }
    
    void finishCreatingDb() {
        pBar.setIndeterminate(false);
        pBar.setVisible(false);
        createDbLabel.setVisible(false);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        createDbLabel = new javax.swing.JLabel();
        pBar = new javax.swing.JProgressBar();
        msgLabel = new javax.swing.JLabel();
        passText = new javax.swing.JPasswordField();
        passLabel = new javax.swing.JLabel();
        userNameText = new javax.swing.JTextField();
        userNameLabel = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(createDbLabel, Bundle.CTL_createDbLabel());
        createDbLabel.setVisible(false);

        pBar.setVisible(false);

        org.openide.awt.Mnemonics.setLocalizedText(msgLabel, Bundle.CTL_helpMsg());
        msgLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        org.openide.awt.Mnemonics.setLocalizedText(passLabel, Bundle.CTL_password());

        org.openide.awt.Mnemonics.setLocalizedText(userNameLabel, Bundle.CTL_userName());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(userNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(passLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userNameText)
                            .addComponent(passText)))
                    .addComponent(msgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(createDbLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 238, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userNameLabel)
                    .addComponent(userNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passLabel)
                    .addComponent(passText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(createDbLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel createDbLabel;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JLabel passLabel;
    private javax.swing.JPasswordField passText;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JTextField userNameText;
    // End of variables declaration//GEN-END:variables
}
