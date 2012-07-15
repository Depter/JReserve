package org.jreserve.oracledatabase.opendialog;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.jreserve.oracledatabase.OracleDatabase;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class OracleDbRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value instanceof OracleDatabase)
            setText(((OracleDatabase) value).getName());
        return this;
    }

    
}
