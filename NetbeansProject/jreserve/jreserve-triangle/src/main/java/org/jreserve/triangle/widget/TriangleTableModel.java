package org.jreserve.triangle.widget;

import java.util.Date;
import javax.swing.table.DefaultTableModel;
import org.jreserve.triangle.mvc.controller.CalendarTriangleTableModel;
import org.jreserve.triangle.mvc.controller.DevelopmentTriangleTableModel;
import org.jreserve.triangle.mvc.controller.LayeredTableModel;
import org.jreserve.triangle.mvc.model.AbstractCell;
import org.jreserve.triangle.mvc.model.TriangleTable;
import org.jreserve.triangle.mvc.model.ValueCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportTableModel.Periods=Periods"
})
class TriangleTableModel extends DefaultTableModel {

    static enum ModelType {
        DEVELOPMENT,
        CALENDAR
    };
    
    private LayeredTableModel triangleModel = new LayeredTableModel(DevelopmentTriangleTableModel.FACTORY);
    private ModelType type = ModelType.DEVELOPMENT;
    
    @Override
    public int getColumnCount() {
        if(triangleModel == null)
            return 0;
        return triangleModel.getColumnCount()+1;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_ImportTableModel_Periods();
        return ""+triangleModel.getColumnTitle(column-1);
    }

    @Override
    public int getRowCount() {
        if(triangleModel == null)
            return 0;
        return triangleModel.getRowCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return triangleModel.getRowTitle(row);
        AbstractCell cell = triangleModel.getCellAt(row, column-1);
        if(cell instanceof ValueCell)
            return ((ValueCell) cell).getValue();
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
        //return triangleModel.isCellEditable(row, column);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return Date.class;
        return Double.class;
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
    
    private void rebuildTable() {
        clearTable();
        createTable();
        fireTableStructureChanged();
    }
    
    private void clearTable() {
        int rows = getRowCount();
        if(rows > 0)
            fireTableRowsDeleted(0, rows-1);
    }
    
    private void createTable() {
        switch(type) {
            case CALENDAR:
                triangleModel.setModelType(CalendarTriangleTableModel.FACTORY);
                break;
            case DEVELOPMENT:
                triangleModel.setModelType(DevelopmentTriangleTableModel.FACTORY);
                break;
            default:
                throw new IllegalStateException("Unknown model type: "+type);
        }
    }
    
    public boolean hasValueAt(int row, int column) {
        if(triangleModel == null)
            return false;
        return triangleModel.hasValueAt(row, column);
    }
    
    public void addTriangleTable(TriangleTable table) {
        clearTable();
        triangleModel.addTable(table);
        fireTableStructureChanged();
    }
}
