package org.jreserve.triangle.mvc.view;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Peter Decsi
 */
public abstract class LayerTextEditor implements TableCellEditor {

    private final static int EDIT_CLICK_COUNT = 2;
    
    private List<CellEditorListener> listeners = new ArrayList<CellEditorListener>();
    private Callback callback;
    protected Object originalValue;
    protected int editedRow;
    protected int editedColumn;
    
    protected LayerTextEditor(Callback cb) {
        this.callback = cb;
    }
    
    public int getEditedRow() {
        return editedRow;
    }
    
    public int getEditedColumn() {
        return editedColumn;
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
        fireEditingCanceled();
        if(callback != null)
            callback.editingStopped(this);
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override
    public Object getCellEditorValue() {
        return originalValue;
    }

    public abstract Object getEditorComponentValue();
    
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
    
    public static interface Callback {
        
        public void editingStopped(LayerTextEditor editor);
    }
    
}
