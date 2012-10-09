package org.jreserve.persistence;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface PersistentObject {

    public String getId();
    
    public Long getVersion();
}
