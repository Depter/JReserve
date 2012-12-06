package org.jreserve.localesettings;

import java.awt.Component;
import java.util.Locale;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.LocaleLisRenderer.Empty=Select language"
})
class LocaleListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(value);
        return this;
    }
    
    private void setText(Object value) {
        if(value instanceof Locale) {
            Locale l = (Locale) value;
            setText(l.getDisplayLanguage(Locale.ENGLISH));
        } else {
            setText(Bundle.LBL_LocaleLisRenderer_Empty());
        }
    }
}
