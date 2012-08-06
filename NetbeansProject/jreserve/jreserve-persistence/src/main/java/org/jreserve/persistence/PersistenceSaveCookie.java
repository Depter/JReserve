package org.jreserve.persistence;

import org.openide.cookies.SaveCookie;

/**
 * {@link SaveCookie SaveCookie} to mark objects that require saving to a 
 * database. Instances of this cookie will be used, when the actual persistence 
 * database is closed.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface PersistenceSaveCookie extends SaveCookie {

}
