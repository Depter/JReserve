package org.jreserve.project.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Interface to load instances from the database, after the 
 * connection is made.
 * 
 * <p>
 * The loading process looks like as follows:
 * <ol>
 *   <li>User makes a new connection, and succesfully logs in to the database.</li>
 *   <li>The {@link RootElement RootElement} obtains a {@link org.jreserve.persistence.Session Session} instance.</li>
 *   <li>The RootElement asks for instances of this inteface, which are interested in
 *       an instance of {@link RootElement.RootValue RootValue} object.
 *   </li>
 *   <li>The RootElement calls {@link #createChildren(java.lang.Object, org.jreserve.persistence.Session) createChildren()} on
 *       all factories and adds them to the root.
 *   </li>
 *   <li>The RootElement closes the session.</li>
 * </ol>
 * This process implies that all factory should ask for factories, which are 
 * interested in the value, created by the factory. Further the factory should
 * pass on the session object to the sub factories.
 * </p>
 * 
 * <p>
 * The session instance is already initialized, and has an opened transaction. If
 * the caller tries to close, rollback or comit it, or save something an exception
 * will be thrown.
 * </p>
 * 
 * <p>
 * If more then one factory is interested in a given object, their position
 * is used to order them. Order of factories with the same position is unpredictible.
 * See {@link org.jreserve.project.system.ProjectElementFactory.Registration Registration}.
 * </p>
 * 
 * <p>
 * In most cases it will be enough to subclass {@link AbstractProjectElementFactory AbstractProjectElementFactory},
 * which takes care much of the work.
 * </p>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProjectElementFactory {
    
    /**
     * 
     */
    public final static int MAX_PRIORITY = 0;
    
    /**
     * Returns true if the factory wishes to provide child elements
     * to the <i>value</i>.
     */
    public boolean isInterested(Object value);
    
    /**
     * Creates zero or more child elements for the given value. The caller should
     * make sure that the returened elements are added to the element representing 
     * the passed in value.
     * 
     * 
     * <p>
     * The session instance is already initialized, and has an opened transaction. If
     * the caller tries to close, rollback or comit it, or save something an exception
     * will be thrown.
     * </p>
     * 
     * @param value The value the children are created for.
     * @param session The session, to load the children.
     * @return List of children for the value.
     */
    public List<ProjectElement> createChildren(Object value);

    /**
     * Registers a {@link ProjectElementFactory ProjectElementFactory} in the 
     * layer file. The registered class should have a no arg public constructor.
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public static @interface Registration {
        
        /**
         * The position of this factory. This value specifies
         * the order among factories, which are intereseted in the
         * same object. Factories with lower position will be called first.
         * 
         * <p>The minimum value is 0.</p>
         */
        int value() default Integer.MAX_VALUE;
    }
}
