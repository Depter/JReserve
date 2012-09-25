package org.jreserve.data.importdialog.clipboardtable;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.jreserve.data.DataImport.ImportType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ImportTypeListRenderer.AddNew=Add only new data",
    "LBL.ImportTypeListRenderer.DeleteOld=Overwrite old data"
})
class ImportTypeListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value instanceof ImportType)
            setText(getCaption((ImportType) value));
        return this;
    }

    private String getCaption(ImportType type) {
        switch(type) {
            case ADD_NEW:
                return Bundle.LBL_ImportTypeListRenderer_AddNew();
            case DELETE_OLD:
                return Bundle.LBL_ImportTypeListRenderer_DeleteOld();
            default:
                throw new IllegalArgumentException("Unknown ImportType: "+type);
        }
    }
}
