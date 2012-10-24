package org.jreserve.persistence;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface PersistentObject {
    public final static String COLUMN_DEF = "varchar (36) not null";

    public String getId();
    
    public Long getVersion();
}
