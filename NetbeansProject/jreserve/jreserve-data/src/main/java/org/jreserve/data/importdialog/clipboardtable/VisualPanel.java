package org.jreserve.data.importdialog.clipboardtable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;
import org.jreserve.data.DataImport.ImportType;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.data.util.DataImportSettings;
import org.jreserve.project.entities.Project;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.VisualPanel.name=Import table",
    "LBL.VisualPanel.Paste=Paste"
})
class VisualPanel extends JPanel implements ActionListener {

    private final static String PASTE_ACTION = "PASTE";
 
    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    
    private InputPanel inputPanel;
    private DataTableModel tableModel = new DataTableModel();
    private DataValueRenderer valueRenderer;
    private DateRenderer dateRenderer = new DateRenderer(DataImportSettings.getDateFormat());
    private JTable dataTable;
    
    VisualPanel() {
        valueRenderer = new DataValueRenderer(DataImportSettings.getDecimalFormatter());
        initComponents();
        setName(Bundle.LBL_VisualPanel_name());
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
    
    void setProject(Project project) {
        inputPanel.setProject(project);
        fireChangeEvent();
    }
    
    ProjectDataType getDataType() {
        return inputPanel.getDataType();
    }
    
    String getDateFormat() {
        return inputPanel.getDateFormatString();
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
            dateRenderer = new DateRenderer(inputPanel.getDateFormatString());
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
