package org.jreserve.project.entities.project.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.project.ProjectElement;
import org.jreserve.resources.ToolBarButton;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.actions.CopyAction;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ProjectLogView.AllLogType=All"
})
class ProjectLogView extends JPanel implements MultiViewElement, ActionListener {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static DateFormat DF = new SimpleDateFormat(DATE_FORMAT);
    
    private JToolBar toolBar;
    private MultiViewElementCallback callBack;
    private ProjectElement element;
    
    private JScrollPane tableScroll;
    private ChangeLogTableModel tableModel;
    private JTable table;

    private LogTypeCombo logTypeCombo;
    
    public ProjectLogView(ProjectElement element) {
        this.element = element;
        initTable();
        initComponents();
        createToolBar();
    }
    
    private void initTable() {
        tableModel = new ChangeLogTableModel(element);
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setCellRenderer(new DateRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new LogTypeRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(0).setMinWidth(10);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(10);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setMinWidth(150);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        tableScroll = new JScrollPane(table);
        add(tableScroll, BorderLayout.CENTER);
    }
    
    private void createToolBar() {
        toolBar = new JToolBar();
        toolBar.add(new JToolBar.Separator());

        logTypeCombo = new LogTypeCombo();
        logTypeCombo.addActionListener(this);
        Dimension size = new Dimension(100, 18);
        logTypeCombo.setPreferredSize(size);
        logTypeCombo.setMaximumSize(size);
        logTypeCombo.setMinimumSize(size);
        toolBar.add(logTypeCombo);
        toolBar.add(Box.createHorizontalStrut(5));
        
        registerCopyAction();
        toolBar.add(new ToolBarButton(SystemAction.get(CopyAction.class)));
        //toolBar.add(SystemAction.get(CopyAction.class));
        toolBar.add(Box.createHorizontalGlue());
    }
    
    private void registerCopyAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke("control C");
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DefaultEditorKit.copyAction);
        table.getInputMap().put(stroke, DefaultEditorKit.copyAction);
        getActionMap().put(DefaultEditorKit.copyAction, new CopyLogAction());
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(logTypeCombo == source)
            tableModel.setLogType(logTypeCombo.getSelectedType());
    }
    
    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        callBack = mvec;
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        if(callBack == null)
            return new Action[0];
        return callBack.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        return element.getLookup();
    }

    @Override public void componentOpened() {}
    @Override public void componentClosed() {}
    @Override public void componentShowing() {}
    @Override public void componentHidden() {}
    @Override public void componentActivated() {}
    @Override public void componentDeactivated() {}

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
    
    private ChangeLog.Type[] getChangeLogTypes() {
        ChangeLog.Type[] values = ChangeLog.Type.values();
        ChangeLog.Type[] types = new ChangeLog.Type[values.length+1];
        System.arraycopy(values, 0, types, 1, values.length);
        return types;
    }
    
    private static class DateRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(value instanceof Date)
                setText(DF.format((Date) value));
            return this;
        }
    }
    
    private static class LogTypeRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(value instanceof ChangeLog.Type)
                setText(((ChangeLog.Type) value).getUserName());
            return this;
        }
    }
    
    private class CopyLogAction extends AbstractAction {
        
        private StringBuilder sb = new StringBuilder();
                
        @Override
        public void actionPerformed(ActionEvent e) {
            StringSelection text = new StringSelection(getText());
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(text, null);
        }
        
        private String getText() {
            sb.setLength(0);
            ChangeLogTableModel model = (ChangeLogTableModel) table.getModel();
            for(ChangeLog log : model.getLogs())
                appendLog(log);
            return sb.toString();
        }
        
        private void appendLog(ChangeLog log) {
            if(sb.length() != 0)
                sb.append("\n");
            sb.append(DF.format(log.getCreationTime())).append('\t');
            sb.append(log.getUserName()).append('\t');
            sb.append(log.getType().getUserName()).append('\t');
            sb.append(log.getLogMessage());
        }
    }
    
    private class LogTypeCombo extends JComboBox {
        
        private LogTypeCombo() {
            super(getChangeLogTypes());
            setRenderer(new LogTypeComboRenderer());
        }
    
        public ChangeLog.Type getSelectedType() {
            return (ChangeLog.Type) getSelectedItem();
        }
    }
    
    private static class LogTypeComboRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setText(value);
            return this;
        }
        
        private void setText(Object value) {
            if(value instanceof ChangeLog.Type)
                setText(((ChangeLog.Type)value).getUserName());
            else
                setText(Bundle.LBL_ProjectLogView_AllLogType());
        }
    }
}
