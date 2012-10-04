package org.jreserve.data.container;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.project.entities.Project;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataContainer {

    private Project project;
    private List<Vector> vectors = new ArrayList<Vector>();
    private List<Triangle> triangles = new ArrayList<Triangle>();
    
    public ProjectDataContainer(Project project) {
        this.project = project;
    }
    
    public boolean containsName(String name) {
        return containsVectorName(name) || containsTriangleName(name);
    }
    
    private boolean containsVectorName(String name) {
        for(Vector vector : vectors)
            if(vector.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
    
    private boolean containsTriangleName(String name) {
        for(Triangle triangle : triangles)
            if(triangle.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
}
