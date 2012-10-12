package org.jreserve.triangle.widget;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jreserve.data.Data;
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
class ImportTableModel extends DefaultTableModel {

    static enum ModelType {
        DEVELOPMENT,
        CALENDAR
    };
    
    private List<Data> datas = new ArrayList<Data>();
    private TriangleGeometry geometry;
    private DataTable table;
    
    private boolean cummulated = false;
    
    private TriangleModel triangleModel = new DevelopmentPeriodTriangleModel();
    private ModelType type = ModelType.DEVELOPMENT;
    
    @Override
    public int getColumnCount() {
        return triangleModel==null? 0 : triangleModel.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return triangleModel.getColumnName(column);
    }

    @Override
    public int getRowCount() {
        return triangleModel==null? 0 : triangleModel.getRowCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return triangleModel.getValueAt(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return triangleModel.isCellEditable(row, column);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return triangleModel.getColumnClass(column);
    }
    
    public List<Data> getDatas() {
        return new ArrayList<Data>(datas);
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
        initModel();
    }
    
    private void initModel() {
        switch(type) {
            case DEVELOPMENT:
                triangleModel = new DevelopmentPeriodTriangleModel();
                break;
            case CALENDAR:
                triangleModel = new CalendarYearTriangleModel();
                break;
            default:
                throw new IllegalStateException("Unknown ModelType: "+type);
        }
        triangleModel.setDataTable(table);
    }
    
    private void fireRowsInserted() {
        int rows = getRowCount();
        if(rows > 0)
            fireTableRowsInserted(0, rows-1);
    }
    
    public DataTable getTable() {
        return table;
    }
    
    public boolean isCummulated() {
        return cummulated;
    }
    
    public void setCummulated(boolean cummulated) {
        this.cummulated = cummulated;
        triangleModel.setCummulated(cummulated);
        super.fireTableDataChanged();
    }
    
    public ModelType getModelType() {
        return type;
    }
    
    public void setModelType(ModelType type) {
        if(type == null)
            type = ModelType.DEVELOPMENT;
        if(this.type != type) {
            this.type = type;
            rebuildTable();
        }
    }
    
    public boolean hasValueAt(int row, int column) {
        if(triangleModel == null)
            return false;
        return triangleModel.hasValueAt(row, column);
    }
}
