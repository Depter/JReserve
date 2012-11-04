package org.jreserve.smoothing.geometric;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.NameDialog.Title=Geometric smoothing",
    "LBL.NameDialog.Name=Name:",
    "LBL.NameDialog.Ok=Ok",
    "LBL.NameDialog.Cancel=Cancel"
})
class NameDialog extends JPanel {

    
    private JTextField nameText;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel msgLabel;
    
    private NameDialog() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(Bundle.LBL_NameDialog_Name());
        panel.add(label, BorderLayout.LINE_START);
        nameText = new JTextField();
        panel.add(nameText, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
        
        
        add(getSouthPanel(), BorderLayout.CENTER);
    }
    
    private JPanel getSouthPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        okButton = new JButton(Bundle.LBL_NameDialog_Ok());
        buttons.add(okButton);
        buttons.add(Box.createHorizontalStrut(5));
        cancelButton = new JButton(Bundle.LBL_NameDialog_Cancel());
        buttons.add(cancelButton);
        panel.add(buttons, BorderLayout.NORTH);
        
        msgLabel = new JLabel();
        panel.add(msgLabel, BorderLayout.SOUTH);
        return panel;
    }
}
