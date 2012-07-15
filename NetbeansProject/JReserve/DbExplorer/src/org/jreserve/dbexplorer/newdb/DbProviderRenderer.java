package org.jreserve.dbexplorer.newdb;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.jreserve.database.api.DatabaseProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DbProviderRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value instanceof DatabaseProvider)
            renderProvider((DatabaseProvider) value);
        return this;
    }
    
    private void renderProvider(DatabaseProvider provider) {
        setIcon(new ImageIcon(provider.getIcon()));
        setText(provider.getName());
    }

    
}
