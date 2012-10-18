package org.jreserve.data.container;

import java.util.List;
import org.jreserve.data.ProjectData;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;

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
        for(ProjectData data : getChildValues(ProjectData.class)) {
            if(data.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
    
    private <T> List<T> getChildValues(Class<T> clazz) {
        return myElement.getChildValues(clazz);
    }
    
    void setMyElement(ProjectElement<ProjectDataContainer> element) {
        myElement = element;
    }
    
    public void addElement(ProjectElement<? extends ProjectData> element) {
        checkProject(element.getValue().getProject());
        checkAddableElement(element.getValue());
        int index = getIndex(element.getValue());
        myElement.addChild(index, element);
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
    
    private void checkProject(Project p) {
        if(p == null)
            throw new IllegalArgumentException("Value does not belongs to any project!");
        if(!p.getId().equals(project.getId()))
            throw new IllegalArgumentException("Value belongs to another project!");
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
}
