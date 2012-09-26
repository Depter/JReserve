package org.jreserve.data.util;

import java.text.Format;
import javax.swing.JComboBox;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractFormatCombo<T extends Format> extends JComboBox {

    AbstractFormatCombo(String[] formats) {
        super(formats);
        setEditable(true);
    }
    
    public String getFormatValue() {
        return (String) getSelectedItem();
    }
    
    public T getFormat() {
        String format = (String) getSelectedItem();
        if(format == null)
            return null;
        return createFormat(format);
    }
    
    protected abstract T createFormat(String format);
    
    public boolean isValidFormat() {
        return getFormat() != null;
    }
}
