package org.jreserve.triangle.mvc.controller;

import java.util.Date;
import org.jreserve.triangle.mvc.model.AbstractCell;
import org.jreserve.triangle.mvc.model.TriangleRow;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CalendarTriangleTableModel extends AbstractTriangleTableModel {

    public final static TriangleTableModelFactory FACTORY = new TriangleTableModelFactory() {
        @Override
        public TriangleTableModel createModel(TriangleTable table) {
            return new CalendarTriangleTableModel(table);
        }
    };
    
    public CalendarTriangleTableModel(TriangleTable table) {
        super(table);
    }
    
    @Override
    public Object getRowTitle(int row) {
        return accidentDates.get(row);
    }

    @Override
    public Object getColumnTitle(int column) {
        return developmentDates.get(column);
    }

    @Override
    public AbstractCell getCellAt(int row, int column) {
        TriangleRow r = table.getRow(row);
        if(r == null)
            return null;
        return getCellAt(r, developmentDates.get(column));
    }

    private AbstractCell getCellAt(TriangleRow row, Date date) {
        for(int c=0, cCount=row.getCellCount(); c<cCount; c++) {
            AbstractCell cell = row.getCell(c);
            if(date.equals(cell.getDevelopmentBegin()))
                return cell;
        }
        return null;
    }
}
