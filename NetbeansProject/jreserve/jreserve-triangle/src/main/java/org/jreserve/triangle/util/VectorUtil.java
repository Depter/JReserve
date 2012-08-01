package org.jreserve.triangle.util;

import java.util.*;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.entities.VectorGeometry;


/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorUtil {
    
    private final Calendar calendar = GregorianCalendar.getInstance();
    private final VectorGeometry geometry;
    
    public VectorUtil(Vector vector) {
        this.geometry = vector.getGeometry();
    }
    
    public int getRowCount() {
        Date start = geometry.getStartDate();
        Date end = geometry.getEndDate();
        int step = geometry.getMonthInAccident();
        return monthBetween(start, end, step);
    }
    
    private int monthBetween(Date start, Date end, int step) {
        int stepCount = 0;
        calendar.setTime(start);
        while(calendar.before(end)) {
            calendar.add(Calendar.MONTH, step);
            stepCount++;
        }
        return stepCount;
    }
    
    public List<DataCell> getCells() {
        int size = getRowCount();
        List<DataCell> cells = new ArrayList<DataCell>(size);
        return cells;
    }
}
