package org.jreserve.dataimport.clipboardtable;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jreserve.data.ProjectDataType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataTableModel.Row=Row",
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
        fireTableStructureChanged();
    }

    ProjectDataType getDataType() {
        return dataType;
    }
    
    @Override
    public int getColumnCount() {
        if(dataType == null)
            return 0;
        return dataType.isTriangle()? 4 : 3;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return Bundle.LBL_DataTableModel_Row();
            case 1:
                return Bundle.LBL_DataTableModel_Accident();
            case 2:
                return dataType.isTriangle()?
                       Bundle.LBL_DataTableModel_Development() :
                       dataType.getName();
            case 3:
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
                return row+1;
            case 1:
                return dummy.getAccident();
            case 2:
                return dataType.isTriangle()?
                       dummy.getDevelopment() :
                       dummy.getValue();
            case 3:
                return dummy.getValue();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    public void setValueAt(Object vale, int row, int column) {
        DataDummy dummy = dummies.get(row);
        String str = (String) vale;
        switch(column) {
            case 1:
                dummy.setAccident(str);
                break;
            case 2:
                if(dataType.isTriangle())
                       dummy.setDevelopment(str);
                else
                       dummy.setValue(str);
                break;
            case 3:
                dummy.setValue(str);
                break;
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return column==0? Integer.class : String.class;
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
        initDummy(dummy, columns);
        return dummy;
    }
        
    private void initDummy(DataDummy dummy, String[] columns) {
        if(columns.length < 3)
            initAsVector(dummy, columns);
        else
            initAsTriangle(dummy, columns);
    }

    private void initAsVector(DataDummy dummy, String[] columns) {
        dummy.setAccident(columns[0]);
        dummy.setDevelopment(columns[0]);
        dummy.setValue(columns[1]);
    }

    private void initAsTriangle(DataDummy dummy, String[] columns) {
        dummy.setAccident(columns[0]);
        dummy.setDevelopment(columns[1]);
        dummy.setValue(columns[2]);
    }
    
    List<DataDummy> getDummies() {
        return new ArrayList<DataDummy>(dummies);
    }
}
