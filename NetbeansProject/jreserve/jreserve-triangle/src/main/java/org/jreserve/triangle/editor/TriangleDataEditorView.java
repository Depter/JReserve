package org.jreserve.triangle.editor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.data.Criteria;
import org.jreserve.data.Data;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.guiutil.TriangleFormatVisualPanel;
import org.jreserve.triangle.mvc.TriangleLoader;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleDataEditorView extends TriangleFormatVisualPanel implements MultiViewElement, Serializable, ChangeListener {
    
    private final static Logger logger = Logger.getLogger(TriangleDataEditorView.class.getName());
    
    private JToolBar toolBar = new JToolBar();
    private TriangleProjectElement element;
    private MultiViewElementCallback callBack;
    private TriangleLoader loader;
    
    public TriangleDataEditorView(TriangleProjectElement element) {
        this.element = element;
        super.addChangeListener(this);
        initGeometry(element.getValue().getGeometry());
        new DataLoader(element.getValue()).start();
    }
    
    private void initGeometry(TriangleGeometry geometry) {
        initBegin(geometry);
        initPeriods(geometry);
        initMonths(geometry);
    }
    
    private void initBegin(TriangleGeometry geometry) {
        if(isBeginSymmetric(geometry)) {
            geometrySetting.setSymmetricFromDate(true);
            geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
        } else {
            geometrySetting.setSymmetricFromDate(false);
            geometrySetting.setAccidentStartDate(geometry.getAccidentStart());
            geometrySetting.setDevelopmentStartDate(geometry.getDevelopmentStart());
        }
    }
    
    private boolean isBeginSymmetric(TriangleGeometry geometry) {
        Date aBegin = geometry.getAccidentStart();
        Date dBegin = geometry.getDevelopmentStart();
        return aBegin.equals(dBegin);
    }
    
    private void initPeriods(TriangleGeometry geometry) {
        if(isPeriodSymmetric(geometry)) {
            geometrySetting.setSymmetricPeriods(true);
            geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
        } else {
            geometrySetting.setSymmetricPeriods(false);
            geometrySetting.setAccidentPeriodCount(geometry.getAccidentPeriods());
            geometrySetting.setDevelopmentPeriodCount(geometry.getDevelopmentPeriods());
        }
    }
    
    private boolean isPeriodSymmetric(TriangleGeometry geometry) {
        int aPeriod = geometry.getAccidentPeriods();
        int dPeriod = geometry.getDevelopmentPeriods();
        return aPeriod == dPeriod;
    }
    
    private void initMonths(TriangleGeometry geometry) {
        if(isMonthsSymmetric(geometry)) {
            geometrySetting.setSymmetricMonths(true);
            geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
        } else {
            geometrySetting.setSymmetricMonths(true);
            geometrySetting.setAccidentMonthsPerStep(geometry.getMonthInAccident());
            geometrySetting.setDevelopmentMonthsPerStep(geometry.getMonthInDevelopment());
        }
    }
    
    private boolean isMonthsSymmetric(TriangleGeometry geometry) {
        int aMonth = geometry.getMonthInAccident();
        int dMonth = geometry.getMonthInDevelopment();
        return aMonth == dMonth;
    }
    
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        if(callBack == null)
            return new Action[0];
        return callBack.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        return element.getLookup();
    }
    
    @Override 
    public void componentClosed() {
        if(loader != null) {
            loader.cancel();
            loader = null;
        }
    }

    @Override public void componentOpened() {}
    @Override public void componentShowing() {}
    @Override public void componentHidden() {}
    @Override public void componentActivated() {}
    @Override public void componentDeactivated() {}

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        this.callBack = mvec;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    private void finnished(DataLoader loader) {
        try {
            List<Data<Double>> datas = loader.getData();
            setData(datas);
        } catch (RuntimeException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private void setData(List<Data<Double>> datas) {
        super.setDatas(datas);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TriangleGeometry triangleGeometry = super.getGeometry();
        if(triangleGeometry != null)
            element.setProperty(TriangleProjectElement.GEOMETRY_PROPERTY, triangleGeometry);
    }
    
    private class DataLoader implements Runnable {
        private final ProgressHandle handle;
        private final RequestProcessor.Task task;
        
        private Project project;
        private ProjectDataType dataType;
        private final TriangleGeometry geometry;
        private final String triangleId;
        private final String triangleName;
        
        private Criteria criteria;
        private Session session;
        
        private volatile List<Data<Double>> datas = null;
        private volatile RuntimeException ex = null;
        
        private DataLoader(Triangle triangle) {
            this.triangleId = triangle.getId();
            this.project = triangle.getProject();
            this.dataType = triangle.getDataType();
            this.geometry = triangle.getGeometry().copy();
            this.triangleName = triangle.getName();
            this.task = RequestProcessor.getDefault().create(this);
            this.handle = ProgressHandleFactory.createHandle("Loading triangle: "+triangleName, task);
        }

        private void start() {
            task.schedule(0);
        }
        
        @Override
        public void run() {
            handle.start();
            handle.switchToIndeterminate();
            try {
                initSession();
                initCriteria();
                loadData();
            } catch (RuntimeException rex) {
                this.ex = rex;
                logger.log(Level.SEVERE, "Unable to load data for triangle: "+triangleName, ex);
            } finally {
                closeSession();
            }
            handle.finish();
            fireFinnished();
        }
    
        private void initSession() {
            session = SessionFactory.createSession();
            project = (Project) session.merge(project);
            dataType = (ProjectDataType) session.merge(dataType);
        }
    
        private void initCriteria() {
            criteria = new Criteria(project.getClaimType());
            criteria.setDataType(dataType);
        }
        
        private void loadData() {
            this.datas = new DataSource(session).getClaimData(criteria);
        }
        
    
        private void closeSession() {
            if(session != null) {
                session.close();
                session = null;
            }
        }
        
        private void fireFinnished() {
            final DataLoader loader = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    TriangleDataEditorView.this.finnished(loader);
                }
            });
        }
        
        public List<Data<Double>> getData() {
            if(ex != null)
                throw ex;
            return datas;
        }
    } 
}
