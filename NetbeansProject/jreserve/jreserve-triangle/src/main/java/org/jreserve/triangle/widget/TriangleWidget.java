package org.jreserve.triangle.widget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Data;
import org.jreserve.data.model.DataTable;
import org.jreserve.resources.ToolBarToggleButton;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 */
public class TriangleWidget extends JPanel implements Serializable, ActionListener, ChangeListener {
    
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
    
    private DecimalSpinner spinner;
    private JToolBar toolBar;
    private ImportTableModel tableModel;
    private JTable table;
    
    private DoubleTriangleTableRenderer doubleRenderer;
    
    public TriangleWidget() {
        initComponent();
    }
    
    private void initComponent() {
        super.setLayout(new BorderLayout(5, 5));
        super.add(getToolBar(), BorderLayout.NORTH);
        super.add(getTable(), BorderLayout.CENTER);
        super.setBorder(BorderFactory.createLoweredBevelBorder());
    }
    
    private JToolBar getToolBar() {
        toolBar = new JToolBar();
        
        toolBar.add(new JLabel("Digits:"));
        spinner = new DecimalSpinner();
        Dimension size = new Dimension(40, 18);
        spinner.setMinimumSize(size);
        spinner.setMaximumSize(size);
        spinner.setPreferredSize(size);
        spinner.addChangeListener(this);
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
        toolBar.setBorder(BorderFactory.createRaisedBevelBorder());
        return toolBar;
    }
    
    private JToggleButton createDevelopmentPeriodButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(DEVELOPMENT_PERIOD_STRUCTURE_ICON, false);
        JToggleButton button = new ToolBarToggleButton(i);
        button.setActionCommand(DEVELOPMENT_PERIOD_STRUCTURE_ACTION);
        button.addActionListener(this);
        button.setSelected(true);
        group.add(button);
        return button;
    }
    
    private JToggleButton createCalendarPeriodButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(CALENDAR_PERIOD_STRUCTURE_ICON, false);
        JToggleButton button = new ToolBarToggleButton(i);
        button.setActionCommand(CALENDAR_PERIOD_STRUCTURE_ACTION);
        button.addActionListener(this);
        button.setSelected(false);
        group.add(button);
        return button;
    }
    
    private JToggleButton createNotCummulatedButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(NOT_CUMMULATED_ICON, false);
        JToggleButton button = new ToolBarToggleButton(i);
        button.setActionCommand(NOT_CUMMULATED_ACTION);
        button.addActionListener(this);
        button.setSelected(true);
        group.add(button);
        return button;
    }
    
    private JToggleButton createCummulatedButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(CUMMULATED_ICON, false);
        JToggleButton button = new ToolBarToggleButton(i);
        button.setActionCommand(CUMMULATED_ACTION);
        button.addActionListener(this);
        button.setSelected(false);
        group.add(button);
        return button;
    }
    
    private JScrollPane getTable() {
        tableModel = new ImportTableModel();
        table = new JTable(tableModel);
        doubleRenderer = new DoubleTriangleTableRenderer(tableModel);
        table.setDefaultRenderer(Double.class, doubleRenderer);
        table.setDefaultRenderer(Date.class, new DateTriangleTableRenderer(table));
        table.setFillsViewportHeight(false);
        table.setIntercellSpacing(INTERNAL_SPACIN);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }
    
    @Override
    public void setLayout(LayoutManager layout) {
    }
    
    public void setShowsToolbar(boolean showsToolBar) {
        toolBar.setVisible(showsToolBar);
    }
    
    public boolean getShowsToolbar() {
        return toolBar.isVisible();
    }
    
    public List<Data> getDatas() {
        return tableModel.getDatas();
    }
    
    public void setDatas(List<Data> datas) {
        tableModel.setDatas(datas);
    }
    
    public DataTable getDataTable() {
        return tableModel.getTable();
    }
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        tableModel.setGeometry(geometry);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int value = spinner.getIntValue();
        doubleRenderer.setMaximumFractionDigits(value);
        tableModel.fireTableDataChanged();
    }
    
}
