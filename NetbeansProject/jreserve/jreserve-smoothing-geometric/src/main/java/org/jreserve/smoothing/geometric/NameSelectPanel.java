package org.jreserve.smoothing.geometric;

import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.SwingLoader;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.NameSelectPanel.Title=Select name",
    "MSG.NameSelectPanel.Name.Empty=Field 'Name' is empty!",
    "# {0} - name",
    "MSG.NameSelectPanel.Name.Exists=Name \"{0}\" already exists!",
    "# {0} - count",
    "LBL.NameSelectPanel.DefaultName=Smooth {0}"
})
public class NameSelectPanel extends javax.swing.JPanel implements DocumentListener, ActionListener, WindowListener {
    
    public static String getName(PersistentObject owner) {
        NameSelectPanel content = new NameSelectPanel(owner);
        createDialog(content);
        content.dialog.setVisible(true);
        return content.name;
    }
    
    private static void createDialog(NameSelectPanel panel) {
        DialogDescriptor dd = new DialogDescriptor(panel, Bundle.LBL_NameSelectPanel_Title());
        dd.setOptions(new Object[0]);
        panel.setDialog(DialogDisplayer.getDefault().createDialog(dd));
        panel.dialog.pack();
    }
    
    
    private final static Logger logger = Logger.getLogger(NameSelectPanel.class.getName());
    
    private final static String ERR_IMG = "org/netbeans/modules/dialogs/error.gif";
    private final static String CARD_MSG = "MSG_LABEL";
    private final static String CARD_PBAR = "PBAR_LABEL";
    
    private Dialog dialog;
    private String name;
    private CardLayout msgLayout;
    private List<Smoothing> smoothings;
    
    private volatile boolean cancelled = false;
    private Loader loader;
    
    
    public NameSelectPanel(PersistentObject owner) {
        initComponents();
        msgLayout = (CardLayout) msgPanel.getLayout();
        checkInput();
        startLoading(owner);
    }
    
    private void setDialog(Dialog dialog) {
        this.dialog = dialog;
        this.dialog.addWindowListener(this);
    }
    
    private void startLoading(PersistentObject owner) {
        nameText.setEnabled(false);
        okButton.setEnabled(false);
        msgLayout.show(msgPanel, CARD_PBAR);
        pBar.setIndeterminate(true);
        loader = new Loader(owner);
        loader.execute();
    }
        
    private void stopLoading() {
        msgLayout.show(msgPanel, CARD_MSG);
        setDefaultName();
        nameText.setEnabled(true);
        pBar.setIndeterminate(false);
        loader = null;
    }
    
    private void setDefaultName() {
        if(smoothings == null)
            return;
        int count = smoothings.size()+1;
        nameText.setText(Bundle.LBL_NameSelectPanel_DefaultName(count));
    }
    
    private void showError(String msg) {
        msgLabel.setVisible(msg != null);
        msgLabel.setText(msg);
        msgLayout.show(msgPanel, CARD_MSG);
    }
    
    private void checkInput() {
        String smoothingName = nameText.getText();
        boolean valid = checkNotNull(smoothingName) && checkNotExists(smoothingName);
        okButton.setEnabled(valid);
        if(valid)
            showError(null);
    }

    private boolean checkNotNull(String name) {
        if(name == null || name.trim().length() == 0) {
            showError(Bundle.MSG_NameSelectPanel_Name_Empty());
            return false;
        }
        return true;
    }
    
    private boolean checkNotExists(String name) {
        String trimmed = name.trim();
        for(Smoothing s : smoothings)
            if(s.getName().trim().equalsIgnoreCase(trimmed)) {
                showError(Bundle.MSG_NameSelectPanel_Name_Exists(name));
                return false;
            }
        return true;
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        checkInput();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkInput();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(okButton == source) {
            name = nameText.getText();
        } else {
            cancel();
        }
        dialog.dispose();
    }
    
    private void cancel() {
        cancelled = true;
        if(loader != null)
            loader.cancel(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        cancel();
    }
    
    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        namePanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        soutPanel = new javax.swing.JPanel();
        msgPanel = new javax.swing.JPanel();
        pBarPanel = new javax.swing.JPanel();
        pBarLabel = new javax.swing.JLabel();
        pBar = new javax.swing.JProgressBar();
        msgLabelPanel = new javax.swing.JPanel();
        msgLabel = new javax.swing.JLabel(ImageUtilities.loadImageIcon(ERR_IMG, false));
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        cancelButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.BorderLayout(15, 15));

        namePanel.setLayout(new java.awt.BorderLayout(5, 0));

        nameLabel.setText(org.openide.util.NbBundle.getMessage(NameSelectPanel.class, "LBL.NameSelectPanel.Name")); // NOI18N
        namePanel.add(nameLabel, java.awt.BorderLayout.LINE_START);

        nameText.setText(null);
        nameText.getDocument().addDocumentListener(this);
        namePanel.add(nameText, java.awt.BorderLayout.CENTER);

        add(namePanel, java.awt.BorderLayout.NORTH);
        add(filler1, java.awt.BorderLayout.CENTER);

        soutPanel.setLayout(new java.awt.BorderLayout(0, 5));

        msgPanel.setLayout(new java.awt.CardLayout());

        pBarPanel.setLayout(new java.awt.BorderLayout());

        pBarLabel.setText(org.openide.util.NbBundle.getMessage(NameSelectPanel.class, "LBL.NameSelectPanel.PBar")); // NOI18N
        pBarPanel.add(pBarLabel, java.awt.BorderLayout.NORTH);
        pBarPanel.add(pBar, java.awt.BorderLayout.WEST);

        msgPanel.add(pBarPanel, "PBAR_LABEL");

        msgLabelPanel.setLayout(new java.awt.BorderLayout());

        msgLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        msgLabel.setText(null);
        msgLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        msgLabelPanel.add(msgLabel, java.awt.BorderLayout.CENTER);

        msgPanel.add(msgLabelPanel, "MSG_LABEL");

        soutPanel.add(msgPanel, java.awt.BorderLayout.NORTH);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        okButton.setText(org.openide.util.NbBundle.getMessage(NameSelectPanel.class, "CTL.NameSelectPanel.Ok")); // NOI18N
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(filler2);

        cancelButton.setText(org.openide.util.NbBundle.getMessage(NameSelectPanel.class, "CTL.NameSelectPanel.Cancel")); // NOI18N
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        soutPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        add(soutPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JPanel msgLabelPanel;
    private javax.swing.JPanel msgPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel namePanel;
    private javax.swing.JTextField nameText;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JLabel pBarLabel;
    private javax.swing.JPanel pBarPanel;
    private javax.swing.JPanel soutPanel;
    // End of variables declaration//GEN-END:variables

    private class Loader extends SwingLoader {
        
        private Loader(PersistentObject owner) {
            super(owner);
        }

        @Override
        protected void done() {
            try {
                if(cancelled)
                    return;
                smoothings = get();
                checkInput();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unable to load smoothings!", ex);
                showError(ex.getLocalizedMessage());
            } finally {
                stopLoading();
            }
        }
    }
}
