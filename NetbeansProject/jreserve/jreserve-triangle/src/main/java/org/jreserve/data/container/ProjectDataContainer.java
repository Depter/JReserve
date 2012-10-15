package org.jreserve.data.container;

import java.util.List;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.triangle.TriangleProjectElement;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.entities.Vector;
import org.jreserve.triangle.VectorProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataContainer {

    private Project project;
    private ProjectElement<ProjectDataContainer> myElement;
    
    public ProjectDataContainer(Project project) {
        this.project = project;
    }
    
    public Project getProject() {
        return project;
    }
    
    public ProjectElement<ProjectDataContainer> getElement() {
        return myElement;
    }
    
    public boolean containsName(String name) {
        return containsVectorName(name) || containsTriangleName(name);
    }
    
    private boolean containsVectorName(String name) {
        for(Vector vector : getChildValues(Vector.class))
            if(vector.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
    
    private <T> List<T> getChildValues(Class<T> clazz) {
        return myElement.getChildValues(clazz);
    }
    
    private boolean containsTriangleName(String name) {
        for(Triangle triangle : getChildValues(Triangle.class))
            if(triangle.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
    
    void setMyElement(ProjectElement<ProjectDataContainer> element) {
        myElement = element;
    }
    
    public <T extends PersistentObject> void addElement(ProjectElement<T> element) {
        checkAddableElement(element.getValue());
        myElement.addChild(element);
    }
    
    private <T> void checkAddableElement(T value) {
        if(value == null)
            throw new NullPointerException("Value is null!");
        if(containsValue(value))
            throw new IllegalArgumentException("Value already added!" + value);
    }
    
    private <T> boolean containsValue(T value) {
        List<T> values = (List<T>) getChildValues(value.getClass());
        for(T v : values)
            if(v.equals(value))
                return true;
        return false;
    }
    
    public ProjectElement<Vector> addVector(Vector vector) {
        checkProject(vector.getProject());
        VectorProjectElement element = new VectorProjectElement(vector);
        addElement(element);
        return element;
    }
    
    private void checkProject(Project p) {
        if(p == null)
            throw new IllegalArgumentException("Value does not belongs to any project!");
        if(!p.getId().equals(project.getId()))
            throw new IllegalArgumentException("Value belongs to another project!");
    }
    
    public ProjectElement<Triangle> addTriangle(Triangle triangle) {
        checkProject(triangle.getProject());
        TriangleProjectElement element = new TriangleProjectElement(triangle);
        addElement(element);
        return element;
    }
    
    public <T> List<ProjectElement<T>> getProjectElements(Class<T> clazz) {
        return myElement.getChildren(clazz);
    }
}
