package org.jreserve.triangle.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.DataSource;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.GeometryUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleInputFactory {

    private TriangleGeometry geometry;
    private List<ClaimValue> values;
    
    private GeometryUtil util = new GeometryUtil();
    private int accidentCount;
    private Date developmentEnd;
    
    private List<Date> accidentDates = new ArrayList<Date>();
    private List<List<Date>> developmentDates = new ArrayList<List<Date>>();
    private List<List<Double>> cells = new ArrayList<List<Double>>();
    
    public TriangleInputFactory(TriangleGeometry geometry, List<ClaimValue> values) {
        this.geometry = geometry.copy();
        this.values = new ArrayList<ClaimValue>(values);
    }
    
    public TriangularData buildTriangle() {
        initParams();
        buildCells();
        return createTriangleInput();
    }
    
    private void initParams() {
        accidentCount = geometry.getAccidentPeriods();
        int periods = geometry.getDevelopmentPeriods();
        developmentEnd = util.getDevelopmentEnd(geometry, 0, periods-1);
    }

    
    private void buildCells() {
        for(int accident=0; accident<accidentCount; accident++)
            buildRow(accident);
    }
    
    private void buildRow(int accident) {
        Date aBegin = util.getAccidentBegin(geometry, accident);
        Date aEnd = util.getAccidentEnd(geometry, accident);
        accidentDates.add(aBegin);
        buildRow(accident, aBegin, aEnd);
    }
    
    private void buildRow(int accident, Date aBegin, Date aEnd) {
        List<Double> row = new ArrayList<Double>();
        cells.add(row);
        
        List<Date> dates = new ArrayList<Date>();
        developmentDates.add(dates);
        
        int dev = 0;
        Date dBegin = util.getDevelopmentBegin(geometry, accident, dev);
        
        while(dBegin.before(developmentEnd)) {
            dates.add(dBegin);
            Date dEnd = util.getDevelopmentEnd(geometry, accident, dev);
            row.add(getCellValue(aBegin, aEnd, dBegin, dEnd));
            dBegin = dEnd;
        }
    }
    
    private double getCellValue(Date aBegin, Date aEnd, Date dBegin, Date dEnd) {
        double sum = 0d;
        boolean found = false;
        
        for(ClaimValue value : values) {
            if(withinCell(aBegin, aEnd, dBegin, dEnd, value)) {
                found = true;
                sum += value.getClaimValue();
            }
        }
        
        return found? sum : Double.NaN;
    }
    
    private boolean withinCell(Date aBegin, Date aEnd, Date dBegin, Date dEnd, ClaimValue value) {
        return withinIntervall(value.getAccidentDate(), aBegin, aEnd) &&
               withinIntervall(value.getDevelopmentDate(), dBegin, dEnd);
    }
    
    private boolean withinIntervall(Date date, Date begin, Date end) {
        if(date.before(begin))
            return false;
        return date.before(end);
    }
    
    private TriangleInput createTriangleInput() {
        Date[] accidents = getAccidentDates();
        Date[][] developments = getDevelopmentDates();
        double[][] data = getValues();
        return new TriangleInput(accidents, developments, data);
    }
    
    private Date[] getAccidentDates() {
        Date[] dates = getDates(accidentDates);
        accidentDates.clear();
        return dates;
    }
    
    private Date[] getDates(List<Date> dates) {
        int size = accidentDates.size();
        return dates.toArray(new Date[size]);
    }
    
    private Date[][] getDevelopmentDates() {
        Date[][] dates = new Date[accidentCount][];
        for(int a=0; a<accidentCount; a++)
            dates[a] = getDates(developmentDates.get(a));
        developmentDates.clear();
        return dates;
    }
    
    private double[][] getValues() {
        double[][] result = new double[accidentCount][];
        for(int a=0; a<accidentCount; a++)
            result[a] = getValues(cells.get(a));
        cells.clear();
        return result;
    }
    
    private double[] getValues(List<Double> values) {
        int size = values.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = values.get(i);
        return result;
    }
}
