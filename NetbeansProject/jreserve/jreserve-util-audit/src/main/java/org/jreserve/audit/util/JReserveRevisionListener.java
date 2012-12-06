package org.jreserve.audit.util;

import org.jreserve.audit.JReserveRevisionEntity;
import org.hibernate.envers.RevisionListener;

/**
 *
 * @author Peter Decsi
 */
public class JReserveRevisionListener implements RevisionListener {

    private final static String USER_NAME = "user.name";
    private final static String UNKOWN = "unknown";
    
    @Override
    public void newRevision(Object o) {
        JReserveRevisionEntity revision = (JReserveRevisionEntity) o;
        revision.setUserName(getUserName());
    }
    
    private String getUserName() {
        String name = System.getProperty(USER_NAME);
        if(name == null)
            return UNKOWN;
        return name;
    }

}
