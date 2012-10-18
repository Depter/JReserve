package org.jreserve.triangle.mvc.controller;

import java.util.Date;
import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CalendarTriangleTableModel<V> extends AbstractTriangleTableModel<V> {

    public final static TriangleTableModelFactory FACTORY = new TriangleTableModelFactory() {
        @Override
        public <V> TriangleTableModel<V> createModel(TriangleTable<V> table) {
            return new CalendarTriangleTableModel<V>(table);
        }
    };
    
    public CalendarTriangleTableModel(TriangleTable<V> table) {
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
    public TriangleCell<V> getCellAt(int row, int column) {
        Date accident = accidentDates.get(row);
        Date development = developmentDates.get(column);
        return table.getCell(accident, development);
    }
}
