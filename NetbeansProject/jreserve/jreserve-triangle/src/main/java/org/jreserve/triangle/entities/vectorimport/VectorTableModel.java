package org.jreserve.triangle.entities.vectorimport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.jreserve.data.Data;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.VectorTableModel.Period=Period",
    "LBL.VectorTableModel.Value=Value"
})
public class VectorTableModel extends DefaultTableModel {

    private final static int COLUMN_COUNT = 2;
    private final static int PERIOD_COLUMN = 0;
    private final static int VALUE_COLUMN = 1;
    
    private List<Data> originalDatas = new ArrayList<Data>();
    private Date beginDate;
    private Date endDate;
    private int monthSteps;
    
    private List<Data> datas = new ArrayList<Data>();
    
    @Override
    public int getColumnCount() {
        return COLUMN_COUNT; 
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case PERIOD_COLUMN:
                return Bundle.LBL_VectorTableModel_Period();
            case VALUE_COLUMN:
                return Bundle.LBL_VectorTableModel_Value();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public int getRowCount() {
        if(datas == null)
            return 0;
        return datas.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Data data = datas.get(row);
        switch(column) {
            case PERIOD_COLUMN:
                return data.getAccidentDate();
            case VALUE_COLUMN:
                return data.getValue();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case PERIOD_COLUMN:
                return Date.class;
            case VALUE_COLUMN:
                return Double.class;
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }
    
    private void filterData() {
        datas.clear();
        if(!checkInput())
            return;
    }
    
    private boolean checkInput() {
        if(beginDate==null || endDate==null || endDate.before(beginDate))
            return false;
        return true;
    }
    
    private void createCells() {
        
    }
}
