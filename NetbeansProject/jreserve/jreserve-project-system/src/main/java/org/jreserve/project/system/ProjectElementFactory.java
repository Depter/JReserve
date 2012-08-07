package org.jreserve.project.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProjectElementFactory {
    
    public boolean isInterested(Object value);
    
    public List<ProjectElement> createChildren(Object value, Session session);
    
    public final static int MAX_PRIORITY = 0;

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public static @interface Registration {
        int value() default Integer.MAX_VALUE;
    }
}
