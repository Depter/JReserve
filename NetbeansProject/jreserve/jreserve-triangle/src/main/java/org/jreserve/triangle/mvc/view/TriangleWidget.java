package org.jreserve.triangle.mvc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import org.jreserve.resources.ToolBarToggleButton;
import org.jreserve.triangle.mvc.model.TriangleTable;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.TriangleWidget.Digits=Digits:"
})
public class TriangleWidget extends JPanel implements Serializable {
    
    private final static Dimension INTERNAL_SPACIN = new Dimension(0, 0);
    private final static String DEVELOPMENT_PERIOD_STRUCTURE_ACTION = "DEVELOPMENT_PERIOD_STRUCTURE_ACTION";
    private final static String DEVELOPMENT_PERIOD_STRUCTURE_ICON = "resources/triangle.png";
    private final static String CALENDAR_PERIOD_STRUCTURE_ACTION = "CALENDAR_PERIOD_STRUCTURE_ACTION";
    private final static String CALENDAR_PERIOD_STRUCTURE_ICON = "resources/calendar_triangle.png";

    private final static String CUMMULATED_ACTION = "CUMMULATED_ACTION";
    private final static String CUMMULATED_ICON = "resources/arrow_up.png";
    private final static String NOT_CUMMULATED_ACTION = "NOT_CUMMLATED_ACTION";
    private final static String NOT_CUMMULATED_ICON = "resources/arrow_down.png";
    
    private final static int TOOLBAR_STRUT = 5;
    private final static int DEFAULT_COLUMN_WIDTH = 75;
    
    private DecimalSpinner spinner;
    private JToolBar toolBar;
    private ToolBarToggleButton cummulatedButton;
    private ToolBarToggleButton notCummulatedButton;
    private ActionHandler actionHandler = new ActionHandler();
    
    private TriangleTableModel tableModel;
    private JTable table;
    private JScrollPane scroll;
    
    private DoubleTriangleTableRenderer doubleRenderer;
    
    public TriangleWidget() {
        initComponent();
        super.addComponentListener(new ResizeListener());
    }
    
    private void initComponent() {
        super.setLayout(new BorderLayout());
        super.add(getToolBar(), BorderLayout.NORTH);
        super.add(getTable(), BorderLayout.CENTER);
        super.setBorder(BorderFactory.createLoweredBevelBorder());
    }
    
    private JToolBar getToolBar() {
        toolBar = new JToolBar();
        
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.add(new JLabel(Bundle.LBL_TriangleWidget_Digits()));
        spinner = new DecimalSpinner();
        Dimension size = new Dimension(40, 18);
        spinner.setMinimumSize(size);
        spinner.setMaximumSize(size);
        spinner.setPreferredSize(size);
        spinner.addChangeListener(new DecimalListener());
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.add(spinner);
        
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.addSeparator();
        
        ButtonGroup group = new ButtonGroup();
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.add(createDevelopmentPeriodButton(group));
        
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.add(createCalendarPeriodButton(group));
        
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.addSeparator();
        
        group = new ButtonGroup();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.add(createNotCummulatedButton(group));
        
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.add(createCummulatedButton(group));
        
        toolBar.setVisible(true);
        toolBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        return toolBar;
    }
    
    private JToggleButton createDevelopmentPeriodButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(DEVELOPMENT_PERIOD_STRUCTURE_ICON, false);
        JToggleButton button = new ToolBarToggleButton(i);
        button.setActionCommand(DEVELOPMENT_PERIOD_STRUCTURE_ACTION);
        button.addActionListener(actionHandler);
        button.setSelected(true);
        group.add(button);
        return button;
    }
    
    private JToggleButton createCalendarPeriodButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(CALENDAR_PERIOD_STRUCTURE_ICON, false);
        JToggleButton button = new ToolBarToggleButton(i);
        button.setActionCommand(CALENDAR_PERIOD_STRUCTURE_ACTION);
        button.addActionListener(actionHandler);
        button.setSelected(false);
        group.add(button);
        return button;
    }
    
    private JToggleButton createNotCummulatedButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(NOT_CUMMULATED_ICON, false);
        notCummulatedButton = new ToolBarToggleButton(i);
        notCummulatedButton.setActionCommand(NOT_CUMMULATED_ACTION);
        notCummulatedButton.addActionListener(actionHandler);
        notCummulatedButton.setSelected(true);
        group.add(notCummulatedButton);
        return notCummulatedButton;
    }
    
    private JToggleButton createCummulatedButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(CUMMULATED_ICON, false);
        cummulatedButton = new ToolBarToggleButton(i);
        cummulatedButton.setActionCommand(CUMMULATED_ACTION);
        cummulatedButton.addActionListener(actionHandler    );
        cummulatedButton.setSelected(false);
        group.add(cummulatedButton);
        return cummulatedButton;
    }
    
    private JScrollPane getTable() {
        tableModel = new TriangleTableModel();
        table = new JTable(tableModel);
        doubleRenderer = new DoubleTriangleTableRenderer(tableModel);
        table.getTableHeader().setDefaultRenderer(new TriangleWidgetHeaderRenderer());
        table.setDefaultRenderer(Double.class, doubleRenderer);
        DateTriangleTableRenderer dateRenderer = new DateTriangleTableRenderer(table);
        table.setDefaultRenderer(Date.class, dateRenderer);
        tableModel.setColumnRenderer(dateRenderer);
        table.setFillsViewportHeight(false);
        table.setIntercellSpacing(INTERNAL_SPACIN);
        table.getModel().addTableModelListener(new TableListener());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }
    
//    @Override
//    public void setLayout(LayoutManager layout) {
//    }
    
    public void setShowsToolbar(boolean showsToolBar) {
        toolBar.setVisible(showsToolBar);
    }
    
    public boolean getShowsToolbar() {
        return toolBar.isVisible();
    }
    
    public boolean isCummulated() {
        return tableModel.isCummulated();
    }
    
    public void setCummulated(boolean cummulated) {
        selectCummulatedButton(cummulated);
        tableModel.setCummulated(cummulated);
    }
    
    private void selectCummulatedButton(boolean cummulated) {
        cummulatedButton.setSelected(cummulated);
        notCummulatedButton.setSelected(!cummulated);
    }
    
    public TriangleTable getTableAt(int layer) {
        return tableModel.getTableAt(layer);
    }
    
    public void addTable(TriangleTable table) {
        tableModel.addTable(table);
    }
    
    public void setTable(TriangleTable table, int layer) {
        tableModel.setTable(table, layer);
    } 
    
    public void removeTable(int layer) {
        tableModel.removeTable(layer);
    }
    
    private class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            int width = getWidth();
            if(scroll.getWidth() > width)
                scroll.setSize(width, scroll.getHeight());
        }
    }
    
    private class TableListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            for(int c=0, count=table.getColumnCount(); c<count; c++) {
                TableColumn column = table.getColumnModel().getColumn(c);
                column.setMinWidth(DEFAULT_COLUMN_WIDTH);
            }
        }
    }
    
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals(CUMMULATED_ACTION)) {
                tableModel.setCummulated(true);
            } else if(command.equals(NOT_CUMMULATED_ACTION)) {
                tableModel.setCummulated(false);
            } else if(command.equals(CALENDAR_PERIOD_STRUCTURE_ACTION)) {
                tableModel.setModelType(TriangleTableModel.ModelType.CALENDAR);
            } else if(command.equals(DEVELOPMENT_PERIOD_STRUCTURE_ACTION)) {
                tableModel.setModelType(TriangleTableModel.ModelType.DEVELOPMENT);
            }

        }
    }
    
    private class DecimalListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int value = spinner.getIntValue();
            doubleRenderer.setFractionDigits(value);
            tableModel.fireRowsChanged();
        }
    }
}
