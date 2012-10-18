package org.jreserve.triangle.mvc;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jreserve.data.Criteria;
import org.jreserve.data.Data;
import org.jreserve.data.DataSource;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleCorrection;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleRow;
import org.jreserve.triangle.mvc.model.TriangleTable;
import org.jreserve.triangle.mvc.model.TriangleTableFactory;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - the name",
    "MSG.TriangleLoader.Title=Loading {0}..."
})
public class TriangleLoader implements Runnable {
    
    private final static String CORRECTION_QUERY = "SELECT c FROM TriangleCorrection c WHERE c.triangle.id = :triangleId";
    
    private final static Logger logger = Logger.getLogger(TriangleLoader.class.getName());
    
    private ProgressHandle handle;
    private RequestProcessor.Task task;
    
    private final Callback callBack;
    private String triangleName;
    private String triangleId;
    private TriangleGeometry geometry;
    private Criteria criteria;
    
    private volatile RuntimeException ex;
    private volatile TriangleTable<Double> table;
    
    public TriangleLoader(Triangle triangle, Callback callBack) {
        this.callBack = callBack;
        task = RequestProcessor.getDefault().create(this);
        handle = ProgressHandleFactory.createHandle(getCaption(triangle), task);
        initCriteria(triangle);
        triangleId = triangle.getId();
    }
    
    private String getCaption(Triangle triangle) {
        triangleName = triangle.getName();
        return Bundle.MSG_TriangleLoader_Title(triangleName);
    }
    
    private void initCriteria(Triangle triangle) {
        criteria = new Criteria(triangle.getProject().getClaimType());
        criteria.setDataType(triangle.getDataType());
        setGeometry(triangle);
    }
    
    private void setGeometry(Triangle triangle) {
        copyGeometry(triangle.getGeometry());
        setAccidentGeometry();
        setDevelopmentGeometry();
    }
    
    private void copyGeometry(TriangleGeometry geometry) {
        Date aFrom = geometry.getAccidentStart();
        int aPeriods = geometry.getAccidentPeriods();
        int aMonths = geometry.getMonthInAccident();
        Date dFrom = geometry.getDevelopmentStart();
        int dPeriods = geometry.getDevelopmentPeriods();
        int dMonths = geometry.getMonthInDevelopment();
        this.geometry = new TriangleGeometry(aFrom, aPeriods, aMonths, dFrom, dPeriods, dMonths);
    }
    
    private void setAccidentGeometry() {
        Date from = geometry.getAccidentStart();
        Date end = getDate(from, geometry.getAccidentPeriods());
        criteria.setFromAccidentDate(from);
        criteria.setToAccidentDate(end);
    }
    
    private Date getDate(Date begin, int after) {
        return null;
    }
    
    private void setDevelopmentGeometry() {
        Date from = geometry.getDevelopmentStart();
        Date end = getDate(from, geometry.getDevelopmentPeriods());
        criteria.setFromDevelopmentDate(from);
        criteria.setToDevelopmentDate(end);
    }

    public void start() {
        task.schedule(0);
    }
    
    @Override
    public void run() {
        handle.start();
        handle.switchToIndeterminate();
        try {
            createTable();
        } catch (RuntimeException ex) {
            this.ex = ex;
            logger.log(Level.SEVERE, "Unable to load data for triangle: "+triangleName, ex);
        }
        handle.finish();
        callBack();
    }
    
    private void createTable() {
        TriangleTableFactory<Double> factory = new TriangleTableFactory<Double>(geometry);
        table = factory.buildTable();
        loadData();
        loadUtilityData();
    }
    
    private void loadData() {
        DataSource ds = null;
        try {
            ds = new DataSource();
            ds.open();
            TriangleTableUtil.setValues(table, ds.getClaimData(criteria));
        } finally {
            if(ds != null)
                ds.rollBack();
        }
    }
    
    private void loadUtilityData() {
        Session session = null;
        try {
            session = SessionFactory.beginTransaction();
            loadCorrections(session);
            //TODO load comments
        } finally {
            if(session != null)
                session.close();
        }
    }
    
    private void loadCorrections(Session session) {
        List<TriangleCorrection> corrections = query(CORRECTION_QUERY, session);
        
    }
    
    private List query(String sql, Session session) {
        Query query = session.createQuery(sql);
        query.setParameter("triangleId", triangleId);
        return query.getResultList();
    }
    
    private void callBack() {
        if(callBack == null)
            return;
        final TriangleLoader loader = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                callBack.finnished(loader);
            }
        });
    }
    
    public TriangleTable getResult() {
        if(ex != null)
            throw ex;
        return table;
    }
    
    public void cancel() {
        if(!task.isFinished())
            task.cancel();
    }
    
    public static interface Callback {
        public void finnished(TriangleLoader loader);
    }
}
