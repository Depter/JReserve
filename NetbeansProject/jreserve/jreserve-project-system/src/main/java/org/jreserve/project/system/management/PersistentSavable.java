package org.jreserve.project.system.management;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class PersistentSavable<T> extends AbstractProjectElementSavable<T> {
    
    protected Session session;
    
    protected final static Logger logger = Logger.getLogger(PersistentSavable.class.getName());

    public PersistentSavable(ProjectElement<T> element) {
        super(element);
    }
    
    @Override
    protected void saveElement() throws IOException {
        try {
            session = SessionFactory.beginTransaction();
            saveEntity();
            session.comitTransaction();
        } catch (Exception ex) {
            session.rollBackTransaction();
            logger.log(Level.SEVERE, "Unable to save entities, transaction is rolled back.", ex);
            throw new IOException(ex);
        } finally {
            element.removeFromLookup(this);
        }
    }

    protected void saveEntity() {
        session.update(element.getValue());
    }
}
