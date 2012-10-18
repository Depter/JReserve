package org.jreserve.triangle.mvc;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jreserve.data.Criteria;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleCorrection;
import org.jreserve.triangle.entities.TriangleGeometry;
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
    
    private final ProgressHandle handle;
    private final RequestProcessor.Task task;
    
    private final Callback callBack;
    private Project project;
    private ProjectDataType dataType;
    private final TriangleGeometry geometry;
    private final String triangleId;
    private final String triangleName;
    
    private Criteria criteria;
    
    private Session session;
    
    private volatile RuntimeException ex;
    private volatile TriangleTable<Double> table;
    
    public TriangleLoader(Triangle triangle, Callback callBack) {
        this.callBack = callBack;
        this.triangleId = triangle.getId();
        this.project = triangle.getProject();
        this.dataType = triangle.getDataType();
        this.geometry = copyGeometry(triangle.getGeometry());
        this.triangleName = triangle.getName();
        this.task = RequestProcessor.getDefault().create(this);
        this.handle = ProgressHandleFactory.createHandle(getCaption(), task);
    }
    
    private TriangleGeometry copyGeometry(TriangleGeometry geometry) {
        Date aStart = geometry.getAccidentStart();
        int aPeriods = geometry.getAccidentPeriods();
        int aMonths = geometry.getMonthInAccident();
        Date dStart = geometry.getDevelopmentStart();
        int dPeriods = geometry.getDevelopmentPeriods();
        int dMonths = geometry.getMonthInDevelopment();
        return new TriangleGeometry(aStart, aPeriods, aMonths, dStart, dPeriods, dMonths);
    }
    
    private String getCaption() {
        return Bundle.MSG_TriangleLoader_Title(triangleName);
    }

    public void start() {
        task.schedule(0);
    }
    
    @Override
    public void run() {
        handle.start();
        handle.switchToIndeterminate();
        try {
            initSession();
            initCriteria();
            createTable();
        } catch (RuntimeException ex) {
            this.ex = ex;
            logger.log(Level.SEVERE, "Unable to load data for triangle: "+triangleName, ex);
        } finally {
            closeSession();
        }
        handle.finish();
        callBack();
    }
    
    private void initSession() {
        session = SessionFactory.createSession();
        project = (Project) session.merge(project);
        dataType = (ProjectDataType) session.merge(dataType);
    }
    
    private void initCriteria() {
        criteria = new Criteria(project.getClaimType());
        criteria.setDataType(dataType);
        //setGeometry();
    }
    
    private void setGeometry() {
        setAccidentGeometry();
        setDevelopmentGeometry();
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
    
    private void createTable() {
        TriangleTableFactory<Double> factory = new TriangleTableFactory<Double>(geometry);
        table = factory.buildTable();
        loadData();
        loadUtilityData();
    }
    
    private void loadData() {
        DataSource ds = new DataSource(session);
        TriangleTableUtil.setValues(table, ds.getClaimData(criteria));
    }
    
    private void loadUtilityData() {
        loadCorrections();
        //TODO load comments
    }
    
    private void loadCorrections() {
        List<TriangleCorrection> corrections = query(CORRECTION_QUERY);
        
    }
    
    private List query(String sql) {
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
    
    private void closeSession() {
        if(session == null)
            return;
        session.close();
        session = null;
    }
    
    public static interface Callback {
        public void finnished(TriangleLoader loader);
    }
}
