package org.jreserve.smoothing.exponential;

import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.localesettings.util.LocaleSettings;
import org.jreserve.resources.textfieldfilters.DoubleFilter;
import org.jreserve.smoothing.core.Smoothable;
import org.jreserve.smoothing.core.Smoothing;
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
    "LBL.CreatorPanel.Title=Exponential smoothing",
    "LBL.CreatorPanel.Error.Name.Empty=Field 'Name' is empty!",
    "LBL.CreatorPanel.Error.Name.Exists=Name already exists!",
    "LBL.CreatorPanel.Error.Alpha.Empty=Field 'Alpha' is empty!",
    "LBL.CreatorPanel.Error.Alpha.Invalid=Alpha must be between 0 and 1!",
    "# {0} - count",
    "LBL.CreatorPanel.DefaultName=Smooth {0}"
})
public class CreatorPanel extends javax.swing.JPanel implements ActionListener, DocumentListener, WindowListener {
    
    public static CreatorPanel create(Smoothable smoothable, double[] input, int visibleDigits) {
        CreatorPanel content = new CreatorPanel(smoothable, input, visibleDigits);
        createDialog(content);
        content.dialog.setVisible(true);
        return content;
    }
    
    private static void createDialog(CreatorPanel panel) {
        DialogDescriptor dd = new DialogDescriptor(panel, Bundle.LBL_CreatorPanel_Title());
        dd.setOptions(new Object[0]);
        panel.setDialog(DialogDisplayer.getDefault().createDialog(dd));
        panel.dialog.pack();
    }
    
    private final static String ERR_IMG = "org/netbeans/modules/dialogs/error.gif";
    private final static String CARD_MSG = "MSG_LABEL";
    private final static double DEFAULT_ALPHA = 0d;
    
    private Dialog dialog;
    
    private DoubleRenderer renderer = new DoubleRenderer();
    private ExponentialSmoothing smoothing = new ExponentialSmoothing(DEFAULT_ALPHA);
    
    private Smoothable smoothable;
    
    private CardLayout msgLayout;
    
    private String name;
    private double[] input;
    private double alpha;
    
    public CreatorPanel(Smoothable smoothable, double[] input, int visibleDigits) {
        this.input = input;
        smoothing.setAlpha(0d);
        this.smoothable = smoothable;
        
        initComponents();
        smoothingTable.setVisibleDigits(visibleDigits);
        smoothingTable.setInput(input);
        smoothingTable.setSmoothed(smoothing.smooth(input));
        
        msgLayout = (CardLayout) msgPanel.getLayout();
        checkInput();
    }
    
    private void setDialog(Dialog dialog) {
        this.dialog = dialog;
        this.dialog.addWindowListener(this);
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
    
    private void checkInput() {
        boolean valid = checkNameValid() & checkAlphaValid();
        okButton.setEnabled(valid);
        if(valid)
            showError(null);
    }
    
    private boolean checkNameValid() {
        String smoothingName = nameText.getText();
        return checkNameNotNull(smoothingName) && checkNotExists(smoothingName);
    }
    
    private boolean checkNameNotNull(String name) {
        if(name == null || name.trim().length() == 0) {
            showError(Bundle.LBL_CreatorPanel_Error_Name_Empty());
            return false;
        }
        return true;
    }
    
    private boolean checkNotExists(String name) {
        String trimmed = name.trim();
        for(Smoothing s : smoothable.getSmoothings())
            if(s.getName().trim().equalsIgnoreCase(trimmed)) {
                showError(Bundle.LBL_CreatorPanel_Error_Name_Exists());
                return false;
            }
        return true;
    }
    
    private boolean checkAlphaValid() {
        String strAlpha = alphaText.getText();
        if(checkAlphaNotNull(strAlpha) && checkAlphaValid(strAlpha)) {
            smoothing.setAlpha(renderer.parse(alphaText.getText()));
            smoothingTable.setSmoothed(smoothing.smooth(input));
            return true;
        } else {
            smoothingTable.setSmoothed(null);
            return false;
        }
    }
    
    private boolean checkAlphaNotNull(String alpha) {
        if(alpha == null || alpha.trim().length() == 0) {
            showError(Bundle.LBL_CreatorPanel_Error_Alpha_Empty());
            return false;
        }
        return true;
    }
    
    private boolean checkAlphaValid(String strAlpha) {
        double a = renderer.parse(strAlpha);
        if(a < 0d || 1d < a) {
            showError(Bundle.LBL_CreatorPanel_Error_Alpha_Invalid());
            return false;
        }
        return true;
    }
    
    private void cancel() {
        smoothing = null;
    }
    
    private String getDefaultName() {
        int count = smoothable.getMaxSmoothingOrder() + 1;
        return Bundle.LBL_CreatorPanel_DefaultName(count);
    }
    
    private void showError(String msg) {
        msgLabel.setVisible(msg != null);
        msgLabel.setText(msg);
        msgLayout.show(msgPanel, CARD_MSG);
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(okButton == source) {
            name = nameText.getText();
            alpha = renderer.parse(alphaText.getText());
        } else {
            cancel();
        }
        dialog.dispose();
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
    
    boolean isCancelled() {
        return name == null;
    }
    
    String getSmoothingName() {
        return name;
    }
    
    double getAlpha() {
        return alpha;
    }
    
    boolean[] getApplied() {
        return smoothingTable.getApplied();
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

        northPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        alphaLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        alphaText = new javax.swing.JTextField();
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
        centerPanel = new javax.swing.JPanel();
        smoothingTable = new org.jreserve.smoothing.visual.SmoothingTablePanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setLayout(new java.awt.BorderLayout(15, 15));

        northPanel.setLayout(new java.awt.GridBagLayout());

        nameLabel.setText(org.openide.util.NbBundle.getMessage(CreatorPanel.class, "LBL.CreatorPanel.Name")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        northPanel.add(nameLabel, gridBagConstraints);

        alphaLabel.setText(org.openide.util.NbBundle.getMessage(CreatorPanel.class, "LBL.CreatorPanel.Alpha")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        northPanel.add(alphaLabel, gridBagConstraints);

        nameText.setText(getDefaultName());
        nameText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        northPanel.add(nameText, gridBagConstraints);

        alphaText.setText(null);
        alphaText.setDocument(new DoubleFilter(LocaleSettings.getDecimalSeparator()));
        alphaText.setText("0");
        alphaText.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        northPanel.add(alphaText, gridBagConstraints);

        add(northPanel, java.awt.BorderLayout.NORTH);

        soutPanel.setLayout(new java.awt.BorderLayout(0, 5));

        msgPanel.setLayout(new java.awt.CardLayout());

        pBarPanel.setLayout(new java.awt.BorderLayout());

        pBarLabel.setText(org.openide.util.NbBundle.getMessage(CreatorPanel.class, "LBL.CreatorPanel.Loading")); // NOI18N
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

        okButton.setText(org.openide.util.NbBundle.getMessage(CreatorPanel.class, "CTL.CreatorPanel.Ok")); // NOI18N
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(filler2);

        cancelButton.setText(org.openide.util.NbBundle.getMessage(CreatorPanel.class, "CTL.CreatorPanel.Cancel")); // NOI18N
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        soutPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        add(soutPanel, java.awt.BorderLayout.SOUTH);

        centerPanel.setLayout(new java.awt.BorderLayout());
        centerPanel.add(smoothingTable, java.awt.BorderLayout.CENTER);

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alphaLabel;
    private javax.swing.JTextField alphaText;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel centerPanel;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JPanel msgLabelPanel;
    private javax.swing.JPanel msgPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameText;
    private javax.swing.JPanel northPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JLabel pBarLabel;
    private javax.swing.JPanel pBarPanel;
    private org.jreserve.smoothing.visual.SmoothingTablePanel smoothingTable;
    private javax.swing.JPanel soutPanel;
    // End of variables declaration//GEN-END:variables
}
