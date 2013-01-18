package org.jreserve.triangle.visual.widget;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.text.DefaultEditorKit;
import org.jreserve.localesettings.util.DecimalSpinner;
import org.jreserve.resources.ActionUtil;
import org.jreserve.resources.ToolBarButton;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.visual.widget.util.*;
import org.openide.actions.CopyAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TriangleWidget.Digits=Digits:",
    "LBL.TriangleWidget.ToolTip.ClipboardCopy=Copy to clipboard",
    "LBL.TriangleWidget.ToolTip.Deummulate=Decummulate",
    "LBL.TriangleWidget.ToolTip.Cummulate=Cummulate"
})
public class TriangleWidget extends JPanel implements Lookup.Provider {
    
    public final static String VISIBLE_DECIMALS_PROPERTY = "VISIBLE_DECIMALS";
    
    private final static Dimension INTERNAL_SPACING = new Dimension(0, 0);
    
    private final static String CUMMULATED_ACTION = "CUMMULATED_ACTION";
    private final static String CUMMULATED_ICON = "resources/arrow_up.png";
    private final static String NOT_CUMMULATED_ACTION = "NOT_CUMMLATED_ACTION";
    private final static String NOT_CUMMULATED_ICON = "resources/arrow_down.png";
    
    private final static int TOOLTIP_DISMISS_DELAY = 10 * 60 * 1000;
    
    private final static int TOOLBAR_STRUT = 5;
    private final static Dimension PREFFERED_SIZE = new Dimension(500, 200);
    
    private JToolBar toolBar;
    private DecimalSpinner spinner;
    
    private boolean viewButtonsVisible = true;
    private List<Component> viewButtonComponents = new ArrayList<Component>();
    
    private boolean cummulateButtonsVisible = true;
    private List<Component> cummulateButtonComponents = new ArrayList<Component>();
    
    private JToggleButton cummulatedButton;
    private JToggleButton notCummulatedButton;
    private ActionHandler actionHandler = new ActionHandler();
    
    private JTable table;
    private JScrollPane scroll;
    private Lookup lookup;
    private WidgetEditor widgetEditor;
    private TableModelListener dataListener = new DataListener();
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    private InstanceContent lkpContent = new InstanceContent();
    private AbstractLookup dataLookup = new AbstractLookup(lkpContent);
    private TriangularData.Provider source = null;
    private TriangularData data = TriangularData.EMPTY;
    
    private String popUpActionPath = null;
    
    private TriangleWidgetProperties properties = new TriangleWidgetProperties();
    
    public TriangleWidget() {
        initComponent();
        lookup = new ProxyLookup(Lookups.fixed(properties, getActionMap()), dataLookup);
        super.addComponentListener(new ResizeListener());
        registerCopyAction();
        setPreferredSize(PREFFERED_SIZE);    
    }
    
    public void setSource(TriangularData.Provider source) {
        clearLookup();
        this.source = source;
        this.data = source==null? TriangularData.EMPTY : source.getTriangularData();
        fillLookup();
    }
    
    private void clearLookup() {
        if(source != null)
            lkpContent.remove(source);
        lkpContent.remove(data);
    }
    
    private void fillLookup() {
        if(source != null)
            lkpContent.add(source);
        lkpContent.add(data);
    }
    
    public void setWidgetEditorFolder(String path) {
        if(this.widgetEditor != null)
            lkpContent.remove(widgetEditor);
        this.widgetEditor = WidgetEditorLoader.loadEditor(path);
        if(this.widgetEditor != null)
            lkpContent.add(this.widgetEditor);
    }
    
    public void setDecimalDigits(int digits) {
        if(digits < 0) digits = 0;
        spinner.setValue(digits);
    }
    
    public int getVisibleDigits() {
        return (Integer) spinner.getValue();
    }
    
    private void initComponent() {
        setLayout(new BorderLayout());
        add(getTable(), BorderLayout.CENTER);
        add(getToolBar(), BorderLayout.NORTH);
        setBorder(BorderFactory.createLoweredBevelBorder());
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
        properties.setVisibleDigits(spinner.getIntValue());
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
        List<WidgetTableModelImpl> models = WidgetTableModelRegistry.getModels();
        for(int m=0, size=models.size(); m<size; m++) {
            createTriangleModelButton(group, models.get(m), m==0);
            Component strut = Box.createHorizontalStrut(TOOLBAR_STRUT);
            viewButtonComponents.add(strut);
            toolBar.add(strut);
        }
        if(!models.isEmpty()) {
            table.setModel(models.get(0).getModel());
            table.getModel().addTableModelListener(dataListener);
        }
    }
    
    private JToggleButton createTriangleModelButton(ButtonGroup group, WidgetTableModelImpl model, boolean selected) {
        JToggleButton button = new JToggleButton(new ImageIcon(model.getIcon()));
        button.setToolTipText(model.getDisplayName());
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
        properties.setCummulated(false);
        notCummulatedButton.setToolTipText(Bundle.LBL_TriangleWidget_ToolTip_Deummulate());
        group.add(notCummulatedButton);
        cummulateButtonComponents.add(notCummulatedButton);
        return notCummulatedButton;
    }
    
    private JToggleButton createCummulatedButton(ButtonGroup group) {
        Icon i = ImageUtilities.loadImageIcon(CUMMULATED_ICON, false);
        cummulatedButton = new JToggleButton(i);
        cummulatedButton.setActionCommand(CUMMULATED_ACTION);
        cummulatedButton.addActionListener(actionHandler);
        cummulatedButton.setSelected(false);
        cummulatedButton.setToolTipText(Bundle.LBL_TriangleWidget_ToolTip_Cummulate());
        group.add(cummulatedButton);
        cummulateButtonComponents.add(cummulatedButton);
        return cummulatedButton;
    }
    
    private JScrollPane getTable() {
        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.putClientProperty(VISIBLE_DECIMALS_PROPERTY, 0);
        createTableRenderers();
        
        table.setFillsViewportHeight(false);
        table.setIntercellSpacing(INTERNAL_SPACING);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        table.addMouseListener(new MouseHandler());
                
        table.getSelectionModel().addListSelectionListener(new CellSelectionListener());
        
        scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }    
    
    private void createTableRenderers() {
        HeaderRenderer headerRenderer = new HeaderRenderer();
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.setDefaultRenderer(Date.class, headerRenderer);
        
        table.setDefaultRenderer(Double.class, new ValueRenderer());
        
        DoubleEditor editor = new DoubleEditor();
        table.setDefaultEditor(Double.class, editor);
    }
    
    private void registerCopyAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke("control C");
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DefaultEditorKit.copyAction);
        table.getInputMap().put(stroke, DefaultEditorKit.copyAction);
        getActionMap().put(DefaultEditorKit.copyAction, new CopyDataAction());
    }
    
    public boolean isCummulated() {
        return cummulatedButton.isSelected();
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    public void setPopupActionPath(String path) {
        this.popUpActionPath = path;
    }
    
    public String getPopupActionPath() {
        return this.popUpActionPath;
    }
    
    public WidgetTableModel getModel() {
        return (WidgetTableModel) table.getModel();
    }
    
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals(CUMMULATED_ACTION)) {
                setCummulated(true);
            } else if(command.equals(NOT_CUMMULATED_ACTION)) {
                setCummulated(false);
            }
        }
        
        private void setCummulated(boolean isCummulated) {
            properties.setCummulated(isCummulated);
        }
    }
    
    private class ResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            int width = getWidth();
            if(scroll.getWidth() > width)
                scroll.setSize(width, scroll.getHeight());
        }
    }
    
    private class DecimalListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int value = spinner.getIntValue();
            table.putClientProperty(VISIBLE_DECIMALS_PROPERTY, value);
            fireRowsChanged();
            properties.setVisibleDigits(value);
        }
        
        private void fireRowsChanged() {
            TableModelEvent evt = new TableModelEvent(table.getModel());
            table.tableChanged(evt);
        }
    }
    
    private class CopyDataAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = new TextExtractor(table.getModel()).extract();
            StringSelection text = new StringSelection(str);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(text, null);
        }
    }
    
    private class ModelSelectAction implements ActionListener {
        
        private WidgetTableModel model;

        ModelSelectAction(WidgetTableModelImpl model) {
            this.model = model.getModel();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Object o = table.getModel();
            if(o instanceof WidgetTableModel)
                initModelFromOld((WidgetTableModel) o);
            table.setModel(model);
        }
        
        private void initModelFromOld(WidgetTableModel oldModel) {
            oldModel.removeTableModelListener(dataListener);
            oldModel.setLookup(null);
            model.setLookup(getLookup());
            model.addTableModelListener(dataListener);
        }
    }    
    
    private class DataListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            fireChangeEvent();
        }
    }    
    
    
    private class MouseHandler extends MouseAdapter {

        private int originalDismissDelay;
        
        @Override
        public void mousePressed(MouseEvent e) {
            if(isPopupTrigger(e))
                showPopUp(e);
        }

        private boolean isPopupTrigger(MouseEvent e) {
            return popUpActionPath != null &&
                   e.isPopupTrigger();
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if(isPopupTrigger(e))
                showPopUp(e);
        }
        
        private void showPopUp(MouseEvent e) {
            JPopupMenu menu = ActionUtil.createPopupForPath(popUpActionPath);
            if(menu != null) {
                Point p = e.getPoint();
                menu.show(TriangleWidget.this.table, p.x, p.y);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ToolTipManager mg = ToolTipManager.sharedInstance();
            originalDismissDelay = mg.getDismissDelay();
            mg.setDismissDelay(TOOLTIP_DISMISS_DELAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(originalDismissDelay == 0)
                originalDismissDelay = 5 * 1000;
            ToolTipManager.sharedInstance().setDismissDelay(originalDismissDelay);
        }
    }
    
    private class CellSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            removeOldSelection();
            for(TriangleCell cell : getSelectedCells()) 
                lkpContent.add(cell);
        }
        
        private void removeOldSelection() {
            for(TriangleCell cell : getLookedupCells())
                lkpContent.remove(cell);
        }
        
        private List<TriangleCell> getLookedupCells() {
            return new ArrayList<TriangleCell>(
                    dataLookup.lookupAll(TriangleCell.class)
                    );
        }
        
        private List<TriangleCell> getSelectedCells() {
            int[] rows = table.getSelectedRows();
            int[] columns = table.getSelectedColumns();
            WidgetTableModel model = (WidgetTableModel) table.getModel();
            return model.getCells(rows, columns);
        }
    }
}
