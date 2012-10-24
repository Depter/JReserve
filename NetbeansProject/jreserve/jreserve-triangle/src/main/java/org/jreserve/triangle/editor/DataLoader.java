package org.jreserve.triangle.editor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.Data;
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
public class DataLoader implements Runnable {
    
    private final static Logger logger = Logger.getLogger(DataLoader.class.getName());

    private final ProgressHandle handle;
    private final RequestProcessor.Task task;
    private Project project;
    private ProjectDataType dataType;
    private final String name;
    private final Callback callback;
    
    private DataCriteria<ProjectDataType> criteria;
    private Session session;
    private volatile List<Data<ProjectDataType, Double>> datas = null;
    private volatile RuntimeException ex = null;

    DataLoader(AbstractDataStructure data, Callback callback) {
        this.callback = callback;
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
            initCriteria();
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
    }

    private void initCriteria() {
        criteria = new DataCriteria<ProjectDataType>(dataType);
    }

    private void loadData() {
        this.datas = new DataSource(session).getClaimData(criteria);
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
        if (ex != null) {
            throw ex;
        }
        return datas;
    }

    void cancel() {
        task.cancel();
    }
    
    public static interface Callback {
        public void finnished(DataLoader loader);
    }
}

