package org.jreserve.triangle.editor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.data.Data;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.triangle.entities.AbstractDataStructure;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "# {0} - name",
    "MSG.DataLoader.Loading=Loading data for \"{0}\"..."
})
public class DataLoader<T extends AbstractDataStructure> implements Runnable {
    
    private final static Logger logger = Logger.getLogger(DataLoader.class.getName());

    private final ProgressHandle handle;
    private final RequestProcessor.Task task;
    
    private T dataStructure;
    private Project project;
    private ProjectDataType dataType;
    private final String name;
    private final Callback<T> callback;
    
    private Session session;
    private DataSource dataSource;
    
    private volatile List<Data<ProjectDataType, Double>> datas = null;
    private volatile List<Data<T, Double>> corrections = null;
    
    private volatile RuntimeException ex = null;

    DataLoader(T data, Callback<T> callback) {
        this.callback = callback;
        this.dataStructure = data;
        this.project = data.getProject();
        this.dataType = data.getDataType();
        this.name = data.getName();
        
        this.task = RequestProcessor.getDefault().create(this);
        this.handle = ProgressHandleFactory.createHandle("Loading triangle: " + name, task);
    }

    void start() {
        task.schedule(0);
    }

    @Override
    public void run() {
        handle.start();
        handle.switchToIndeterminate();
        try {
            initSession();
            loadData();
        } catch (RuntimeException rex) {
            this.ex = rex;
            logger.log(Level.SEVERE, "Unable to load data for: " + name, ex);
        } finally {
            closeSession();
        }
        handle.finish();
        fireFinnished();
    }

    private void initSession() {
        session = SessionFactory.openSession();
        project = (Project) session.merge(project);
        dataType = (ProjectDataType) session.merge(dataType);
        dataSource = new DataSource(session);
    }

    private void loadData() {
        this.datas = dataSource.getClaimData(new DataCriteria<ProjectDataType>(dataType));
        this.corrections = dataSource.getCorrections(new DataCriteria<T>(dataStructure));
    }

    private void closeSession() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    private void fireFinnished() {
        if(callback == null)
            return;
        final DataLoader loader = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                callback.finnished(loader);
            }
        });
    }

    public List<Data<ProjectDataType, Double>> getData() {
        if (ex != null)
            throw ex;
        return datas;
    }
    
    public List<Data<T, Double>> getCorrections() {
        if(ex != null)
            throw ex;
        return corrections;
    }

    void cancel() {
        task.cancel();
    }
    
    public static interface Callback<T extends AbstractDataStructure> {
        public void finnished(DataLoader<T> loader);
    }
}

