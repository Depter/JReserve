package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.jreserve.data.entities.ProjectDataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProjectDataTypeComboRenderer extends DefaultListCellRenderer {

    private final static String FORMAT = "%-4d - %s";
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value instanceof ProjectDataType)
            setText((ProjectDataType) value);
        return this;
    }
    
    private void setText(ProjectDataType type) {
        int dbId = type.getDbId();
        String name = type.getName();
        String text = String.format(FORMAT, dbId, name);
        super.setText(text);
    }
}
