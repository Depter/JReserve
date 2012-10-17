package org.jreserve.triangle.mvc.controller;

import org.jreserve.triangle.mvc.model.AbstractCell;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DevelopmentTriangleTableModel extends AbstractTriangleTableModel {

    public final static TriangleTableModelFactory FACTORY = new TriangleTableModelFactory() {
        @Override
        public TriangleTableModel createModel(TriangleTable table) {
            return new DevelopmentTriangleTableModel(table);
        }
    };
    
    public DevelopmentTriangleTableModel(TriangleTable table) {
        super(table);
    }

    @Override
    public AbstractCell getCellAt(int row, int column) {
        return table.getCell(row, column);
    }

    @Override
    public Object getRowTitle(int row) {
        return accidentDates.get(row);
    }

    @Override
    public Object getColumnTitle(int column) {
        return Integer.valueOf(column+1);
    }
}
