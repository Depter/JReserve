package org.jreserve.project.system.management;

import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public abstract class PersistentDeletable implements Deletable {

    private Session session;
    
    @Override
    public void delete() {
        initSession();
        try {
            delete(session);
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            throw ex;
        } finally {
            session.close();
        }
    }

    private void initSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }
    
    protected abstract void delete(Session session);
}
