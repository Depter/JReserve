package org.jreserve.triangle.guiutil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorFormatVisualPanel extends TriangleFormatVisualPanel {

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void componentsInitialized() {
        geometrySetting.setSymmetricPeriods(false);
        geometrySetting.setSymmetricMonths(false);
        geometrySetting.setSymmetricEnabled(false);
        geometrySetting.setDevelopmentPeriodCount(1);
        setDevelopmentMonthsInStep();
        geometrySetting.addPropertyChangeListener(new MonthCountAdapter());
    }

    private void setDevelopmentMonthsInStep() {
        int months = getDevelopmentMonthCount();
        geometrySetting.setDevelopmentMonthsPerStep(months);
    }
    
    private int getDevelopmentMonthCount() {
        if(geometry == null)
            return 0;
        int end = getDevelopmentEnd();
        int from = getDevelopmentStart();
        return getDifference(from, end);
    }

    private int getDevelopmentEnd() {
        Date start = geometry.getAccidentStart();
        if(start == null)
            return -1;
        int periods = geometry.getAccidentPeriods();
        int months = geometry.getMonthInAccident();
        return getYearMonth(start) + periods * months;
    }

    private int getYearMonth(Date date) {
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return year * 12 + month;
    }
    
    private int getDevelopmentStart() {
        Date start = geometry.getDevelopmentStart();
        if(start == null)
            return -1;
        return getYearMonth(start);
    }

    private int getDifference(int from, int end) {
        if(from < 0 || end < 0 || end < from)
            return 0;
        return end - from;
    }
    
    private class MonthCountAdapter implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setDevelopmentMonthsInStep();
        }
    }
}
