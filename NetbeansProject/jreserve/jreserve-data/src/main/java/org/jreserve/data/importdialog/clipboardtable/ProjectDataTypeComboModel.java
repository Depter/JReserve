package org.jreserve.data.importdialog.clipboardtable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.data.projectdatatype.ProjectDataTypeUtil;
import org.jreserve.project.entities.Project;

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
    
    void setProject(Project project) {
        int size = getSize();
        if(project == null)
            types = null;
        else
            loadTypes(project);
        fireDataChanged(size);
    }
    
    private void loadTypes(Project project) {
        types = ProjectDataTypeUtil.getDefault().getValues(project);
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
