package org.jreserve.persistence;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Session {
    
    public void beginTransaction();
    
    public void comitTransaction();
    
    public void rollBackTransaction();
    
    public void close();
    
    public <E> List<E> getAll(Class<E> c);
    
    public void persist(Object entity);
    
    public void persist(Object... entity);
    
    public void delete(Object entity);
    
    public void delete(Object... entity);
    
    public void update(Object entity);
    
    public void update(Object... entity);
    
    public Query createQuery(String query);
    
    public Query createNamedQuery(String name);
    
    public <T> T find(Class<T> type, Serializable oid);
}
