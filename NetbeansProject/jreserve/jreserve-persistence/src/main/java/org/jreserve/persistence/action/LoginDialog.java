package org.jreserve.persistence.action;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.*;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.List;
import javax.swing.SwingWorker;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.resources.images.ImageResources;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LoginDialog.nameLabel.text=Name:",
    "LoginDialog.passwordLabel.text=Password:",
    "LoginDialog.okButton.text=Ok",
    "LoginDialog.cancelButton.text=Cancel",
    "# {0} - database name",
    "loginDialog.title=Login - {0}",
    "CTL_msgConnecting=Connecting...",
    "CTL_msgConnectionFailed=Connection failed! Check user name and password."
})
class LoginDialog extends javax.swing.JDialog {
    
    private final static Logger logger = Logging.getLogger(LoginDialog.class.getName());
    
    private final static boolean MODAL = true;
    private static Frame getParentWindow() {
        return WindowManager.getDefault().getMainWindow();
    }
    
    private static String getTitle(PersistenceDatabase database) {
        return Bundle.loginDialog_title(database.getShortName());
    }
    
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    };
    
    private WindowListener closeListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            cancel();
        }
    };
    
    private ActionListener okListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ok();
        }
    };
    
    private final PersistenceDatabase database;
    private boolean cancelled = true;
    private String userName = null;
    private char[] password = null;
    private ConnectionChecker checker;
    
    LoginDialog(PersistenceDatabase database) {
        super(getParentWindow(), getTitle(database), MODAL);
        this.database = database;
        addWindowListener(closeListener);
        initComponents();
        centerDialog();
    }
    
    private void centerDialog() {
        Container parent = super.getParent();
        Point mainLocation = parent.getLocation();
        Dimension mainSize = parent.getSize();
        
        Dimension mySize  = getSize();
        int x = mainLocation.x + (mainSize.width - mySize.width) / 2 ;
        int y = mainLocation.y + (mainSize.height - mySize.height) / 2 ;
        setLocation(x, y);
    }
    
    boolean isCancelled() {
        return cancelled;
    }
    
    String getUserName() {
        return userName;
    }
    
    char[] getPassword() {
        return password;
    }
    
    private void cancel() {
        stopChecker();
        cancelState();
        dispose();
    }
    
    private void stopChecker() {
        if(checker == null)
            return;
        checker.cancel(true);
        checker = null;
    }
    
    private void cancelState() {
        cancelled = true;
        userName = null;
        password = null;
    }
    
    private void ok() {
        setCheckState(true);
        checker = new ConnectionChecker();
        checker.execute();
    }
    
    private void setCheckState(boolean isChecking) {
        nameText.setEnabled(!isChecking);
        passwordText.setEnabled(!isChecking);
        okButton.setEnabled(!isChecking);
        okButton.setEnabled(!isChecking);
        msgLabel.setText(Bundle.CTL_msgConnecting());
        msgLabel.setVisible(isChecking);
        pBar.setIndeterminate(isChecking);
        pBar.setVisible(isChecking);
    }
    
    private void checkerFinnished() {
        setCheckState(false);
        try {
            checker.get();
            okState();
            dispose();
        } catch (Exception ex) {
            showException(ex);
        } finally {
            checker = null;
        }
    }
    
    private void okState() {
        cancelled = false;
        password = passwordText.getPassword();
        userName = escapeUserName();
    }
    
    private String escapeUserName() {
        String name = nameText.getText();
        if(name != null && name.length()==0)
            return null;
        return name;
    }
    
    private void showException(Exception ex) {
        logger.error(ex, "Unable to connect to database '%s'!", database.getShortName());
        msgLabel.setIcon(ImageResources.error());
        msgLabel.setText(Bundle.CTL_msgConnectionFailed());
        msgLabel.setVisible(true);
        pack();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordText = new javax.swing.JPasswordField();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        pBar = new javax.swing.JProgressBar();
        msgLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        nameLabel.setText(Bundle.LoginDialog_nameLabel_text());

        nameText.setText(database.getUserName());
        nameText.setMinimumSize(new java.awt.Dimension(200, 20));
        nameText.addActionListener(okListener);

        passwordLabel.setText(Bundle.LoginDialog_passwordLabel_text());

        passwordText.addActionListener(okListener);
        passwordText.setMinimumSize(new java.awt.Dimension(200, 20));

        cancelButton.setText(Bundle.LoginDialog_cancelButton_text());
        cancelButton.addActionListener(cancelListener);

        okButton.setText(Bundle.LoginDialog_okButton_text());
        okButton.addActionListener(okListener);

        pBar.setVisible(false);

        msgLabel.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(msgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton)
                        .addComponent(okButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(msgLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordText;
    // End of variables declaration//GEN-END:variables

    
    private class ConnectionChecker extends SwingWorker<Void, Void> {
        
        private final String driver;
        private final String url;
        private final String user = escapeUserName();
        private final char[] password = passwordText.getPassword();
        private final ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
        
        private ConnectionChecker() {
            this.driver = database.getDriverClass();
            this.url = database.getConnectionUrl();
        }
        
        @Override
        protected void done() {
            checkerFinnished();
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            registerDriver();
            printDrivers();
            DriverManager.getConnection(url, user, getPassAsString()).close();
            return null;
        }
        
        private void registerDriver() throws Exception {
            for(Driver driverInstance : Lookup.getDefault().lookupAll(Driver.class))
                DriverManager.registerDriver(driverInstance);
            //Class<?> driverClass = classLoader.loadClass(driver);
            //Driver driverInstance = (Driver) driverClass.newInstance();
            //DriverManager.registerDriver(driverInstance);
        }
        
        private void printDrivers() {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while(drivers.hasMoreElements())
                logger.debug("Driver: %s", drivers.nextElement().getClass());
        }
        
        private String getPassAsString() {
            if(password == null)
                return null;
            return new String(password);
        }
    }
}
