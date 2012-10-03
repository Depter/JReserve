package org.jreserve.dataimport.clipboardtable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectDataTypeComboModel extends DefaultComboBoxModel implements Comparator<ProjectDataType>{

    private List<ProjectDataType> types = null;

    @Override
    public Object getElementAt(int index) {
        return types.get(index);
    }

    @Override
    public int getSize() {
        if(types == null)
            return 0;
        return types.size();
    }
    
    void setProject(ProjectElement<Project> element) {
        int size = getSize();
        if(element == null)
            types = null;
        else
            loadTypes(element);
        fireDataChanged(size);
    }
    
    private void loadTypes(ProjectElement<Project> element) {
        types = element.getChildValues(ProjectDataType.class);
        Collections.sort(types, this);
    }

    private void fireDataChanged(int oldSize) {
        if(oldSize > 0)
            fireIntervalRemoved(this, 0, oldSize-1);
        int newSize = getSize();
        if(newSize > 0)
            fireIntervalAdded(this, 0, newSize-1);
    }
    
    @Override
    public int compare(ProjectDataType o1, ProjectDataType o2) {
        return o1.getDbId() - o2.getDbId();
    }
}
