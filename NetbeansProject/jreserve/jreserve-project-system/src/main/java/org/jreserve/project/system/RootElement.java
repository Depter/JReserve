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
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RootElement extends ProjectElement {
    
    public static List<ProjectElementFactory> getFactories(Object value) {
        return FactoryUtil.getInterestedFactories(value);
    }
    
    private final static Logger logger = Logging.getLogger(RootElement.class.getName());
    private final static ProjectElement LOADING_CHILD = new ProjectElement("Loading...");
    private final static RootValue VALUE = new RootValue();
    
    public RootElement() {
        super(VALUE);
    }

    @Override
    public Node createNodeDelegate() {
        return new AbstractNode(new RootChildren());
    }

    @Override
    public ProjectElement getParent() {
        return null;
    }
    
    @Override
    void setParent(ProjectElement element) {
        throw new UnsupportedOperationException("Can not set parent for root!");
    }
    
    public static class RootValue {
        @Override
        public String toString() {
            return "ROOT";
        }
    }
    
    private class RootChildren extends Children.Keys<ProjectElement> implements LookupListener {
        
        private final Result<PersistenceUnit> result;
        
        private RootChildren() {
            result = PersistenceUtil.getLookup().lookupResult(PersistenceUnit.class);
            result.addLookupListener(this);
        }

        @Override
        protected void addNotify() {
            resultChanged(null);
        }

        @Override
        public void resultChanged(LookupEvent le) {
            PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
            if(pu == null)
                removeChildren();
            else
                loadChildren(pu);
        }
        
        private void removeChildren() {
            setChildren(new ArrayList<ProjectElement>());
        }
        
        private void loadChildren(PersistenceUnit pu) {
            addLoadingChild();
            new RootLoader(this, pu).start();
        }
        
        private void addLoadingChild() {
            RootElement.this.addChild(LOADING_CHILD);
            setKeys(Collections.singleton(LOADING_CHILD));
        }
        
        private void loadingFinnished(RootLoader loader) {
            try {
                removeLoadingChild();
                setChildren(loader.get());
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        private void removeLoadingChild() {
            RootElement.this.removeChild(LOADING_CHILD);
            setKeys(Collections.EMPTY_LIST);
        }
        
        private void setChildren(List<ProjectElement> newChildren) {
            super.setKeys(newChildren);
        }
        
        @Override
        protected Node[] createNodes(ProjectElement t) {
            return new Node[]{t.createNodeDelegate()};
        }
    }
    
    private class RootLoader implements Runnable {
        
        private final RootChildren children;
        private final PersistenceUnit pu;
        private Session session;
        private ProgressHandle progress;
        
        private RequestProcessor.Task task;
        private List<ProjectElement> elements;
        private Exception ex;
        
        private RootLoader(RootChildren children, PersistenceUnit pu) {
            this.children = children;
            this.pu = pu;
            initialize();
        }
        
        private void initialize() {
            task = RequestProcessor.getDefault().create(this);
            progress = ProgressHandleFactory.createHandle("Loading database");
            progress.switchToIndeterminate();
        }
        
        void start() {
            task.schedule(0);
        }
        
        @Override
        public void run() {
            try {
                progress.start();
                Thread.sleep(3000);
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
                result.addAll(factory.createChildren(VALUE, session));
            return result;
        }
        
        private void closeSession() {
            if(session == null)
                return;
            session.comitTransaction();
            session.close();
            session = null;
        }
        
        private void finnish() {
            final RootLoader loader = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    children.loadingFinnished(loader);
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
