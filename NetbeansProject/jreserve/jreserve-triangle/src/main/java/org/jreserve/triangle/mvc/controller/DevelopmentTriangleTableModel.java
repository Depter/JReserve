package org.jreserve.triangle.mvc.controller;

import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DevelopmentTriangleTableModel<V> extends AbstractTriangleTableModel<V> {

    public final static TriangleTableModelFactory FACTORY = new TriangleTableModelFactory() {
        @Override
        public <V> TriangleTableModel<V> createModel(TriangleTable<V> table) {
            return new DevelopmentTriangleTableModel<V>(table);
        }
    };
    
    public DevelopmentTriangleTableModel(TriangleTable<V> table) {
        super(table);
    }

    @Override
    public Object getRowTitle(int row) {
        return accidentDates.get(row);
    }

    @Override
    public TriangleCell<V> getCellAt(int row, int column) {
        return table.getCell(row, column);
    }
}
