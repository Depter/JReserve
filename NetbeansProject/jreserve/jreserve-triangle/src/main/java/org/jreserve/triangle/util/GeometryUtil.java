package org.jreserve.triangle.util;

import java.util.Calendar;
import java.util.Date;
import javax.swing.SwingUtilities;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.VectorGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GeometryUtil {

    private static GeometryUtil INSTANCE;
    
    public static GeometryUtil getEDTInstance() {
        if(!SwingUtilities.isEventDispatchThread())
            throw new IllegalStateException("Not called form EDT thread! thread: "+Thread.currentThread().getName());
        if(INSTANCE == null)
            INSTANCE = new GeometryUtil();
        return INSTANCE;
    }
    
    private final Calendar calendar = Calendar.getInstance();
    
    public Date getAccidentBegin(VectorGeometry geometry, int period) {
        Date start = geometry.getStartDate();
        if(period == 0)
            return start;
        return stepMonths(start, period, geometry.getAccidentMonths());
    }
    
    private Date stepMonths(Date start, int steps, int months) {
        calendar.setTime(start);
        calendar.add(Calendar.MONTH, steps * months);
        return calendar.getTime();
    }

    public Date getAccidentEnd(VectorGeometry geometry, int period) {
        Date start = geometry.getStartDate();
        if(++period == 0)
            return start;
        return stepMonths(start, period, geometry.getAccidentMonths());
    }

    public Date getDevelopmentBegin(TriangleGeometry geometry, int accident, int development) {
        Date start = getAccidentBegin(geometry, accident);
        if(development == 0)
            return start;
        return stepMonths(start, development, geometry.getMonthInDevelopment());
    }

    public Date getDevelopmentEnd(TriangleGeometry geometry, int accident, int development) {
        Date start = getAccidentBegin(geometry, accident);
        if(++development == 0)
            return start;
        return stepMonths(start, development, geometry.getMonthInDevelopment());
    }
}