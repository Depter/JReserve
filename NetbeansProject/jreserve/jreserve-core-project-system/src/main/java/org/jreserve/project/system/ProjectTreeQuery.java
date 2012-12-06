package org.jreserve.project.system;

import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectTreeQuery {

    public static <T> ProjectElement<T> findElement(T value) {
        return findElement(RootElement.getDefault(), value);
    }
    
    public static <T> ProjectElement<T> findElement(ProjectElement parent, T value) {
        for(ProjectElement child : (List<ProjectElement>) parent.getChildren()) {
            
            if(value.equals(child.getValue()))
                return child;
            
            ProjectElement element = findElement(child, value);
            if(element != null)
                return element;
        }
        return null;
    }
}
