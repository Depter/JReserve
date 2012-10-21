package org.jreserve.project.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.SessionFactory;
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
    
    private final static Logger logger = Logger.getLogger(RootElement.class.getName());
    private final static LoadingElement LOADING_CHILD = new LoadingElement();
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
    
    private Result<org.hibernate.SessionFactory> puResult;
    private LookupListener puListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {
            puResultChanged();
        }
    };
    
    private RootElement() {
        super(VALUE);
        puResult = PersistenceUtil.getLookup().lookupResult(org.hibernate.SessionFactory.class);
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
        if(SessionFactory.isConnected())
            loadChildren();
        else
            setChildren(Collections.EMPTY_LIST);
    }
        
    private void loadChildren() {
        addChild(LOADING_CHILD);
        new RootLoader(this).start();
    }
        
    private void loadingFinnished(RootLoader loader) {
        try {
            setChildren(loader.get());
        } catch (Exception ex) {
            setChildren(Collections.EMPTY_LIST);
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
        private Session session;
        private ProgressHandle progress;
        
        private RequestProcessor.Task task;
        private List<ProjectElement> elements;
        private Exception ex;
        
        private RootLoader(RootElement rootElement) {
            this.rootElement = rootElement;
            initialize();
        }
        
        private void initialize() {
            task = RequestProcessor.getDefault().create(this);
            progress = ProgressHandleFactory.createHandle(Bundle.LBL_RootElement_loadingmsg());
        }
        
        void start() {
            task.schedule(0);
        }
        
        @Override
        public void run() {
            try {
                progress.start();
                progress.switchToIndeterminate();
                initSession();
                elements = loadElements();
            } catch(Exception lex) {
                this.ex = lex;
            } finally {
                try {closeSession();} catch (Exception cex) {
                logger.log(Level.SEVERE, "Unable to close session!", cex);}
                progress.finish();
                finnish();
            }
        }
        
        private void initSession() {
            session = SessionFactory.getCurrentSession();
            session.beginTransaction();
        }

        private List<ProjectElement> loadElements() {
            List<ProjectElement> result = new ArrayList<ProjectElement>();
            for(ProjectElementFactory factory : FactoryUtil.getInterestedFactories(VALUE))
                result.addAll(factory.createChildren(VALUE));
            return result;
        }
        
        private void closeSession() {
            if(session == null)
                return;
            session.getTransaction().rollback();
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
}
