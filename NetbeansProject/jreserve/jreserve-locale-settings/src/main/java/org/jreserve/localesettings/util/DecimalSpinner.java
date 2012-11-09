package org.jreserve.localesettings.util;

import java.text.DecimalFormat;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DecimalSpinner extends JSpinner {

    public DecimalSpinner() {
        super(createModel());
    }

    private static SpinnerModel createModel() {
        DecimalFormat format = LocaleSettings.getDecimalFormat();
        int value = format.getMaximumFractionDigits();
        return new SpinnerNumberModel(value, 0, Integer.MAX_VALUE, 1);
    }
    
    public int getIntValue() {
        Object o = super.getValue();
        if(o == null || !(o instanceof Integer))
            return 0;
        return ((Integer)o).intValue();
    }

}
