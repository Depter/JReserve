package org.jreserve.project.filesystem;

import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface EntityCookie<E> extends Node.Cookie {

    public E getEntity();
}
