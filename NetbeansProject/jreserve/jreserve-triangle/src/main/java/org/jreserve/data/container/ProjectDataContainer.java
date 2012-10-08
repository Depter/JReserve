package org.jreserve.data.container;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.vector.VectorProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataContainer {

    private Project project;
    private List<Vector> vectors = new ArrayList<Vector>();
    private List<Triangle> triangles = new ArrayList<Triangle>();
    private ProjectElement myElement;
    
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
    
    void setMyElement(ProjectElement element) {
        myElement = element;
    }
    
    public void addVector(Vector vector) {
        if(vector == null)
            throw new NullPointerException("Vector is null!");
        if(vector.getProject() == null)
            throw new IllegalArgumentException("Vector does not belongs to any project!");
        if(vector.getProject().getId() != project.getId())
            throw new IllegalArgumentException("Vector does belongs to another project!");
        if(vectors.contains(vector))
            throw new IllegalArgumentException("Vector already added!");
        createElement(vector);
    }
    
    private void createElement(Vector vector) {
        vectors.add(vector);
        VectorProjectElement element = new VectorProjectElement(vector);
        myElement.addChild(element);
    }
}
