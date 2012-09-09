package org.jreserve.project.entities.project.editor;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.project.ProjectElement;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ChangeLogTableModel.creationDate=Created",
    "LBL.ChangeLogTableModel.userName=User",
    "LBL.ChangeLogTableModel.message=Message"
})
class ChangeLogTableModel extends AbstractTableModel implements LookupListener {
    
    private final static int DATE_COLUMN = 0;
    private final static int USER_COLUMN = 1;
    private final static int MESSAGE_COLUMN = 2;
    
    private final static Comparator<ChangeLog> CHANGE_COMPARATOR = new Comparator<ChangeLog>() {
        @Override
        public int compare(ChangeLog o1, ChangeLog o2) {
            Date d1 = o1.getCreationTime();
            Date d2 = o2.getCreationTime();
            return compare(d1, d2);
        }
        
        private int compare(Date d1, Date d2) {
            if(d1.before(d2))
                return -1;
            return d1.after(d2)? 1 : 0;
        }
    };
    
    private ProjectElement element;
    private Result<ChangeLog> changeResult;
    private List<ChangeLog> changes = new ArrayList<ChangeLog>();
    
    ChangeLogTableModel(ProjectElement element) {
        this.element = element;
        changeResult = element.getLookup().lookupResult(ChangeLog.class);
        changeResult.addLookupListener(this);
        loadChanges();
    }
    
    @Override
    public int getRowCount() {
        return changes.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ChangeLog log = changes.get(rowIndex);
        switch(columnIndex) {
            case DATE_COLUMN: return log.getCreationTime();
            case USER_COLUMN: return log.getUserName();
            case MESSAGE_COLUMN: return log.getLogMessage();
            default:
                throw new IllegalArgumentException("Column index out of bounds [0,2]! "+columnIndex);
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case DATE_COLUMN: 
                return Bundle.LBL_ChangeLogTableModel_creationDate();
            case USER_COLUMN: 
                return Bundle.LBL_ChangeLogTableModel_userName();
            case MESSAGE_COLUMN: 
                return Bundle.LBL_ChangeLogTableModel_message();
            default:
                throw new IllegalArgumentException("Column index out of bounds [0,2]! "+columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public void resultChanged(LookupEvent le) {
        loadChanges();
        super.fireTableDataChanged();
    }
    
    private void loadChanges() {
        changes.clear();
        changes.addAll(element.getLookup().lookupAll(ChangeLog.class));
        Collections.sort(changes, CHANGE_COMPARATOR);
    }
}
