package org.jreserve.data.datatypesetting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jreserve.data.ProjectDataType;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.DataTypePanel.add=Add",
    "LBL.DataTypePanel.delete=Delete",
    "LBL.DataTypePanel.reset=Reset",
    "# {0} - the name",
    "MSG.DataTypePanel.nameexists=Name \"{0}\" used more than once!",
    "MSG.DataTypePanel.nameempty=Empty names are not allowed!",
    "# {0} - name",
    "# {1} - max length",
    "MSG.DataTypePanel.nametoolong=Name \"{0}\" is longer than {1} characters!"
})
public class DataTypePanel extends JPanel implements TableModelListener, ListSelectionListener, ActionListener {
    
    public final static String PROP_IS_VALID = "IS_PANEL_VALID";
    
    private final static String ERR_ICON = "org/netbeans/modules/dialogs/error.gif";
    
    private boolean addButtons;
    private JPanel buttonPanel;
    private DataTypeTableModel tableModel = new DataTypeTableModel();
    private JTable table;
    private JButton addButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JLabel msgLabel;
    private boolean isValid = true;
    private int clickedRow = -1;
    
    public DataTypePanel() {
        this.addButtons = true;
        initComponents();
        super.putClientProperty(PROP_IS_VALID, isValid);
    }
    
    public DataTypePanel(boolean addButtons) {
        this.addButtons = addButtons;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));

        initTable();
        tableModel.addTableModelListener(this);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        add(scroll, BorderLayout.CENTER);
        
        add(getSouthPanel(), BorderLayout.PAGE_END);
        setPreferredSize(new Dimension(300, 400));
    }
    
    private void initTable() {
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.getSelectionModel().addListSelectionListener(this);
        table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        table.addMouseListener(new PopUpListener());
        table.setFillsViewportHeight(true);
    }
    
    private JPanel getSouthPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        buttonPanel = createButtonPanel();
        if(addButtons)
        panel.add(buttonPanel, BorderLayout.PAGE_START);
        panel.add(getMsgLabel(), BorderLayout.PAGE_END);
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        
        addButton = new JButton(Bundle.LBL_DataTypePanel_add());
        addButton.addActionListener(this);
        panel.add(addButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        deleteButton = new JButton(Bundle.LBL_DataTypePanel_delete());
        deleteButton.addActionListener(this);
        deleteButton.setEnabled(true);
        panel.add(deleteButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
       
        resetButton = new JButton(Bundle.LBL_DataTypePanel_reset());
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
        } else if (source instanceof JMenuItem) {
            deleteClickedRows();
        }
    }

    private void deleteRows() {
        DTDummy[] dummies = getDummies(table.getSelectedRows());
        tableModel.removeDummies(dummies);
    }
    
    private void deleteClickedRows() {
        if(table.getSelectedRows().length > 0)
            deleteRows();
        else if(clickedRow >= 0)
            tableModel.removeDummies(getDummies(new int[]{clickedRow}));
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
        super.putClientProperty(PROP_IS_VALID, isValid);
    }
    
    private boolean checkInput() {
        String[] names = getNames();
        for(int row = 0, size=names.length; row < size; row++)
            if(!checkNameLength(names[row]) || !checkNewNameAt(row, names))
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
                showError(Bundle.MSG_DataTypePanel_nameexists(name));
                return false;
            }
        }
        return true;
    }
    
    private void showError(String msg) {
        msgLabel.setText(msg);
        msgLabel.setVisible(true);
    }
    
    private boolean checkNameLength(String name) {
        if(name == null || name.trim().length() == 0) {
            showError(Bundle.MSG_DTDummyCreator_name_empty());
            return false;
        } else if(name.length() > ProjectDataType.MAX_NAME_LENGTH) {
            showError(Bundle.MSG_DataTypePanel_nametoolong(name, ProjectDataType.MAX_NAME_LENGTH));
            return false;
        }
        return true;
    }
    
    public boolean valid() {
        return isValid;
    }
    
    public DTDummy getDummy(int dbId) {
        return tableModel.getDummy(dbId);
    }
    
    public int getDummyCount() {
        return tableModel.getRowCount();
    }
    
    public DTDummy getDummyAtRow(int row) {
        return tableModel.getDummyAtRow(row);
    }
    
    public void setDummies(List<DTDummy> dummies) {
        tableModel.setDTDummies(dummies);
    }
    
    public JPanel getButtonPanel() {
        return buttonPanel;
    }
    
    private class PopUpListener extends MouseAdapter {

        private JPopupMenu popUp;
        
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger())
                popUp(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger())
                popUp(e);
        }
        
        private void popUp(MouseEvent evt) {
            int x = evt.getX();
            int y = evt.getY();
            clickedRow = table.rowAtPoint(evt.getPoint());
            getPopUp().show(table, x, y);
        }
        
        private JPopupMenu getPopUp() {
            if(popUp == null)
                createPopUp();
            return popUp;
        }
        
        private void createPopUp() {
            popUp = new JPopupMenu();
            JMenuItem delete = new JMenuItem(Bundle.LBL_DataTypePanel_delete());
            delete.addActionListener(DataTypePanel.this);
            popUp.add(delete);
        }
    }
}
