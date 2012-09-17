package org.jreserve.data.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataTypesPanel.add=Add",
    "LBL.DataTypesPanel.delete=Delete",
    "LBL.DataTypesPanel.reset=Reset"
})
public class DataTypesPanel extends JPanel implements ActionListener {

    private final static String ERR_ICON = "org/netbeans/modules/dialogs/error.gif";
    
    private DataTypesOptionsPanelController controller;
    private DataTypeTableModel tableModel = new DataTypeTableModel();
    private JButton addButton;
    private JButton deleteButton;
    private JButton resetButton;
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

        JTable table = new JTable(tableModel);
        table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        add(scroll, BorderLayout.CENTER);
        
        add(getSouthPanel(), BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(300, 400));
    }
    
    private JPanel getSouthPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.add(getButtonPanel(), BorderLayout.PAGE_START);
        panel.add(getMsgLabel(), BorderLayout.PAGE_END);
        return panel;
    }
    
    private JPanel getButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
        
        addButton = new JButton(Bundle.LBL_DataTypesPanel_add());
        addButton.addActionListener(this);
        panel.add(addButton);
       
        deleteButton = new JButton(Bundle.LBL_DataTypesPanel_delete());
        deleteButton.addActionListener(this);
        panel.add(deleteButton);
       
        resetButton = new JButton(Bundle.LBL_DataTypesPanel_reset());
        resetButton.addActionListener(this);
        panel.add(resetButton);
       
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == addButton) {
            addDummy();
        }
        controller.changed();
    }
    
    private void addDummy() {
        
    }
}
