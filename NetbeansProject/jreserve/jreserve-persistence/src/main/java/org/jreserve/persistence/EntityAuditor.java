package org.jreserve.persistence;

import java.util.List;

/**
 *
 * @author Peter Decsi
 */
public interface EntityAuditor<T> {

    public T getEntity();
    
    public Object getId();
    
    public String getChange(T previous, T current);
    
    public List<Class<?>> getSubEntities();
    
    public String getName();
}
