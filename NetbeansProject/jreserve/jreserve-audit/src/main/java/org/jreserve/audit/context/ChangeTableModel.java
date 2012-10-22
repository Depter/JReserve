package org.jreserve.audit.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jreserve.audit.AuditElement;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ChangeTableModel.When=Date",
    "LBL.ChangeTableModel.Who=User",
    "LBL.ChangeTableModel.Type=Type",
    "LBL.ChangeTableModel.Change=Change"
})
class ChangeTableModel implements TableModel {
    
    private List<AuditElement> changes = new ArrayList<AuditElement>();
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    
    ChangeTableModel() {
    }
    
    ChangeTableModel(List<AuditElement> changes) {
        this.changes.addAll(changes);
    }
    
    @Override
    public int getRowCount() {
        return changes.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return Bundle.LBL_ChangeTableModel_When();
            case 1:
                return Bundle.LBL_ChangeTableModel_Who();
            case 2:
                return Bundle.LBL_ChangeTableModel_Type();
            case 3:
                return Bundle.LBL_ChangeTableModel_Change();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case 0:
                return Date.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        AuditElement change = changes.get(row);
        switch(column) {
            case 0:
                return change.getDate();
            case 1:
                return change.getUser();
            case 2:
                return change.getType();
            case 3:
                return change.getChange();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    void setChanges(List<AuditElement> changes) {
        this.changes.clear();
        this.changes.addAll(changes);
        fireChange();
    }
    
    private void fireChange() {
        TableModelEvent evt = new TableModelEvent(this);
        for(TableModelListener l : new ArrayList<TableModelListener>(listeners))
            l.tableChanged(evt);
    }
}
