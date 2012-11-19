package org.jreserve.estimates.factors.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.DataSource;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.persistence.SessionTask;
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
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - triangle name",
    "MSG.FactorInputLoader.HandlerName=Loading triangle {0}"
})
public class FactorInputLoader extends AbstractTask<TriangleCell[][]> implements Runnable{
    
    public static interface Callback {
        public void finnished(TriangleCell[][] cells);
        public void finnished(Exception ex);
    }
    
    private final static Logger logger = Logger.getLogger(FactorInputLoader.class.getName());
    private final static TriangleCell[] ROW_SABLON = new TriangleCell[0];
    
    private final Callback callBack;
    private final ProgressHandle handle;
    private final RequestProcessor.Task task;
    
    private final String triangleName;
    private final String triangleId;
    private GeometryUtil util;
    
    public FactorInputLoader(Triangle triangle, Callback callBack) {
        this.triangleName = triangle.getName();
        this.triangleId = triangle.getId();
        this.callBack = callBack;
        
        this.task = RequestProcessor.getDefault().create(this);
        handle = ProgressHandleFactory.createHandle(Bundle.MSG_FactorInputLoader_HandlerName(triangleName), task);
    }
    
    public void start() {
        task.schedule(0);
    }
    
    public void cancel() {
        task.cancel();
    }
    
    @Override
    public void run() {
        try {
            handle.start(5);
            TriangleCell[][] cells = SessionTask.withOpenCurrentSession(this);
            callBack(cells);
        } catch (Exception ex) {
            callBack(ex);
        } finally {
            handle.finish();
        }
    }
    
    private void callBack(final TriangleCell[][] cells) {
        if(callBack == null) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                callBack.finnished(cells);
            }
        });
    }
    
    private void callBack(final Exception ex) {
        if(callBack == null) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                callBack.finnished(ex);
            }
        });
    }

    @Override
    public void doWork(Session session) throws Exception {
        logger.log(Level.FINE, "Loading triangle data: {0}", triangleName);
        try {
            loadCells(session);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to load triangle data: "+triangleName, ex);
            throw ex;
        }
    }
    
    private void loadCells(Session session) {
        Triangle triangle = (Triangle) session.load(Triangle.class, triangleId);
        handle.progress(1);

        initCells(triangle);
        handle.progress(1);

        loadValues(session, triangle);
        handle.progress(1);

        applyCorrections(triangle);
        handle.progress(1);

        applySmoothings(triangle);
        handle.progress(1);
    }
    
    private void initCells(Triangle triangle) {
        util = new GeometryUtil();
        createCells(triangle);
    }
    
    private void createCells(Triangle triangle) {
        TriangleGeometry geometry = triangle.getGeometry();
        int aps = geometry.getAccidentPeriods();
        result = new TriangleCell[aps][];
        Date developmentEnd = util.getDevelopmentEnd(geometry, 0, geometry.getDevelopmentPeriods() - 1);
        
        for(int r=0; r<aps; r++)
            result[r] = createRow(r, geometry, developmentEnd);
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
        TriangleCellUtil.setCellValues(result, values, TriangleCell.VALUE_LAYER);
    }
    
    private void applyCorrections(Triangle triangle) {
        List<WidgetData<Double>> corrections = DataUtil.convertCorrections(triangle.getCorrections());
        TriangleCellUtil.setCellValues(result, corrections, TriangleCell.CORRECTION_LAYER);
    }
    
    private void applySmoothings(Triangle triangle) {
        Smoother smoother = new Smoother(result, TriangleCell.SMOOTHING_LAYER);
        for(Smoothing smoothing : triangle.getSmoothings())
            smoother.smooth(smoothing);
    }
}
