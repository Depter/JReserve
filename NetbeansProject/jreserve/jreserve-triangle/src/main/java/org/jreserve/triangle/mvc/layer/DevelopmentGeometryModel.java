package org.jreserve.triangle.mvc.layer;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DevelopmentGeometryModel extends AbstractGeometryModel {

    @Override
    public Object getColumnTitle(int column) {
        if(column == 0)
            return null;
        return column;
    }

    @Override
    public Class getColumnTitleClass() {
        return Integer.class;
    }

    @Override
    public boolean hasCellAt(int row, int column) {
        Date tableEnd = super.getDevelopmentEnd();
        Date start = super.getAccidentBegin(row);
        Date cellBegin = super.getDevelopmentBegin(start, column);
        return cellBegin.before(tableEnd);
    }
}
