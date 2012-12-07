package org.jreserve.triangle.widget.model;

import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DevelopmentPeriodWidgetTableModel extends AbstractWidgetTableModel {
    
    public DevelopmentPeriodWidgetTableModel() {
    }
    
    public DevelopmentPeriodWidgetTableModel(TriangularData data) {
        super(data);
    }

    @Override
    protected String getRowName(int row) {
        return data.getAccidentName(row).toString();
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return "";
        return Integer.toString(column);
    }

    @Override
    protected Double getData(int row, int column) {
        int devCount = data.getDevelopmentCount(row);
        if(column >= devCount)
            return null;
        return data.getValue(row, column);
    }
}
