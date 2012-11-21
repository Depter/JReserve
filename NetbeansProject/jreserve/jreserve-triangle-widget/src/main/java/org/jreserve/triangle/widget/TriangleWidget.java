package org.jreserve.triangle.widget;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultEditorKit;
import org.jreserve.localesettings.util.DecimalSpinner;
import org.jreserve.resources.ToolBarButton;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.util.TriangleTable;
import org.openide.actions.CopyAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 */
@NbBundle.Messages({
    "LBL.TriangleWidget.Digits=Digits:",
    "LBL.TriangleWidget.ToolTip.Development=Development periods",
    "LBL.TriangleWidget.ToolTip.Calendar=Calendar periods",
    "LBL.TriangleWidget.ToolTip.Cummulate=Cummulate",
    "LBL.TriangleWidget.ToolTip.Deummulate=Decummulate",
    "LBL.TriangleWidget.ToolTip.ClipboardCopy=Copy to clipboard"
})
public class TriangleWidget extends JPanel implements Serializable {
    
    private final static Dimension INTERNAL_SPACING = new Dimension(0, 0);
    
    private final static String CUMMULATED_ACTION = "CUMMULATED_ACTION";
    private final static String CUMMULATED_ICON = "resources/arrow_up.png";
    private final static String NOT_CUMMULATED_ACTION = "NOT_CUMMLATED_ACTION";
    private final static String NOT_CUMMULATED_ICON = "resources/arrow_down.png";
    
    private final static int TOOLBAR_STRUT = 5;
    private final static int DEFAULT_COLUMN_WIDTH = 75;
    private final static Dimension PREFFERED_SIZE = new Dimension(500, 200);
    
    public final static Color CORRECTION_BG = new Color(235, 204, 204);
    public final static Color VALUE_BG = Color.WHITE;
    public final static Color SMOOTHING_BG = new Color(167, 191, 255);
    
    private DecimalSpinner spinner;
    private JToolBar toolBar;
    
    private boolean viewButtonsVisible = true;
    private List<Component> viewButtonComponents = new ArrayList<Component>();
    
    private boolean cummulateButtonsVisible = true;
    private List<Component> cummulateButtonComponents = new ArrayList<Component>();
    
    private JToggleButton cummulatedButton;
    private JToggleButton notCummulatedButton;
    private ActionHandler actionHandler = new ActionHandler();
    
    private TriangleTable table;
    private JScrollPane scroll;
    private Lookup lookup;
    
    public TriangleWidget() {
        initComponent();
        lookup = new ProxyLookup(Lookups.fixed(this, getActionMap()), table.getLookup());
        super.addComponentListener(new ResizeListener());
        registerCopyAction();
        setPreferredSize(PREFFERED_SIZE);
    }
    
    private void initComponent() {
        super.setLayout(new BorderLayout());
        super.add(getTable(), BorderLayout.CENTER);
        super.add(getToolBar(), BorderLayout.NORTH);
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
        createViewButtons(group);
        toolBar.addSeparator();
        
        group = new ButtonGroup();
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        toolBar.add(createNotCummulatedButton(group));
        
        Component c = Box.createHorizontalStrut(TOOLBAR_STRUT);
        cummulateButtonComponents.add(c);
        toolBar.add(c);
        toolBar.add(createCummulatedButton(group));
        
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(TOOLBAR_STRUT));
        ToolBarButton copyButton = new ToolBarButton(SystemAction.get(CopyAction.class));
        copyButton.setToolTipText(Bundle.LBL_TriangleWidget_ToolTip_ClipboardCopy());
        toolBar.add(copyButton);
        
        toolBar.setVisible(true);
        toolBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        return toolBar;
    }
    
    private void createViewButtons(ButtonGroup group) {
        TriangleModel[] models = getRegisteredModels();
        for(int m=0, size=models.length; m<size; m++) {
            createTriangleModelButton(group, models[m], m==0);
            Component strut = Box.createHorizontalStrut(TOOLBAR_STRUT);
            viewButtonComponents.add(strut);
            toolBar.add(strut);
        }
        if(models.length > 0)
        table.setTriangleModel(models[0].createInstance());
    }
    
    private TriangleModel[] getRegisteredModels() {
        return Lookup.getDefault()
              .lookupAll(TriangleModel.class)
              .toArray(new TriangleModel[0]);
    }
    
    private JToggleButton createTriangleModelButton(ButtonGroup group, TriangleModel model, boolean selected) {
        JToggleButton button = new JToggleButton(new ImageIcon(model.getIcon()));
        button.setToolTipText(model.getToolTipName());
        viewButtonComponents.add(button);
        toolBar.add(button);
        group.add(button);
        button.setSelected(selected);
        button.addActionListener(new ModelSelectAction(model));
        return button;
    }
    
    private JToggleButton createNotCummulatedButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(NOT_CUMMULATED_ICON, false);
        notCummulatedButton = new JToggleButton(i);
        notCummulatedButton.setActionCommand(NOT_CUMMULATED_ACTION);
        notCummulatedButton.addActionListener(actionHandler);
        notCummulatedButton.setSelected(true);
        notCummulatedButton.setToolTipText(Bundle.LBL_TriangleWidget_ToolTip_Deummulate());
        group.add(notCummulatedButton);
        cummulateButtonComponents.add(notCummulatedButton);
        return notCummulatedButton;
    }
    
    private JToggleButton createCummulatedButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(CUMMULATED_ICON, false);
        cummulatedButton = new JToggleButton(i);
        cummulatedButton.setActionCommand(CUMMULATED_ACTION);
        cummulatedButton.addActionListener(actionHandler    );
        cummulatedButton.setSelected(false);
        cummulatedButton.setToolTipText(Bundle.LBL_TriangleWidget_ToolTip_Cummulate());
        group.add(cummulatedButton);
        cummulateButtonComponents.add(cummulatedButton);
        return cummulatedButton;
    }
    
    private JScrollPane getTable() {
        table = new TriangleTable();
        
        table.setFillsViewportHeight(false);
        table.setIntercellSpacing(INTERNAL_SPACING);
        table.getModel().addTableModelListener(new TableListener());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        table.setLayerBackground(TriangleCell.VALUE_LAYER, VALUE_BG);
        table.setLayerBackground(TriangleCell.SMOOTHING_LAYER, SMOOTHING_BG);
        table.setLayerBackground(TriangleCell.CORRECTION_LAYER, CORRECTION_BG);
        
        scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }
    
    private void registerCopyAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke("control C");
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DefaultEditorKit.copyAction);
        table.getInputMap().put(stroke, DefaultEditorKit.copyAction);
        getActionMap().put(DefaultEditorKit.copyAction, new CopyDataAction());
    }
    
    public void setShowsToolbar(boolean showsToolBar) {
        toolBar.setVisible(showsToolBar);
    }
    
    public boolean getShowsToolbar() {
        return toolBar.isVisible();
    }
    
    public int getVisibleDigits() {
        return spinner.getIntValue();
    }
    
    public void setVisibleDigits(int digits) {
        if(digits < 0) 
            digits = 0;
        spinner.setValue(digits);
    }
    
    public void setViewButtonsVisible(boolean visible) {
        this.viewButtonsVisible = visible;
        for(Component c : viewButtonComponents)
            c.setVisible(visible);
    }
    
    public boolean getViewButtonsVisible() {
        return viewButtonsVisible;
    }
    
    public void setCummulateButtonsVisible(boolean visible) {
        this.cummulateButtonsVisible = visible;
        for(Component c : cummulateButtonComponents)
            c.setVisible(visible);
    }
    
    public boolean getCummulateButtonsVisible() {
        return cummulateButtonsVisible;
    }
    
    public void addTriangleWidgetListener(TriangleWidgetListener listener) {
        table.addTriangleWidgetListener(listener);
    }
    
    public void removeTriangleWidgetListener(TriangleWidgetListener listener) {
        table.removeTriangleWidgetListener(listener);
    }
    
    public void setManualEvents(boolean manualEvents) {
        table.setManualEvents(manualEvents);
    }
    
    public boolean isManualEvents() {
        return table.isManualEvents();
    }
    
    public void fireTriangleStructureChanged() {
        table.fireTriangleStructureChanged();
    }

    public void fireEdited(TriangleCell cell, Double old, Double current) {
        table.fireEdited(cell, old, current);
    }
    
    public void fireTriangleValuesChanged() {
        table.fireTriangleValuesChanged();
    }
    
    public void fireCommentsChanged() {
        table.fireCommentsChanged();
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
    
    public void setValueLayer(int layer, List<WidgetData<Double>> datas) {
        table.setValueLayer(layer, datas);
    }
    
    public List<WidgetData<Double>> getValueLayer(int layer) {
        return table.getValueLayer(layer);
    }
    
    public List<TriangleCell> getCells() {
        return table.getCells();
    }
    
    public TriangleCell[][] getCellArray() {
        return table.getCellArray();
    }
    
    public TriangleCell getCellAt(Date accident, Date development) {
        return table.getCellAt(accident, development);
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
    
    public double[][] flattenLayer(int layer) {
        return table.flattenLayer(layer);
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
    
    private class CopyDataAction extends AbstractAction {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = table.getClipboardString();
            StringSelection text = new StringSelection(str);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(text, null);
        }
    }
    
    private class ModelSelectAction implements ActionListener {
        
        private TriangleModel model;

        ModelSelectAction(TriangleModel model) {
            this.model = model.createInstance();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            table.setTriangleModel(model);
        }
    }
    
}
