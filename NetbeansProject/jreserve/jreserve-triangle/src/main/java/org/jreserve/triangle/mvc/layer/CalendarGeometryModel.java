package org.jreserve.triangle.mvc.layer;

import java.util.Date;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.GeometryModel.FirstColumnTitle=Periods"
})
public class CalendarGeometryModel extends AbstractGeometryModel {

    @Override
    public Object getColumnTitle(int column) {
        if(column == 0)
            return Bundle.LBL_GeometryModel_FirstColumnTitle();
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
    
    @Override
    public LayerCriteria createCriteria(int row, int column) {
        Date developmentBegin = geometry.getDevelopmentStart();
        Date developmentFrom = getDevelopmentBegin(developmentBegin, isCummulated()? 1 : column);
        Date developmentEnd = getDevelopmentBegin(developmentBegin, column+1);
        return new LayerCriteria()
                .setAccidentFrom(getAccidentBegin(row)).setAccidentEnd(getAccidentBegin(row+1))
                .setDevelopmentFrom(developmentFrom).setDevelopmentEnd(developmentEnd);
    }
}
