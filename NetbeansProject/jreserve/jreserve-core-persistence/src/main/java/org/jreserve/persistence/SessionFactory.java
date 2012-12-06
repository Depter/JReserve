package org.jreserve.persistence;

import org.hibernate.Session;
import org.jreserve.persistence.connection.HibernateUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SessionFactory {

    
    public static Session openSession() {
        return getSessionFactory().openSession();
    }
    
    private static org.hibernate.SessionFactory getSessionFactory() {
        org.hibernate.SessionFactory factory = HibernateUtil.getLookup().lookup(org.hibernate.SessionFactory.class);
        if(factory != null)
            return factory;
        throw new IllegalStateException("There is no live database connection!");
    }
    
    public static Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }
    
    public static boolean isConnected() {
        org.hibernate.SessionFactory factory = HibernateUtil.getLookup().lookup(org.hibernate.SessionFactory.class);
        return factory != null;
    }
    
    private SessionFactory() {}
}
