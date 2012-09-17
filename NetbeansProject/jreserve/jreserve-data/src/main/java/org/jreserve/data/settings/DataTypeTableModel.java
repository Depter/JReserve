package org.jreserve.data.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataTypeTableModel.id=Id",
    "LBL.DataTypeTableModel.name=Name",
    "LBL.DataTypeTableModel.istriangle=Triangle"
})
class DataTypeTableModel extends DefaultTableModel {

    private final static int COLUMN_COUNT = 3;
    private final static int ID_COLUMN = 0;
    private final static int NAME_COLUMN = 1;
    private final static int TRIANGLE_COLUMN = 2;
    
    private List<DTDummy> dummies = new ArrayList<DTDummy>();
    
    DataTypeTableModel() {
    }

    void load() {
        for(DataType dt : DataTypeUtil.getDataTypes())
            dummies.add(new DTDummy(dt));
        super.fireTableDataChanged();
    }
    
    @Override
    public int getColumnCount() {
        return COLUMN_COUNT; 
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case ID_COLUMN:
                return Bundle.LBL_DataTypeTableModel_id();
            case NAME_COLUMN:
                return Bundle.LBL_DataTypeTableModel_name();
            case TRIANGLE_COLUMN:
                return Bundle.LBL_DataTypeTableModel_istriangle();
            default: 
                throw new IllegalArgumentException("Unknown column id: "+column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case ID_COLUMN:
                return Integer.class;
            case NAME_COLUMN:
                return String.class;
            case TRIANGLE_COLUMN:
                return Boolean.class;
            default: 
                throw new IllegalArgumentException("Unknown column id: "+column);
        }
    }

    @Override
    public int getRowCount() {
        return dummies == null? 0 : dummies.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        DTDummy dummy = dummies.get(row);
        switch(column) {
            case ID_COLUMN:
                return dummy.id;
            case NAME_COLUMN:
                return dummy.name;
            case TRIANGLE_COLUMN:
                return dummy.isTriangle;
            default: 
                throw new IllegalArgumentException("Unknown column id: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != ID_COLUMN;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if(column == ID_COLUMN)
            throw new IllegalArgumentException("Id column is not editable!");
        DTDummy dummy = dummies.get(row);
        switch(column) {
            case NAME_COLUMN:
                dummy.name = (String) value;
                return;
            case TRIANGLE_COLUMN:
                Boolean isTriangle = (Boolean) value;
                dummy.isTriangle = isTriangle==null? false : isTriangle;
                return;
            default: 
                throw new IllegalArgumentException("Column not editable: "+column);
        }
    }

    @Override
    public void addRow(Object[] row) {
        DTDummy dummy = new DTDummy(row);
        int index = -1 * Collections.binarySearch(dummies, dummy) - 1;
        dummies.add(index, dummy);
        super.fireTableRowsInserted(index, index);
    }
    
    DTDummy getDummy(int id) {
        for(DTDummy dummy : dummies) 
            if(dummy.id == id)
                return dummy;
        return null;
    }
    
    class DTDummy implements Comparable<DTDummy> {
        private int id;
        private String name;
        private boolean isTriangle;
        
        private DTDummy() {
        }
        
        
        private DTDummy(DataType dt) {
            this.id = dt.getDbId();
            this.name = dt.getName();
            this.isTriangle = dt.isTriangle();
        }
        
        private DTDummy(Object[] row) {
            this.id = (Integer) row[ID_COLUMN];
            this.name = (String) row[NAME_COLUMN];
            this.isTriangle = (Boolean) row[TRIANGLE_COLUMN];
        }
        
        @Override
        public int compareTo(DTDummy o) {
            if(o == null)
                return -1;
            return id - o.id;
        }
    }
    
}
