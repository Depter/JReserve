package org.jreserve.project.entities.project.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.project.entities.project.ProjectElement;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 */
class ProjectLogView extends JPanel implements MultiViewElement {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private JToolBar toolBar = new JToolBar();
    private MultiViewElementCallback callBack;
    
    private JScrollPane tableScroll;
    private JTable table;

    public ProjectLogView(ProjectElement element) {
        initTable(element);
        initComponents();
    }
    
    private void initTable(ProjectElement element) {
        table = new JTable(new ChangeLogTableModel(element));
        table.getColumnModel().getColumn(0).setCellRenderer(new DateRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(0).setMinWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setMinWidth(150);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        tableScroll = new JScrollPane(table);
        //tableScroll.addComponentListener(resize);
        add(tableScroll, BorderLayout.CENTER);
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
        return Lookups.singleton(this);
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
    
    private static class DateRenderer extends DefaultTableCellRenderer {

        private DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(value instanceof Date)
                setText(df.format((Date) value));
            return this;
        }
    }
}
