package org.jreserve.data.importdialog.clipboardtable;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jreserve.data.entities.ProjectDataType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataTableModel.Accident=Accident",
    "LBL.DataTableModel.Development=Development",
    "LBL.DataTableModel.Value=Value"
})
class DataTableModel extends DefaultTableModel {

    private final static String COLUMN_SEPARATOR = "\\t";
    
    private ProjectDataType dataType;
    private List<DataDummy> dummies = new ArrayList<DataDummy>();
    
    void rerenderData() {
        fireTableDataChanged();
    }
    
    void setDataType(ProjectDataType dataType) {
        this.dataType = dataType;
        dummies.clear();
        fireTableStructureChanged();
        fireTableDataChanged();
    }
    
    @Override
    public int getColumnCount() {
        if(dataType == null)
            return 0;
        return dataType.isTriangle()? 3 : 2;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return Bundle.LBL_DataTableModel_Accident();
            case 1:
                return dataType.isTriangle()?
                       Bundle.LBL_DataTableModel_Development() :
                       dataType.getName();
            case 2:
                return dataType.getName();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public int getRowCount() {
        if(dataType == null || dummies == null)
            return 0;
        return dummies.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        DataDummy dummy = dummies.get(row);
        switch(column) {
            case 0:
                return dummy.accident;
            case 1:
                return dataType.isTriangle()?
                       dummy.development :
                       dummy.value;
            case 2:
                return dummy.value;
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }
    
    void setClipboardData(String data) {
        clearDummies();
        readDummies(data);
        fireTableRowsInserted(0, getRowCount()-1);
    }
    
    private void clearDummies() {
        int size = getRowCount();
        dummies.clear();
        if(size > 0)
            fireTableRowsDeleted(0, size-1);
    }
    
    private void readDummies(String data) {
        String[] lines = data.split("\\n");
        for(String line : lines)
            readDummy(line);
    }
    
    private void readDummy(String line) {
        if(line == null || line.trim().length()==0)
            return;
        DataDummy dummy = createDummy(line);
        dummies.add(dummy);
    }
    
    private DataDummy createDummy(String line) {
        String[] columns = line.split(COLUMN_SEPARATOR);
        DataDummy dummy = new DataDummy();
        dummy.initialize(columns);
        return dummy;
    }
    
    private static class DataDummy implements Comparable<DataDummy>{
        private String accident;
        private String development;
        private String value;
        
        private void initialize(String[] columns) {
            if(columns.length < 3) {
                initAsVector(columns);
            } else {
                initAsTriangle(columns);
            }
        }
        
        private void initAsVector(String[] columns) {
            accident = columns[0];
            development = columns[0];
            value = columns[1];
        }
        
        private void initAsTriangle(String[] columns) {
            accident = columns[0];
            development = columns[1];
            value = columns[2];
        }
        
        @Override
        public boolean equals(Object o) {
            if(o instanceof DataDummy)
                return compareTo((DataDummy)o) == 0;
            return false;
        }
        
        @Override
        public int compareTo(DataDummy o) {
            int dif = accident.compareTo(o.accident);
            return dif!=0? dif : development.compareTo(o.development);
        }
        
        @Override
        public int hashCode() {
            int hash = 31 + accident.hashCode();
            return 17 * hash + development.hashCode();
        }
    }
}
