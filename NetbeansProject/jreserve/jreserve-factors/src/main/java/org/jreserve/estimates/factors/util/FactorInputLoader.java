package org.jreserve.estimates.factors.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.DataSource;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.persistence.SessionTask.AbstractTask;
import org.jreserve.smoothing.Smoother;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.GeometryUtil;
import org.jreserve.triangle.widget.DataUtil;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleCellUtil;
import org.jreserve.triangle.widget.WidgetData;

/**
 *
 * @author Peter Decsi
 */
public class FactorInputLoader extends AbstractTask<List<WidgetData<Double>>> {
    
    private final static Logger logger = Logger.getLogger(FactorInputLoader.class.getName());
    private final static TriangleCell[] ROW_SABLON = new TriangleCell[0];
    
    private String triangleId;
    private GeometryUtil util;
    private TriangleCell[][] cells;
    private TriangleCell[][] factors;
    
//    private final ProgressHandle handle;
//    private final RequestProcessor.Task task;

    @Override
    public void doWork(Session session) throws Exception {
        Triangle triangle = (Triangle) session.load(Triangle.class, triangleId);
        initCells(triangle);
        loadValues(session, triangle);
        applyCorrections(triangle);
        applySmoothings(triangle);
        TriangleCellUtil.cummulate(cells);
        createFactors();
        result = TriangleCellUtil.extractValues(factors);
    }
    
    private void initCells(Triangle triangle) {
        util = new GeometryUtil();
        createCells(triangle);
    }
    
    private void createCells(Triangle triangle) {
        TriangleGeometry geometry = triangle.getGeometry();
        int aps = geometry.getAccidentPeriods();
        cells = new TriangleCell[aps][];
        Date developmentEnd = util.getDevelopmentEnd(geometry, 0, geometry.getDevelopmentPeriods() - 1);
        
        for(int r=0; r<aps; r++)
            cells[r] = createRow(r, geometry, developmentEnd);
    }
    
    private TriangleCell[] createRow(int row, TriangleGeometry geometry, Date developmentEnd) {
        Date aBegin = util.getAccidentBegin(geometry, row);
        Date aEnd = util.getAccidentEnd(geometry, row);
        
        List<TriangleCell> cells = new ArrayList<TriangleCell>();
        int dev = 0;
        Date dBegin = util.getDevelopmentBegin(geometry, row, dev);
        TriangleCell cell = null;
        
        while(dBegin.before(developmentEnd)) {
            Date dEnd = util.getDevelopmentEnd(geometry, row, dev);
            cell = new TriangleCell(cell, row, dev++, aBegin, aEnd, dBegin, dEnd);
            cells.add(cell);
            dBegin = dEnd;
        }
        
        return cells.toArray(ROW_SABLON);
    }
    
    private void loadValues(Session session, Triangle triangle) {
        List<ClaimValue> datas = new DataSource(session).getClaimData(new DataCriteria(triangle.getDataType()));
        List<WidgetData<Double>> values = DataUtil.convertDatas(datas);
        TriangleCellUtil.setCellValues(cells, values, TriangleCell.VALUE_LAYER);
    }
    
    private void applyCorrections(Triangle triangle) {
        List<WidgetData<Double>> corrections = DataUtil.convertCorrections(triangle.getCorrections());
        TriangleCellUtil.setCellValues(cells, corrections, TriangleCell.CORRECTION_LAYER);
    }
    
    private void applySmoothings(Triangle triangle) {
        Smoother smoother = new Smoother(cells, TriangleCell.SMOOTHING_LAYER);
        for(Smoothing smoothing : triangle.getSmoothings())
            smoother.smooth(smoothing);
    }
    
    private void createFactors() {
        factors = new TriangleCell[cells.length][];
        for(int r=0, size=cells.length; r<size; r++) {
            TriangleCell[] row = cells[r];
            if(row.length < 2) {
                factors[r] = ROW_SABLON;
            } else {
                factors[r] = createFactorRow(r, row);
            }            
        }
    }
    
    private TriangleCell[] createFactorRow(int r, TriangleCell[] row) {
        TriangleCell[] result = new TriangleCell[row.length - 1];
        TriangleCell cell = null;
        
        for(int c=0, size=row.length-1; c<size; c++) {
            TriangleCell c1 = row[c];
            TriangleCell c2 = row[c+1];
            cell = new TriangleCell(cell, r, c, c1.getAccidentBegin(), c1.getAccidentEnd(), c1.getDevelopmentBegin(), c1.getDevelopmentEnd());
            double factor = getFactor(c1, c2);
            TriangleCellUtil.setValue(cell, TriangleCell.VALUE_LAYER, factor);
            result[c] = cell;
        }
        return result;
    }
    
    private double getFactor(TriangleCell c1, TriangleCell c2) {
        if(c1 == null || c2 == null) return Double.NaN;
        
        Double d1 = c1.getDisplayValue();
        if(d1 == null) return Double.NaN;
        
        double pd1 = d1.doubleValue();
        if(Double.isNaN(pd1) || pd1 == 0d) return Double.NaN;
        
        Double d2 = c2.getDisplayValue();
        if(d2 == null) return Double.NaN;
        
        double pd2 = d2.doubleValue();
        if(Double.isNaN(pd2)) return Double.NaN;
        
        return pd2 / pd1;
    }
}
