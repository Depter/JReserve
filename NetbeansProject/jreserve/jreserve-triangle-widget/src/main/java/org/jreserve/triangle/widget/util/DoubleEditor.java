package org.jreserve.triangle.widget.util;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.localesettings.util.LocaleSettings;
import org.jreserve.resources.textfieldfilters.DoubleFilter;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleEditor implements TableCellEditor {

    private final static int EDIT_CLICK_COUNT = 2;
    
    private List<CellEditorListener> listeners = new ArrayList<CellEditorListener>();
    private DoubleRenderer renderer = new DoubleRenderer();
    private JTextField editor;

    public DoubleEditor() {
        DecimalFormatSymbols symbols = LocaleSettings.getDecimalSymbols();
        editor = new JTextField();
        editor.setDocument(new DoubleFilter(symbols.getDecimalSeparator()));
    }
    
    @Override
    public boolean isCellEditable(EventObject evt) {
        return evt == null || 
               !(evt instanceof MouseEvent) ||
               ((MouseEvent) evt).getClickCount() >= EDIT_CLICK_COUNT;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override
    public Object getCellEditorValue() {
        String str = editor.getText();
        if(str==null || str.trim().length()==0)
            return null;
        return renderer.parse(str);
    }
    
    @Override
    public void addCellEditorListener(CellEditorListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener listener) {
        listeners.remove(listener);
    }
    
    private void fireEditingCanceled() {
        ChangeEvent evt = new ChangeEvent(this);
        for(CellEditorListener listener : new ArrayList<CellEditorListener>(listeners))
            listener.editingCanceled(evt);
    }
    
    private void fireEditingStopped() {
        ChangeEvent evt = new ChangeEvent(this);
        for(CellEditorListener listener : new ArrayList<CellEditorListener>(listeners))
            listener.editingStopped(evt);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String text = getStringValue(value);
        editor.setText(text);
        return editor;
    }
    
    private String getStringValue(Object value) {
        if(value instanceof Double)
            return renderer.toString((Double) value).trim();
        return null;
    }
}
