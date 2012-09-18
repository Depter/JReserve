package org.jreserve.project.system.util;

/**
 *
 * @author Peter Decsi
 */
public interface ProjectSystemListener {

    /**
     * Services should decide here, wether they are interested in a given value.
     */
    public boolean isInterested(Object value);

}
