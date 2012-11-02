package org.jreserve.triangle.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.resources.ToolBarToggleButton;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.data.TriangleCell;
import org.jreserve.triangle.widget.util.DecimalSpinner;
import org.jreserve.triangle.widget.util.TriangleTable;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 */
@NbBundle.Messages({
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
    
    private TriangleTable table;
    private JScrollPane scroll;
    private Lookup lookup;
    
    public TriangleWidget() {
        initComponent();
        lookup = new ProxyLookup(Lookups.singleton(this), table.getLookup());
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
        table = new org.jreserve.triangle.widget.util.TriangleTable();
        
        table.setFillsViewportHeight(false);
        table.setIntercellSpacing(INTERNAL_SPACIN);
        table.getModel().addTableModelListener(new TableListener());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }
    
    public void setShowsToolbar(boolean showsToolBar) {
        toolBar.setVisible(showsToolBar);
    }
    
    public boolean getShowsToolbar() {
        return toolBar.isVisible();
    }
    
    public void addTriangleWidgetListener(TriangleWidgetListener listener) {
        table.addTriangleWidgetListener(listener);
    }
    
    public void removeTriangleWidgetListener(TriangleWidgetListener listener) {
        table.removeTriangleWidgetListener(listener);
    }
    
    public void setPopUpActionPath(String path) {
        table.setPopUpActionPath(path);
    }
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        table.setTriangleGeometry(geometry);
    }
    
    public TriangleGeometry getTriangleGeometry() {
        return table.getTriangleGeometry();
    }
    
    public boolean isCummulated() {
        return table.isCummulated();
    }
    
    public void setCummulated(boolean cummulated) {
        selectCummulatedButton(cummulated);
        table.setCummulated(cummulated);
    }
    
    private void selectCummulatedButton(boolean cummulated) {
        cummulatedButton.setSelected(cummulated);
        notCummulatedButton.setSelected(!cummulated);
    }
    
    public void addValueLayer(List<WidgetData<Double>> datas) {
        table.addValueLayer(datas);
    }
    
    public <T extends PersistentObject> void addDataValueLayer(List<Data<T, Double>> datas) {
        table.addValueLayer(escapeData(datas));
    }
    
    private <T extends PersistentObject> List<WidgetData<Double>> escapeData(List<Data<T, Double>> datas) {
        List<WidgetData<Double>> escaped = new ArrayList<WidgetData<Double>>(datas.size());
        for(Data<T, Double> data : datas)
            escaped.add(new WidgetData<Double>(data.getAccidentDate(), data.getDevelopmentDate(), data.getValue()));
        return escaped;
    }
    
    public void setValueLayer(int layer, List<WidgetData<Double>> datas) {
        table.setValueLayer(layer, datas);
    }
    
    public <T extends PersistentObject> void setDataValueLayer(int layer, List<Data<T, Double>> datas) {
        List<WidgetData<Double>> escaped = escapeData(datas);
        table.setValueLayer(layer, escaped);
    }
    
    public List<WidgetData<Double>> getValueLayer(int layer) {
        return table.getValueLayer(layer);
    }
    
    public void removeComment(WidgetData<Comment> comments) {
        table.removeComment(comments);
    }
    
    public void setComments(List<WidgetData<Comment>> comments) {
        table.setComments(comments);
    }
    
    public void addComments(List<WidgetData<Comment>> comments) {
        table.addComments(comments);
    }
    
    public void addComment(WidgetData<Comment> comment) {
        table.addComment(comment);
    }
    
    public List<WidgetData<Comment>> getComments() {
        return table.getComments();
    }
    
    public void setLayerBackground(int layer, Color color) {
        table.setLayerBackground(layer, color);
    }
    
    public void setLayerForeground(int layer, Color color) {
        table.setLayerForeground(layer, color);
    }
    
    public void setLayerColor(int layer, Color bg, Color fg) {
        table.setLayerColor(layer, bg, fg);
    }
    
    public void setEditableLayer(int layer) {
        table.setEditableLayer(layer);
    }
    
    public void refreshTableData() {
        javax.swing.event.TableModelEvent evt = new javax.swing.event.TableModelEvent(table.getModel());
        table.tableChanged(evt);
    }
    
    public double[][] flatten() {
        return table.flatten();
    }

    public Lookup getLookup() {
        return lookup;
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
                table.setCummulated(true);
            } else if(command.equals(NOT_CUMMULATED_ACTION)) {
                table.setCummulated(false);
            } else if(command.equals(CALENDAR_PERIOD_STRUCTURE_ACTION)) {
                table.setModelType(org.jreserve.triangle.widget.model.TriangleModel.ModelType.CALENDAR);
            } else if(command.equals(DEVELOPMENT_PERIOD_STRUCTURE_ACTION)) {
                table.setModelType(org.jreserve.triangle.widget.model.TriangleModel.ModelType.DEVELOPMENT);
            }
        }
    }
    
    private class DecimalListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int value = spinner.getIntValue();
            table.setFractionDigits(value);
            fireRowsChanged();
        }
        
        private void fireRowsChanged() {
            TableModelEvent evt = new TableModelEvent(table.getModel());
            table.tableChanged(evt);
        }
    }
    
    public static interface TriangleWidgetListener {
        
        public void cellEdited(TriangleCell cell, int layer, Double oldValue, Double newValue);
        
        public void commentsChanged();
    }
}
