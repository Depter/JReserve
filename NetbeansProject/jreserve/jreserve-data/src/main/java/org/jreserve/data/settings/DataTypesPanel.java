package org.jreserve.data.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataTypesPanel.add=Add"
})
public class DataTypesPanel extends JPanel {

    private final static String ERR_ICON = "org/netbeans/modules/dialogs/error.gif";
    
    private DataTypesOptionsPanelController controller;
    private DataTypeTableModel tableModel = new DataTypeTableModel();
    private JButton addButton;
    private JLabel msgLabel;
    
    DataTypesPanel(DataTypesOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }
    
    
    void load() {
        tableModel.load();
    }
    
    void store() {
    }
    
    boolean valid() {
        return false;
    }
    
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        
        add(getButtonPanel(), BorderLayout.PAGE_START);
        
        JTable table = new JTable(tableModel);
        table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        add(scroll, BorderLayout.CENTER);
        
        add(getMsgLabel(), BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(300, 400));
    }
    
    private JPanel getButtonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(Box.createGlue(), BorderLayout.CENTER);
        
        addButton = new JButton(Bundle.LBL_DataTypesPanel_add());
        panel.add(addButton, BorderLayout.LINE_END);
        
        return panel;
    }
    
    private JPanel getMsgLabel() {
        ImageIcon icon = ImageUtilities.loadImageIcon(ERR_ICON, false);
        msgLabel = new JLabel(icon);
        msgLabel.setText("Test text!");
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(msgLabel, BorderLayout.LINE_START);
        panel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        return panel;
    }
}
