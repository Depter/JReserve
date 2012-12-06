package org.jreserve.dataimport.clipboardtable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;
import org.jreserve.data.DataImport.ImportType;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.VisualPanel.name=Select data",
    "LBL.VisualPanel.Paste=Paste"
})
class VisualPanel extends JPanel implements ActionListener {

    private final static String PASTE_ACTION = "PASTE_ACTION";
 
    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    
    private InputPanel inputPanel;
    private DataTableModel tableModel = new DataTableModel();
    private DataValueRenderer valueRenderer;
    private DateRenderer dateRenderer;
    private JTable dataTable;
    
    VisualPanel() {
        initComponents();
        setName(Bundle.LBL_VisualPanel_name());
        dateRenderer = new DateRenderer(inputPanel.getDateFormat());
        valueRenderer = new DataValueRenderer(inputPanel.getDecimalFormat());
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        add(initInputPanel(), BorderLayout.NORTH);
        add(getTableScroll(), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    private JPanel initInputPanel() {
        inputPanel = new InputPanel();
        inputPanel.addActionListener(this);
        return inputPanel;
    }
    
    private JScrollPane getTableScroll() {
        dataTable = new JTable(tableModel);
        dataTable.setFillsViewportHeight(true);
        dataTable.addMouseListener(new PopUpMenu());
        JScrollPane scroll = new JScrollPane(dataTable);
        scroll.setPreferredSize(new Dimension(300, 300));
        return scroll;
    }
    
    void setClaimType(ProjectElement<ClaimType> element) {
        inputPanel.setClaimType(element);
        fireChangeEvent();
    }
    
    ProjectDataType getDataType() {
        return inputPanel.getDataType();
    }
    
    DateFormat getDateFormat() {
        return inputPanel.getDateFormat();
    }
    
    DecimalFormat getDecimalFormat() {
        return inputPanel.getDecimalFormat();
    }
    
    boolean isCummulated() {
        return inputPanel.isCummulated();
    }
    
    ImportType getImportType() {
        return inputPanel.getImportType();
    }
    
    List<DataDummy> getDummies() {
        return tableModel.getDummies();
    }
    
    public void addChangeListeners(ChangeListener listener) {
        synchronized(changeListeners) {
            if(!changeListeners.contains(listener))
                changeListeners.add(listener);
        }
    }
    
    public void removeChangeListener(ChangeListener listener) {
        synchronized(changeListeners) {
            changeListeners.remove(listener);
        }
    }
    
    private void fireChangeEvent() {
        List<ChangeListener> ls;
        synchronized(changeListeners) {
            ls = new ArrayList<ChangeListener>(changeListeners);
        }
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : ls)
            listener.stateChanged(evt);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if(InputPanel.DATA_TYPE_SELECT_ACTION.equals(action)) {
            setDataType();
        } else if(InputPanel.DATE_FORMAT_ACTION.equals(action)) {
            dateRenderer.setFormat(inputPanel.getDateFormat());
            setTableRenderers(tableModel.getDataType());
        } else if(InputPanel.NUMBER_FORMAT_ACTION.equals(action)) {
            valueRenderer.setFormat(inputPanel.getDecimalFormat());
            setTableRenderers(tableModel.getDataType());
        } else if(PASTE_ACTION.equals(action)) {
            pasteFromClipboard();
        }
        fireChangeEvent();
    }
    
    private void setDataType() {
        ProjectDataType type = inputPanel.getDataType();
        tableModel.setDataType(type);
        dateRenderer.setFormat(inputPanel.getDateFormat());
        valueRenderer.setFormat(inputPanel.getDecimalFormat());
        setTableRenderers(type);
    }
    
    private void setTableRenderers(ProjectDataType dataType) {
        if(dataType == null)
            return;
        TableColumnModel columnModel = dataTable.getColumnModel();
        if(dataType.isTriangle())
            setTriangleRenderers(columnModel);
        else
            setVectorRenderers(columnModel);
        tableModel.rerenderData();
    }
    
    private void setTriangleRenderers(TableColumnModel columnModel) {
        columnModel.getColumn(1).setCellRenderer(dateRenderer);
        columnModel.getColumn(2).setCellRenderer(dateRenderer);
        columnModel.getColumn(3).setCellRenderer(valueRenderer);
    }
    
    private void setVectorRenderers(TableColumnModel columnModel) {
        columnModel.getColumn(1).setCellRenderer(dateRenderer);
        columnModel.getColumn(2).setCellRenderer(valueRenderer);
    }
    
    private void pasteFromClipboard() {
        String data = getDataFromClipboard();
        if(data != null)
            tableModel.setClipboardData(data);
    }

    private String getDataFromClipboard() {
        try {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            return (String) cb.getData(DataFlavor.stringFlavor); 
        } catch (Exception ex) {
            return null;
        }
    }
    
    private boolean canPasteData() {
        return inputPanel.getDataType() != null;
    }
    
    private class PopUpMenu extends MouseAdapter {

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
            if(!canPasteData())
                return;
            JPopupMenu popUp = buildMenu();
            popUp.show(dataTable, evt.getX(), evt.getY());
        }
        
        private JPopupMenu buildMenu() {
            JPopupMenu menu = new JPopupMenu();
            menu.add(getPasteItem());
            return menu;
        }
        
        private JMenuItem getPasteItem() {
            JMenuItem item = new JMenuItem(Bundle.LBL_VisualPanel_Paste());
            item.setActionCommand(PASTE_ACTION);
            item.addActionListener(VisualPanel.this);
            return item;
        }
    }
}
