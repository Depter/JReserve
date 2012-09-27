package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.jreserve.data.DataImport.ImportType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ImportTypeListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value instanceof ImportType)
            setText(((ImportType) value).getUserName());
        return this;
    }
}
