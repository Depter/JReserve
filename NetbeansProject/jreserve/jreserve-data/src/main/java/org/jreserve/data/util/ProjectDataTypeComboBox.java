package org.jreserve.data.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.jreserve.data.ProjectDataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectDataTypeComboBox extends JComboBox {

    private boolean showTriangle = true;
    private boolean showVector = true;
    
    private DefaultComboBoxModel model;
    private List<ProjectDataType> types = new ArrayList<ProjectDataType>();
    
    public ProjectDataTypeComboBox() {
        this(Collections.EMPTY_LIST);
    }
    
    public ProjectDataTypeComboBox(List<ProjectDataType> dataTypes) {
        super(new DefaultComboBoxModel());
        super.setRenderer(new ProjectDataTypeComboRenderer());
        model = (DefaultComboBoxModel) super.getModel();
        types.addAll(dataTypes);
        filterTypes();
    }
    
    private void filterTypes() {
        ProjectDataType selected = clearData();
        filterData();
        setSelected(selected);
    }
    
    private ProjectDataType clearData() {
        ProjectDataType selected = (ProjectDataType) getSelectedItem();
        model.removeAllElements();
        return selected;
    }
    
    private void filterData() {
        for(ProjectDataType dt : types)
            if(isAdded(dt))
                model.addElement(dt);
    }
    
    private boolean isAdded(ProjectDataType dataType) {
        return dataType.isTriangle()? showTriangle : showVector;
    }
    
    private void setSelected(ProjectDataType selected) {
        if(selected != null) {
            int index = model.getIndexOf(selected);
            if(index >= 0)
                setSelectedIndex(index);
        }
    }
    
    public boolean isTriangleShown() {
        return showTriangle;
    }
    
    public void setShowTriangle(boolean show) {
        showTriangle = show;
        filterTypes();
    }
    
    public boolean isVectorShown() {
        return showVector;
    }
    
    public void setShowVector(boolean show) {
        showVector = show;
        filterTypes();
    }
    
    public void setProjectDataTypes(List<ProjectDataType> dataTypes) {
        types.clear();
        types.addAll(dataTypes);
        filterTypes();
    }
}
