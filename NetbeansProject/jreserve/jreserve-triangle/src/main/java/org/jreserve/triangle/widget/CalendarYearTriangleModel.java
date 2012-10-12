package org.jreserve.triangle.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jreserve.data.model.DataCell;
import org.jreserve.data.model.DataRow;
import org.jreserve.data.model.DataTable;
import org.jreserve.localesettings.util.DateRenderer;

/**
 *
 * @author Peter Decsi
 */
class CalendarYearTriangleModel extends AbstractTriangleModel {

    private List<Date> developmentPeriods = new ArrayList<Date>();
    private DateRenderer dateRenderer = new DateRenderer();
    
    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Bundle.LBL_ImportTableModel_Periods();
        return getDateColumnName(column);
    }
    
    private String getDateColumnName(int column) {
        Date date = getDevelopmentDate(column);
        return dateRenderer.toString(date);
    }

    private Date getDevelopmentDate(int column) {
        return developmentPeriods.get(column-1);
    }
    
    @Override
    protected DataCell getCellAt(DataRow row, int column) {
        Date date = getDevelopmentDate(column);
        for(int i=0, count=row.getCellCount(); i<count; i++) {
            DataCell cell = row.getCell(i);
            if(date.equals(cell.getDevelopmentBegin()))
                return cell;
        }
        return null;
    }

    @Override
    public void setDataTable(DataTable table) {
        super.setDataTable(table);
        setDevelopmentPeriods();
        Collections.sort(developmentPeriods);
    }
    
    private void setDevelopmentPeriods() {
        developmentPeriods.clear();
        if(table == null)
            return;
        for(int i=0, count=table.getColumnCount(); i<count; i++)
            setDevelopmentPeriods(table.getRow(i));
    }
    
    private void setDevelopmentPeriods(DataRow row) {
        for(int i=0, count=row.getCellCount(); i<count; i++) {
            Date date = row.getCell(i).getDevelopmentBegin();
            if(!developmentPeriods.contains(date))
                developmentPeriods.add(date);
        }
    }
}
