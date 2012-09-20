package org.jreserve.project.system;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.persistence.Session;

/**
 * This class handles the load process for {@link ProjectElement ProjectElements}. 
 * Subclasses should only decide, whether they are interested in a given parent object, 
 * and if they are, then load the child values.
 * 
 * <p>
 * After the subclass loaded the child values, this class checks for
 * factories that are interested in the children and loads all the 
 * sub children if any.
 * </p>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractProjectElementFactory<T> implements ProjectElementFactory {
    
    private final static Logger logger = Logger.getLogger(AbstractProjectElementFactory.class.getName());
    
    @Override
    public List<ProjectElement> createChildren(Object value, Session session) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(T child : getChildValues(value, session))
            result.add(getChildElement(child, session));
        return result;
    }

    /**
     * Loads the child values for the given object (ie. claim types for a given lob).
     */
    protected abstract List<T> getChildValues(Object value, Session session);

    /**
     * Creates an element for each value created by {@link #getChildValues(java.lang.Object, org.jreserve.persistence.Session) getChildValues()}.
     * 
     * <p>
     * Only override this method if you check for factories, interested in your value
     * or definitly want your element to be a leaf.
     * </p>
     */
    protected ProjectElement getChildElement(T value, Session session) {
        logger.log(Level.FINE, "Loaded project element: %s", value);
        ProjectElement element = createProjectElement(value);
        for(ProjectElement child : getChildren(value, session))
            element.addChild(child);
        return element;
    }
    
    /**
     * Creates a simple element for the given value, without children.
     * The basic implementation simply returns an instance of 
     * {@link ProjectElement ProjectElement}. If you want to 
     * provide something else, do it here.
     */
    protected ProjectElement createProjectElement(T value) {
        return new ProjectElement(value);
    }
    
    private List<ProjectElement> getChildren(T value, Session session) {
        List<ProjectElement> children = new ArrayList<ProjectElement>();
        for(ProjectElementFactory factory : RootElement.getFactories(value))
            children.addAll(factory.createChildren(value, session));
        return children;
    }
}
