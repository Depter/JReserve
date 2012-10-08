package org.jreserve.triangle.importutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jreserve.data.Data;
import org.jreserve.data.model.DataRow;
import org.jreserve.data.model.DataTable;
import org.jreserve.data.model.DataTableFactory;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportTableModel.Periods=Periods"
})
public class ImportTableModel extends DefaultTableModel {

    private List<Data> datas = new ArrayList<Data>();
    private TriangleGeometry geometry;
    
    private DataTable table;

    @Override
    public int getColumnCount() {
        return table==null? 0 : table.getColumnCount()+1;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_ImportTableModel_Periods();
        return Integer.toString(column);
    }

    @Override
    public int getRowCount() {
        return table==null? 0 : table.getRowCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        DataRow dataRow = table.getRow(row);
        if(column == 0)
            return dataRow.getAccidentBegin();
        return dataRow.getCell(column-1).getValue();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return Date.class;
        return Double.class;
    }
    
    public void setDatas(List<Data> datas) {
        this.datas.clear();
        if(datas != null)
            this.datas.addAll(datas);
        rebuildTable();
    }
    
    public void setGeometry(TriangleGeometry geometry) {
        this.geometry = geometry;
        rebuildTable();
    }
    
    private void rebuildTable() {
        clearTable();
        createTable();
        fireTableStructureChanged();
        fireRowsInserted();
    }
    
    private void clearTable() {
        int rows = getRowCount();
        table = null;
        if(rows > 0)
            fireTableRowsDeleted(0, rows-1);
    }
    
    private void createTable() {
        if(geometry == null) {
            table = null;
        } else {
            DataTableFactory factory = new DataTableFactory(geometry);
            table = factory.buildTable();
            factory.setValues(datas);
        }
    }
    
    private void fireRowsInserted() {
        int rows = getRowCount();
        if(rows > 0)
            fireTableRowsInserted(0, rows-1);
    }
}
