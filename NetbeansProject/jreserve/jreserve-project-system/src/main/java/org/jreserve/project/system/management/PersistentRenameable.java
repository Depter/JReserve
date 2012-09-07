package org.jreserve.project.system.management;

import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.PersistentRenameable.emptyname=Empty name is invalid!"
})
public abstract class PersistentRenameable implements Renameable {
    
    private final static Logger logger = Logging.getLogger(PersistentRenameable.class.getName());

    protected Session session;
    
    @Override
    public void setName(String name) {
        if(checkName(name))
            rename(name);
    }
    
    protected boolean checkName(String name) {
        String original = getEntityName();
        if(original.equals(name))
            return false;
        if(original.equalsIgnoreCase(name))
            return true;
        return checkNotNull(name) && checkNotExists(name);
    }
    
    protected abstract String getEntityName();
    
    private boolean checkNotNull(String name) {
        if(name != null && name.trim().length() > 0)
            return true;
        showError(Bundle.MSG_PersistentRenameable_emptyname());
        return false;
    }
        
    protected void showError(String msg) {
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }
        
    protected abstract boolean checkNotExists(String name);
    
    protected void rename(String newName) {
        String original = getEntityName();
        Object entity = getEntity();
        
        initSession();
        try {
            setEntityName(newName);
            session.update(entity);
            session.comitTransaction();
            logger.info("Entity '%s' renamed to '%s'!", original, newName);
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            setEntityName(original);
            logger.error(ex, "Unable to rename entity '%s' to %s!", entity, newName);
            throw ex;
        }
    }
    
    private void initSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }
    
    protected abstract void setEntityName(String newName);
    
    protected abstract Object getEntity();
}
