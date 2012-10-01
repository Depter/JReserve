package org.jreserve.persistence;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SessionFactory {

    public static Session createSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        if(pu == null)
            throw new IllegalStateException("There is no database connection!");
        return pu.getSession();
    }
    
    public static Session beginTransaction() {
        Session session = createSession();
        session.beginTransaction();
        return session;
    }
    
    private SessionFactory() {}
}
