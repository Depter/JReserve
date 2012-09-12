package org.jreserve.project.system.management;

import java.io.IOException;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PersistentSavable extends AbstractProjectElementSavable {
    
    protected Session session;
    
    protected final static Logger logger = Logging.getLogger(PersistentSavable.class.getName());

    public PersistentSavable(ProjectElement element) {
        super(element);
    }
    
    @Override
    protected void handleSave() throws IOException {
        try {
            initSession();
            saveEntity();
            session.comitTransaction();
        } catch (Exception ex) {
            session.rollBackTransaction();
            logger.error(ex, "Unable to save entities, transaction is rolled back.");
            throw new IOException(ex);
        } finally {
            element.removeFromLookup(this);
            unregister();
        }
    }

    private void initSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }

    protected void saveEntity() {
        session.persist(element.getValue());
    }
}
