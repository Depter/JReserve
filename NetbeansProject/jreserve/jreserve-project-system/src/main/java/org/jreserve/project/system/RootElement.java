package org.jreserve.project.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.util.FactoryUtil;
import org.jreserve.project.system.util.LoadingElement;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL_RootElement.loadingmsg=Loading database"
})
public class RootElement extends ProjectElement {
    
    private final static Logger logger = Logging.getLogger(RootElement.class.getName());
    private final static ProjectElement LOADING_CHILD = new LoadingElement();
    private final static RootValue VALUE = new RootValue();
    
    private static RootElement DEFAULT = null;
    
    public static RootElement getDefault() {
        if(DEFAULT == null)
            DEFAULT = new RootElement();
        return DEFAULT;
    }
    
    public static List<ProjectElementFactory> getFactories(Object value) {
        return FactoryUtil.getInterestedFactories(value);
    }
    
    private Result<PersistenceUnit> puResult;
    private LookupListener puListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {
            puResultChanged();
        }
    };
    
    private RootElement() {
        super(VALUE);
        puResult = PersistenceUtil.getLookup().lookupResult(PersistenceUnit.class);
        puResult.addLookupListener(puListener);
    }

    @Override
    public Node createNodeDelegate() {
        return new DefaultProjectNode(this);
    }

    @Override
    public ProjectElement getParent() {
        return null;
    }
    
    @Override
    void setParent(ProjectElement element) {
        throw new UnsupportedOperationException("Can not set parent for root!");
    }
    
    private void puResultChanged() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        if(pu == null)
            setChildren(Collections.EMPTY_LIST);
        else
            loadChildren(pu);
    }
        
    private void loadChildren(PersistenceUnit pu) {
        addChild(LOADING_CHILD);
        new RootLoader(this, pu).start();
    }
        
    private void loadingFinnished(RootLoader loader) {
        try {
            setChildren(loader.get());
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
            
    public static class RootValue {
        @Override
        public String toString() {
            return "ROOT";
        }
    }
    
    private class RootLoader implements Runnable {
        
        private final RootElement rootElement;
        private final PersistenceUnit pu;
        private Session session;
        private ProgressHandle progress;
        
        private RequestProcessor.Task task;
        private List<ProjectElement> elements;
        private Exception ex;
        
        private RootLoader(RootElement rootElement, PersistenceUnit pu) {
            this.rootElement = rootElement;
            this.pu = pu;
            initialize();
        }
        
        private void initialize() {
            task = RequestProcessor.getDefault().create(this);
            progress = ProgressHandleFactory.createHandle(Bundle.LBL_RootElement_loadingmsg());
            progress.switchToIndeterminate();
        }
        
        void start() {
            task.schedule(0);
        }
        
        @Override
        public void run() {
            try {
                progress.start();
                openSession();
                elements = loadElements();
            } catch(Exception lex) {
                this.ex = lex;
            } finally {
                try {closeSession();} catch (Exception cex) {
                logger.error(cex, "Unable to close session!");}
                progress.finish();
                finnish();
            }
        }
        
        private void openSession() {
            session = pu.getSession();
            session.beginTransaction();
        }

        private List<ProjectElement> loadElements() {
            List<ProjectElement> result = new ArrayList<ProjectElement>();
            for(ProjectElementFactory factory : FactoryUtil.getInterestedFactories(VALUE))
                result.addAll(factory.createChildren(VALUE, new RootSession(session)));
            return result;
        }
        
        private void closeSession() {
            if(session == null)
                return;
            session.comitTransaction();
            session = null;
        }
        
        private void finnish() {
            final RootLoader loader = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    rootElement.loadingFinnished(loader);
                }
            });
        }
        
        List<ProjectElement> get() throws Exception {
            if(ex == null)
                return elements;
            throw ex;
        }
    }
    
    private class RootSession implements Session {
        
        private final Session session;
        private RootSession(Session session) {
            this.session = session;
        }
        
        @Override
        public void beginTransaction() {
            throw new UnsupportedOperationException("Transaction already opened!");
        }

        @Override
        public void comitTransaction() {
            throw new UnsupportedOperationException("Do not comit this session, others may need it!");
        }

        @Override
        public void rollBackTransaction() {
            throw new UnsupportedOperationException("Do not roll back this session, others may need it!");
        }

        @Override
        public void close() {
            throw new UnsupportedOperationException("Do not close this session, others may need it!");
        }

        @Override
        public <E> List<E> getAll(Class<E> c) {
            return session.getAll(c);
        }

        @Override
        public void persist(Object o) {
            throw new UnsupportedOperationException("Do not use this session to save entities! this sesison is only for loading.");
        }

        @Override
        public void persist(Object... o) {
            throw new UnsupportedOperationException("Do not use this session to save entities! this sesison is only for loading.");
        }

        @Override
        public void delete(Object o) {
            throw new UnsupportedOperationException("Do not use this session to delete entities! this sesison is only for loading.");
        }

        @Override
        public void delete(Object... o) {
            throw new UnsupportedOperationException("Do not use this session to delete entities! this sesison is only for loading.");
        }
    }
}
