package org.jreserve.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Query {

    public static enum FlushMode {
        AUTO, COMMIT;
    }
    
    public static enum Temporal {
        DATE,
        TIME, 
        TIMESTAMP
    };

    /**
     * Execute an update or delete statement. 
     */
    public int executeUpdate();

    /**
     * Execute a SELECT query and return the query results as a List. 
     */
    public List getResultList(); 
          
    /**
     * Execute a SELECT query that returns a single result. 
     */
    public Object getSingleResult(); 
          
    /**
     * Set the position of the first result to retrieve. 
     */
    public Query setFirstResult(int startPosition); 
          
    /**
     * Set the flush mode type to be used for the query execution. 
     */
     public Query setFlushMode(FlushMode flush);
          
    /**
     * Set the maximum number of results to retrieve. 
     */
     public Query setMaxResults(int maxResult);
          
    /**
     * Bind an instance of java.util.Calendar to a positional parameter. 
     */
     public Query setParameter(int position, Calendar value, Temporal temporalType);
          
    /**
     * Bind an instance of java.util.Date to a positional parameter. 
     */
     public Query setParameter(int position, Date value, Temporal temporalType);
          
    /**
     * Bind an argument to a positional parameter. 
     */
     public Query setParameter(int position, Object value);
          
    /**
     * Bind an instance of java.util.Calendar to a named parameter. 
     */
     public Query setParameter(String name, Calendar value, Temporal temporalType);
          
    /**
     * Bind an instance of java.util.Date to a named parameter. 
     */
     public Query setParameter(String name, Date value, Temporal temporalType);
          
    /**
     * Bind an argument to a named parameter.
     */
     public Query setParameter(String name, Object value);
          
    /**
     * Bind a String argument to a named parameter.
     */
     public Query setParameter(String name, String value);
          
    /**
     * Bind a String argument to a positional parameter. 
     */
     public Query setParameter(int position, String value); 
}
