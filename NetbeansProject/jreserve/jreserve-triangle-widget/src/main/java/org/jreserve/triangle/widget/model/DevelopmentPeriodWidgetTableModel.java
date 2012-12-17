package org.jreserve.triangle.widget.model;

import org.jreserve.triangle.TriangularData;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DevelopmentPeriodWidgetTableModel.Name=Development periods"
})
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
    protected int getAccident(int row, int column) {
        return row;
    }

    @Override
    protected int getDevelopment(int row, int column) {
        return column;
    }
}
