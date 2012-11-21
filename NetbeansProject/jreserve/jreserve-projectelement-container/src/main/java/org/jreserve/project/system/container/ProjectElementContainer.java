package org.jreserve.project.system.container;

import java.util.List;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.util.ProjectData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectElementContainer {
    
    public static ProjectElementContainer getPathElement(int position) {
        return new ProjectElementContainer(null, position);
    }
    
    private Project project;
    private ProjectElement<ProjectElementContainer> myElement;
    private int position;
    
    public ProjectElementContainer(Project project, int position) {
        this.project = project;
        this.position = position;
    }
    
    public Project getProject() {
        return project;
    }
    
    public int getPosition() {
        return position;
    }
    
    public ProjectElement<ProjectElementContainer> getElement() {
        return myElement;
    }
    
    public boolean containsName(String name) {
        return containsName(name, Object.class);
    }
    
    public <T> boolean containsName(String name, Class<T> clazz) {
        for(ProjectElement<T> element : getChildValues(clazz)) {
            if(isSameName(element, name))
                return true;
        }
        return false;
    }
    
    private <T> List<ProjectElement<T>> getChildValues(Class<T> clazz) {
        return myElement.getChildren(clazz);
    }
    
    private boolean isSameName(ProjectElement<?> element, String name) {
        Object o = element.getProperty(ProjectElement.NAME_PROPERTY);
        if(name == null) return o == null;
        if(o instanceof String)
            return name.equalsIgnoreCase((String) o);
        return false;
    }
    
    protected void setMyElement(ProjectElement<ProjectElementContainer> element) {
        myElement = element;
    }
    
    public void addElement(ProjectElement<? extends ProjectData> element) {
        checkProject(element.getValue().getProject());
        checkAddableElement(element.getValue());
        int index = getIndex(element.getValue());
        myElement.addChild(index, element);
    }
    
    private void checkProject(Project p) {
        if(p == null)
            throw new IllegalArgumentException("Value does not belongs to any project!");
        if(!p.getId().equals(project.getId()))
            throw new IllegalArgumentException("Value belongs to another project!");
    }
    
    private int getIndex(ProjectData data) {
        List<ProjectElement<ProjectData>> children = myElement.getChildren(ProjectData.class);
        for(int i=0, count=children.size(); i<count; i++) {
            if(isBefore(data, children.get(i).getValue()))
                return i;
        }
        return children.size();
    }
    
    private boolean isBefore(ProjectData d1, ProjectData d2) {
        int p1 = d1.getPosition();
        int p2 = d2.getPosition();
        if(p1 == p2)
            return d1.getName().compareToIgnoreCase(d2.getName()) < 0;
        return p1 < p2;
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
    
    public <T> List<ProjectElement<T>> getProjectElements(Class<T> clazz) {
        return myElement.getChildren(clazz);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ProjectElementContainer)
            return position == ((ProjectElementContainer)o).position;
        return false;
    }
    
    @Override
    public int hashCode() {
        return position;
    }
}
