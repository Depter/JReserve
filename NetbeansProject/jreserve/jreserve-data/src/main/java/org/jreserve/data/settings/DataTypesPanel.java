package org.jreserve.data.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.data.settings.DataTypeTableModel.DTDummy;
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
    "LBL.DataTypesPanel.reset=Reset",
    "# {0} - the name",
    "MSG.DataTypesPanel.nameexists=Name \"{0}\" used more than once!"
})
public class DataTypesPanel extends JPanel implements ActionListener, ListSelectionListener, TableModelListener {

    private final static String ERR_ICON = "org/netbeans/modules/dialogs/error.gif";
    
    private DataTypesOptionsPanelController controller;
    private DataTypeTableModel tableModel = new DataTypeTableModel();
    private JTable table;
    private JButton addButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JLabel msgLabel;
    private boolean isValid = true;
    
    DataTypesPanel(DataTypesOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }
    
    
    void load() {
        tableModel.load();
    }
    
    void store() {
        if(removeDeleted() | updateList())
            DataTypeUtil.save();
    }
    
    private boolean removeDeleted() {
        boolean deleted = false;
        for(DataType dt : DataTypeUtil.getDataTypes())
            if(tableModel.getDummy(dt.getDbId()) == null) {
                deleted = true;
                DataTypeUtil.deleteDataType(dt);
            }
        return deleted;
    }
    
    private boolean updateList() {
        boolean updated = false;
        int rowCount = tableModel.getRowCount();
        for(int r=0; r<rowCount; r++) {
            if(updateRow(r))
                updated = true;
        }
        return updated;
    }
    
    private boolean updateRow(int row) {
        DTDummy dummy = tableModel.getDummyAtRow(row);
        DataType dt = DataTypeUtil.getDataType(dummy.getId());
        if(dt == null) {
            createDataType(dummy);
            return true;
        } else {
            return updateDataType(dt, dummy);
        }
    }
    
    private void createDataType(DTDummy dummy) {
        int id = dummy.getId();
        String name = dummy.getName();
        boolean isTriangle = dummy.isTriangle();
        DataTypeUtil.createDataType(id, name, isTriangle);
    }
    
    private boolean updateDataType(DataType dt, DTDummy dummy) {
        boolean updated = false;
        if(!dt.getName().equals(dummy.getName())) {
            updated = true;
            DataTypeUtil.setName(dt, dummy.getName());
        }
        if(dt.isTriangle() != dummy.isTriangle()) {
            updated = true;
            DataTypeUtil.setTriangle(dt, dummy.isTriangle());
        }
        return updated;
    }
    
    boolean valid() {
        return isValid;
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.getSelectionModel().addListSelectionListener(this);
        table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        tableModel.addTableModelListener(this);
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
        deleteButton.setEnabled(true);
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
        msgLabel.setVisible(false);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(msgLabel, BorderLayout.LINE_START);
        panel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == addButton) {
            DTDummyCreator.showDialog(tableModel);
        } else if (source == deleteButton) {
            deleteRows();
        } else if (source == resetButton) {
            tableModel.reloadDefaults();
        }
    }

    private void deleteRows() {
        DTDummy[] dummies = getDummies(table.getSelectedRows());
        tableModel.removeDummies(dummies);
    }
    
    private DTDummy[] getDummies(int[] rows) {
        int size = rows.length;
        DTDummy[] dummies = new DTDummy[size];
        for(int i=0; i<size; i++)
            dummies[i] = tableModel.getDummy((Integer) tableModel.getValueAt(rows[i], 0));
        return dummies;
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        deleteButton.setEnabled(table.getSelectedRowCount() > 0);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        msgLabel.setVisible(false);
        isValid = checkInput();
        controller.changed();
    }
    
    private boolean checkInput() {
        String[] names = getNames();
        for(int row = 0, size=names.length; row < size; row++)
            if(!checkNewNameAt(row, names))
                return false;
        return true;
    }
    
    private String[] getNames() {
        int rowCount = tableModel.getRowCount();
        String[] names = new String[rowCount];
        for(int row=0; row<rowCount; row++)
            names[row] = (String) tableModel.getValueAt(row, 1);
        return names;
    }
    
    private boolean checkNewNameAt(int row, String[] names) {
        String name = names[row];
        for(int r=0; r<row; r++) {
            if(names[r].equalsIgnoreCase(name)) {
                showError(Bundle.MSG_DataTypesPanel_nameexists(name));
                return false;
            }
        }
        return true;
    }
    
    private void showError(String msg) {
        msgLabel.setText(msg);
        msgLabel.setVisible(true);
    }
}
