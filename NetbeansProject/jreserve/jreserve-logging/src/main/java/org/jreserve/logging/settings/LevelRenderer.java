package org.jreserve.logging.settings;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.jreserve.logging.Logger;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL_level_fatal=Fatal",
    "LBL_level_error=Error",
    "LBL_level_warn=Warn",
    "LBL_level_info=Info",
    "LBL_level_debug=Debug",
    "LBL_level_trace=Trace",
    "LBL_level_unknown=Unknown"
})
class LevelRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(getText());
        return this;
    }

    private String getText(Object value) {
        if(value instanceof Logger.Level)
            return getText((Logger.Level) value);
        return Bundle.LBL_level_unknown();
    }
    
    private String getText(Logger.Level level) {
        switch(level) {
            case FATAL: return Bundle.LBL_level_fatal();
            case ERROR: return Bundle.LBL_level_error();
            case WARN: return Bundle.LBL_level_warn();
            case INFO: return Bundle.LBL_level_info();
            case DEBUG: return Bundle.LBL_level_debug();
            case TRACE: return Bundle.LBL_level_trace();
            default: return Bundle.LBL_level_unknown();
        }
    }
}
