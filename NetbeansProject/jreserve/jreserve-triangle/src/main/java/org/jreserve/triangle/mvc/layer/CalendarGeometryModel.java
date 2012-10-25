package org.jreserve.triangle.mvc.layer;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CalendarGeometryModel extends AbstractGeometryModel {

    @Override
    public Object getColumnTitle(int column) {
        if(column == 0)
            return null;
        return super.getDevelopmentBegin(column);
    }

    @Override
    public Class getColumnTitleClass() {
        return Date.class;
    }

    @Override
    public boolean hasCellAt(int row, int column) {
        Date accidentBegin = super.getAccidentBegin(row);
        Date developmentBegin = super.getDevelopmentBegin(column);
        return !developmentBegin.before(accidentBegin);
    }
}
