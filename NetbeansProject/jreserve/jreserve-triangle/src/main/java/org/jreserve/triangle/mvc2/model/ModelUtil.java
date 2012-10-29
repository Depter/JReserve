package org.jreserve.triangle.mvc2.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ModelUtil {
    
    public static List<Date> accidentBounds(TriangleGeometry geometry) {
        Date start = geometry.getAccidentStart();
        int periods = geometry.getAccidentPeriods();
        int months = geometry.getMonthInAccident();
        return createBounds(start, periods, months);
    }
    
    private static List<Date> createBounds(Date start, int periods, int months) {
        List<Date> bounds = new ArrayList<Date>(periods+1);
        bounds.add(start);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        for(int p=1; p<=periods; p++) {
            calendar.add(Calendar.MONTH, months);
            bounds.add(calendar.getTime());
        }
        return bounds;
    }

    public static List<Date> developmentBounds(TriangleGeometry geometry) {
        Date start = geometry.getDevelopmentStart();
        int periods = geometry.getDevelopmentPeriods();
        int months = geometry.getMonthInDevelopment();
        return createBounds(start, periods, months);
    }
    
    private ModelUtil() {}
}
