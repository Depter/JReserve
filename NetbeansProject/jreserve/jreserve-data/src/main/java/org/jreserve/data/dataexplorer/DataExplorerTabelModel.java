package org.jreserve.data.dataexplorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.jreserve.data.Criteria;
import org.jreserve.data.Data;
import org.jreserve.data.DataSource;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.project.entities.Project;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DataExplorerTabelModel.Accident=Accident",
    "LBL.DataExplorerTabelModel.Development=Development",
    "LBL.DataExplorerTabelModel.Value=Value",
    "LBL.DataExplorerTabelModel.RowNumber=Row"
})
class DataExplorerTabelModel extends DefaultTableModel {
    
    private final static Logger logger = Logger.getLogger(DataExplorerTabelModel.class.getName());
    
    private Project project;
    private ProjectDataType dataType;
    private List<Data> allData = new ArrayList<Data>();
    private int pageIndex;
    private int rowPerPage;

    DataExplorerTabelModel(Project project, int rowPerPage) {
        this.project = project;
        this.rowPerPage = rowPerPage;
    }
    
    void setDataType(ProjectDataType dataType) {
        pageIndex = 0;
        allData.clear();
        this.dataType = dataType;
        if(dataType != null)
            loadAllData();
        fireTableStructureChanged();
    }
    
    private void loadAllData() {
        DataSource ds = new DataSource();
        try {
            ds.open();
            allData.addAll(ds.getData(getCriteria()));
            Collections.sort(allData);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, String.format("Unable to load data for %s/%s", project, dataType), ex);
            Exceptions.printStackTrace(ex);
        } finally {
            ds.rollBack();
        }
    }
    
    private Criteria getCriteria() {
        return new Criteria(project).setDataType(dataType);
    }
    
    int getTotalRowCount() {
        if(allData == null)
            return 0;
        return allData.size();
    }
    
    void setPageIndex(int index) {
        if(index < 0) {
            this.pageIndex = 0;
        } else {
            int pageCount = getPageCount();
            this.pageIndex = (index >= pageCount)? pageCount-1 : index;
        }
        fireTableDataChanged();
    }
    
    int getPageIndex() {
        return pageIndex;
    }
    
    int getPageCount() {
        int size = allData.size();
        int count = size / rowPerPage;
        if(count * rowPerPage < size)
            count++;
        return count;
    }
    
    void setRowsPerPage(int rowPerPage) {
        this.rowPerPage = (rowPerPage<1)? 1 : rowPerPage;
        fireTableDataChanged();
    }
    
    @Override
    public int getColumnCount() {
        if(dataType == null)
            return 0;
        return dataType.isTriangle()? 4 : 3;
    }

    @Override
    public String getColumnName(int column) {
        if(dataType == null)
            return null;
        switch(column) {
            case 0:
                return Bundle.LBL_DataExplorerTabelModel_RowNumber();
            case 1:
                return Bundle.LBL_DataExplorerTabelModel_Accident();
            case 2:
                return dataType.isTriangle()?
                        Bundle.LBL_DataExplorerTabelModel_Development() :
                        Bundle.LBL_DataExplorerTabelModel_Value();
            case 3:
                return Bundle.LBL_DataExplorerTabelModel_Value();
            default:
                throw new IllegalArgumentException("Unknown column id: "+column);
        }
    }

    @Override
    public int getRowCount() {
        if(allData == null)
            return 0;
        int begin = pageIndex * rowPerPage;
        int last = allData.size();
        int length = last - begin;
        return length <= rowPerPage? length : rowPerPage;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Data data = getData(row);
        switch(column) {
            case 0:
                return pageIndex * rowPerPage + row + 1;
            case 1:
                return data.getAccidentDate();
            case 2:
                return dataType.isTriangle()?
                        data.getDevelopmentDate() :
                        data.getValue();
            case 3:
                return data.getValue();
            default:
                throw new IllegalArgumentException("Unknown column id: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(dataType == null)
            return Object.class;
        switch(column) {
            case 0:
                return Integer.class;
            case 1:
                return Date.class;
            case 2:
                return dataType.isTriangle()?
                        Date.class :
                        Double.class;
            case 3:
                return Double.class;
            default:
                throw new IllegalArgumentException("Unknown column id: "+column);
        }
    }
    
    List<Data> getAllDataOnPage() {
        List<Data> result = new ArrayList<Data>();
        for(int i=0, rowCount=getRowCount(); i<rowCount; i++)
            result.add(getData(i));
        return result;
    }
    
    private Data getData(int row) {
        return allData.get(pageIndex * rowPerPage + row);
    }
    
    List<Data> getSelectedData(int[] rows) {
        List<Data> result = new ArrayList<Data>();
        for(int i=0, length=rows.length; i<length; i++)
            result.add(getData(rows[i]));
        return result;
    }
}
