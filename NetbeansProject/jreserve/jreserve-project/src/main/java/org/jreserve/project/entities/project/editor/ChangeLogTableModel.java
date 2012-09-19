package org.jreserve.project.entities.project.editor;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ChangeLogListener;
import org.jreserve.project.entities.ChangeLogUtil;
import org.jreserve.project.entities.Project;
import org.jreserve.project.entities.project.ProjectElement;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.ChangeLogTableModel.creationDate=Created",
    "LBL.ChangeLogTableModel.userName=User",
    "LBL.ChangeLogTableModel.type=Type",
    "LBL.ChangeLogTableModel.message=Message"
})
class ChangeLogTableModel extends AbstractTableModel implements ChangeLogListener {
    
    private final static int DATE_COLUMN = 0;
    private final static int USER_COLUMN = 1;
    private final static int TYPE_COLUMN = 2;
    private final static int MESSAGE_COLUMN = 3;
    final static int COLUMN_COUNT = 4;
    
    private ProjectElement element;
    private List<ChangeLog> changes = new ArrayList<ChangeLog>();
    
    ChangeLogTableModel(ProjectElement element) {
        this.element = element;
        loadChanges();
    }
    
    private void loadChanges() {
        Project project = element.getValue();
        ChangeLogUtil util = ChangeLogUtil.getDefault();
        changes.addAll(util.getValues(project));
        util.addChangeLogListener(project, this);
    }
    
    @Override
    public int getRowCount() {
        return changes.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ChangeLog log = changes.get(rowIndex);
        switch(columnIndex) {
            case DATE_COLUMN: return log.getCreationTime();
            case USER_COLUMN: return log.getUserName();
            case TYPE_COLUMN: return log.getType();
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
            case TYPE_COLUMN:
                return Bundle.LBL_ChangeLogTableModel_type();
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
    public void changeLogAdded(ChangeLog change) {
        changes.add(change);
        int row = changes.size() - 1;
        fireTableRowsInserted(row, row);
    }
}
