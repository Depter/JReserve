package org.jreserve.logging.settings;

import java.awt.Component;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LevelSelectEditor extends JComboBox implements TableCellEditor {
    private final static int EDIT_CLICK_COUNT = 1;

    private final static Level[] LEVELS = {
        Level.OFF,
        Level.SEVERE,
        Level.WARNING,
        Level.INFO,
        Level.CONFIG,
        Level.FINE,
        Level.FINER,
        Level.FINEST,
        Level.ALL
    };
    
    private List<CellEditorListener> listeners = new ArrayList<CellEditorListener>();
    
    LevelSelectEditor() {
        super(LEVELS);
        super.setRenderer(new LevelRenderer());

        EditStopListener l = new EditStopListener();
        super.addFocusListener(l);
        super.addKeyListener(l);
        super.addPopupMenuListener(l);
        super.addActionListener(l);
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
    
    private void fireEditingStopped() {
        ChangeEvent evt = new ChangeEvent(this);
        for(CellEditorListener listener : new ArrayList<CellEditorListener>(listeners))
            listener.editingStopped(evt);
    }
    
    private void fireEditingCanceled() {
        ChangeEvent evt = new ChangeEvent(this);
        for(CellEditorListener listener : new ArrayList<CellEditorListener>(listeners))
            listener.editingCanceled(evt);
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
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

    @Override
    public Object getCellEditorValue() {
        return getSelectedItem();
    }
   
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        setSelectedItem(value);
        return this;
    }
    
    private class EditStopListener implements FocusListener, KeyListener, PopupMenuListener, ActionListener {

        @Override public void focusGained(FocusEvent e) {}
        
        @Override
        public void focusLost(FocusEvent e) {
            cancelCellEditing();
        }

        @Override public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if(KeyEvent.VK_ESCAPE == e.getKeyCode())
                cancelCellEditing();
        }

        @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
        @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            cancelCellEditing();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
    }
}
