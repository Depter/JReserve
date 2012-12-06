package org.jreserve.logging.settings;

import java.awt.Component;
import java.util.logging.Level;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.jreserve.logging.LoggingUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
class LevelRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(getText());
        return this;
    }

    private String getText(Object value) {
        if(!(value instanceof Level))
            return "";
        Level level = (Level) value;
        return getText(LoggingUtil.getUserName(level));
    }
}
