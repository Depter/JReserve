package org.jreserve.triangle.data.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.persistence.SessionTask;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AsynchronousTriangleInput implements TriangularData {

    private final static Logger logger = Logger.getLogger(AsynchronousTriangleInput.class.getName());
    
    private boolean loading = false;
    private final ProjectDataType dataType;
    
    private List<ClaimValue> values = null;
    private TriangleGeometry geometry = null;
    private TriangularData data = TriangularData.EMPTY;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public AsynchronousTriangleInput(ProjectDataType dataType) {
        this(null, dataType);
    }
    
    public AsynchronousTriangleInput(TriangleGeometry geometry, ProjectDataType dataType) {
        this.geometry = geometry;
        this.dataType = dataType;
    }
    
    @Override
    public int getAccidentCount() {
        checkLoaded();
        return data.getAccidentCount();
    }
    
    private void checkLoaded() {
        if(values == null && !loading) {
            loading = true;
            logger.log(Level.INFO, "Loading claim values for ProjectDataType [{0}; {1}]", new Object[]{dataType.getDbId(), dataType.getName()});
            new SwingLoader().execute();
        }
    }

    @Override
    public int getDevelopmentCount() {
        checkLoaded();
        return data.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        checkLoaded();
        return data.getDevelopmentCount(accident);
    }

    @Override
    public Date getAccidentName(int accident) {
        checkLoaded();
        return data.getAccidentName(accident);
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        checkLoaded();
        return data.getDevelopmentName(accident, development);
    }

    @Override
    public double getValue(int accident, int development) {
        checkLoaded();
        return data.getValue(accident, development);
    }

    @Override
    public double[][] getData() {
        checkLoaded();
        return data.getData();
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        checkLoaded();
            return data.getLayerTypeId(accident, development);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    public void setTriangleGeometry(TriangleGeometry geometry) {
        initGeometry(geometry);
        buildData();
    }
    
    private void initGeometry(TriangleGeometry geometry) {
        if(geometry == null) 
            throw new NullPointerException("Geometry can not be null!");
        this.geometry = geometry.copy();
    }
    
    private void buildData() {
        this.data = new TriangleInputFactory(geometry).buildTriangle(values);
        fireChange();
    }
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    private void setValues(List<ClaimValue> values) {
        this.values = new ArrayList<ClaimValue>(values);
        buildData();
    }
    
    private class SwingLoader extends SwingWorker<List<ClaimValue>, Void> {

        @Override
        protected List<ClaimValue> doInBackground() throws Exception {
            
            DataLoaderTask task = new DataLoaderTask();
            return SessionTask.withOpenSession(task);
        }

        @Override
        protected void done() {
            setValues(getValues());
            loading = false;
        }
        
        private List<ClaimValue> getValues() {
            try {
                return get();
            } catch (Exception ex) {
                String msg = String.format("Unable to load claim values for ProjectDataType [%d; %s]", dataType.getDbId(), dataType.getName());
                logger.log(Level.INFO, msg, ex);
                //TODO notify user
                return Collections.EMPTY_LIST;
            }
        }
    }
    
    private class DataLoaderTask extends SessionTask.AbstractTask<List<ClaimValue>> {

        @Override
        public void doWork(Session session) throws Exception {
            DataSource ds = new DataSource(session);
            DataCriteria criteria = new DataCriteria(dataType);
            result = ds.getClaimData(criteria);
        }
    }
}