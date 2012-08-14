package org.jreserve.persistence;

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
    
    public void persist(Object o);
    
    public void persist(Object... o);
    
    public void delete(Object o);
    
    public void delete(Object... o);
}
